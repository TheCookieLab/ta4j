/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2022 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.rules;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.MultiplierIndicator;
import org.ta4j.core.num.Num;

/**
 * A stop-gain rule.
 *
 * Satisfied when the close price reaches the ATR-based gain threshold.
 */
public class ATRStopGainRule extends AbstractRule {

    private final MultiplierIndicator atr;

    private final Indicator<Num> signalPrice;

    /**
     * Constructor.
     *
     * @param series
     * @param atrMultiple
     * @param atrBarCount
     */
    public ATRStopGainRule(BarSeries series, double atrMultiple, int atrBarCount) {
        this.atr = new MultiplierIndicator(new ATRIndicator(series, atrBarCount), atrMultiple);
        this.signalPrice = new ClosePriceIndicator(series);
    }

    public ATRStopGainRule(BarSeries series) {
        this(series, 2, 14);
    }

    /**
     * This rule uses the {@code tradingRecord}.
     */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        boolean satisfied = false;

        if (tradingRecord == null || tradingRecord.isClosed()) {
            return satisfied; // No trading history or no position opened, no loss
        }

        Num entryPrice = tradingRecord.getCurrentPosition().getEntry().getNetPrice().plus(tradingRecord.getCurrentPosition().getPositionCost());
        Num currentPrice = signalPrice.getValue(index);
        Num atrValue = this.atr.getValue(index);

        if (tradingRecord.getCurrentPosition().getEntry().isBuy()) {
            satisfied = isBuyGainSatisfied(entryPrice, currentPrice, atrValue);
        } else {
            satisfied = isSellGainSatisfied(entryPrice, currentPrice, atrValue);
        }

        traceIsSatisfied(index, satisfied);
        return satisfied;
    }

    private boolean isBuyGainSatisfied(Num entryPrice, Num currentPrice, Num atrValue) {
        Num stopGainThreshold = entryPrice.plus(atrValue);
        return currentPrice.isGreaterThanOrEqual(stopGainThreshold);
    }

    private boolean isSellGainSatisfied(Num entryPrice, Num currentPrice, Num atrValue) {
        Num stopGainThreshold = entryPrice.minus(atrValue);
        return currentPrice.isLessThanOrEqual(stopGainThreshold);
    }
}
