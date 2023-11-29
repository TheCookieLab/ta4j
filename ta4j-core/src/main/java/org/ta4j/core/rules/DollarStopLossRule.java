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
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * A stop-loss rule.
 *
 * Satisfied when the position loss reaches the specified amount.
 */
public class DollarStopLossRule extends AbstractRule {

    /**
     * The close price indicator
     */
    private final Indicator<Num> signalPrice;

    /**
     * The loss value in absolute terms
     */
    private final Num lossValue;

    /**
     * Constructor.
     *
     * @param signalPrice the close price indicator
     * @param lossValue   the loss percentage
     */
    public DollarStopLossRule(Indicator<Num> signalPrice, Number lossValue) {
        this(signalPrice, signalPrice.numOf(lossValue));
    }

    /**
     * Constructor.
     *
     * @param signalPrice the close price indicator
     * @param lossValue   the loss percentage
     */
    public DollarStopLossRule(Indicator<Num> signalPrice, Num lossValue) {
        this.signalPrice = signalPrice;
        this.lossValue = lossValue;
    }

    /**
     * This rule uses the {@code tradingRecord}.
     */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        boolean satisfied = false;
        // No trading history or no position opened, no loss
        if (tradingRecord != null) {
            Position currentPosition = tradingRecord.getCurrentPosition();
            if (currentPosition.isOpened()) {

                Num entryPrice = currentPosition.getEntry().getNetPrice();
                Num currentPrice = signalPrice.getValue(index);

                if (currentPosition.getEntry().isBuy()) {
                    satisfied = isBuyStopSatisfied(entryPrice, currentPrice);
                } else {
                    satisfied = isSellStopSatisfied(entryPrice, currentPrice);
                }
            }
        }
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }

    private boolean isBuyStopSatisfied(Num entryPrice, Num currentPrice) {
        Num currentLoss = entryPrice.minus(currentPrice);

        return currentLoss.isPositive() && currentLoss.isGreaterThanOrEqual(lossValue);
    }

    private boolean isSellStopSatisfied(Num entryPrice, Num currentPrice) {
        Num currentLoss = currentPrice.minus(entryPrice);

        return currentLoss.isPositive() && currentLoss.isGreaterThanOrEqual(lossValue);
    }
}
