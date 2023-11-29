/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
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
/**
 * This copy of Woodstox XML processor is licensed under the
 * Apache (Software) License, version 2.0 ("the License").
 * See the License for details about distribution rights, and the
 * specific rights regarding derivate works.
 *
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/
 *
 * A copy is also included in the downloadable source code package
 * containing Woodstox, in file "ASL2.0", under the same directory
 * as this file.
 */
package org.ta4j.core.rules;

import org.ta4j.core.Indicator;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * A stop-gain rule.
 *
 * Satisfied when the close price reaches the gain threshold.
 */
public class DollarStopGainRule extends AbstractRule {

    /**
     * The signal price indicator (typically ClosePriceIndicator)
     */
    private final Indicator<Num> signalPrice;

    /**
     * The stop gain amount in dollar terms
     */
    private final Num gainAmount;

    /**
     * Constructor.
     *
     * @param signalPrice the close price indicator
     * @param gainAmount  the gain amount in dollar terms
     */
    public DollarStopGainRule(Indicator<Num> signalPrice, Number gainAmount) {
        this(signalPrice, signalPrice.numOf(gainAmount));
    }

    /**
     * Constructor.
     *
     * @param signalPrice the close price indicator
     * @param gainAmount  the gain amount in dollar terms
     */
    public DollarStopGainRule(Indicator<Num> signalPrice, Num gainAmount) {
        this.signalPrice = signalPrice;
        this.gainAmount = gainAmount;
    }

    /**
     * This rule uses the {@code tradingRecord}.
     */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        boolean satisfied = false;
        // No trading history or no position opened, no loss
        if (tradingRecord == null || tradingRecord.isClosed()) {
            return satisfied;
        }

        Num entryPrice = tradingRecord.getCurrentPosition().getEntry().getNetPrice();
        Num currentPrice = signalPrice.getValue(index);

        if (tradingRecord.getCurrentPosition().getEntry().isBuy()) {
            satisfied = isBuyGainSatisfied(entryPrice, tradingRecord.getCurrentPosition().getEntry().getCost(),
                    currentPrice);
        } else {
            satisfied = isSellGainSatisfied(entryPrice, tradingRecord.getCurrentPosition().getEntry().getCost(),
                    currentPrice);
        }

        traceIsSatisfied(index, satisfied);
        return satisfied;
    }

    private boolean isBuyGainSatisfied(Num entryPrice, Num entryFee, Num currentPrice) {
        Num stopGainThreshold = entryPrice.plus(entryFee).plus(this.gainAmount);
        return currentPrice.isGreaterThanOrEqual(stopGainThreshold);
    }

    private boolean isSellGainSatisfied(Num entryPrice, Num entryFee, Num currentPrice) {
        Num stopGainThreshold = entryPrice.minus(entryFee).minus(this.gainAmount);
        return currentPrice.isLessThanOrEqual(stopGainThreshold);
    }
}
