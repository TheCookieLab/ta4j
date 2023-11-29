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

        Num entryPrice = tradingRecord.getCurrentPosition()
                .getEntry()
                .getNetPrice()
                .plus(tradingRecord.getCurrentPosition().getPositionCost());
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
