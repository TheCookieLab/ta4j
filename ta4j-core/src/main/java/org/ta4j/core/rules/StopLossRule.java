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
 * Satisfied when the close price reaches the loss threshold.
 */
public class StopLossRule extends AbstractRule {

    /**
     * Constant value for 100
     */
    private final Num HUNDRED;

    /**
     * The close price indicator
     */
    private final Indicator<Num> signalPrice;

    /**
     * The loss percentage
     */
    private Num lossPercentage;

    /**
     * Constructor.
     *
     * @param signalPrice    the close price indicator
     * @param lossPercentage the loss percentage
     */
    public StopLossRule(Indicator<Num> signalPrice, Number lossPercentage) {
        this(signalPrice, signalPrice.numOf(lossPercentage));
    }

    /**
     * Constructor.
     *
     * @param signalPrice    the close price indicator
     * @param lossPercentage the loss percentage
     */
    public StopLossRule(Indicator<Num> signalPrice, Num lossPercentage) {
        this.signalPrice = signalPrice;
        this.lossPercentage = lossPercentage;
        this.HUNDRED = signalPrice.numOf(100);
    }

    /** This rule uses the {@code tradingRecord}. */
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
        Num lossRatioThreshold = HUNDRED.minus(lossPercentage).dividedBy(HUNDRED);
        Num threshold = entryPrice.multipliedBy(lossRatioThreshold);

        if (log.isDebugEnabled()) {
            log.debug("Long stop loss threshold at {}", threshold);
        }
        return currentPrice.isLessThanOrEqual(threshold);
    }

    private boolean isSellStopSatisfied(Num entryPrice, Num currentPrice) {
        Num lossRatioThreshold = HUNDRED.plus(lossPercentage).dividedBy(HUNDRED);
        Num threshold = entryPrice.multipliedBy(lossRatioThreshold);

        if (log.isDebugEnabled()) {
            log.debug("Short stop loss threshold at {}", threshold);
        }
        return currentPrice.isGreaterThanOrEqual(threshold);
    }
}
