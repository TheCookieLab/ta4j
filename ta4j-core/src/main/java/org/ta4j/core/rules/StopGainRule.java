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
 * A stop-gain rule.
 *
 * Satisfied when the close price reaches the gain threshold.
 */
public class StopGainRule extends AbstractRule {

    /**
     * Constant value for 100
     */
    private final Num HUNDRED;

    /**
     * The signal price indicator (typically ClosePriceIndicator)
     */
    private final Indicator<Num> signalPrice;

    /**
     * The gain percentage
     */
    private final Num gainPercentage;

    /**
     * Constructor.
     *
     * @param signalPrice    the close price indicator
     * @param gainPercentage the gain percentage
     */
    public StopGainRule(Indicator<Num> signalPrice, Number gainPercentage) {
        this(signalPrice, signalPrice.numOf(gainPercentage));
    }

    /**
     * Constructor.
     *
     * @param signalPrice    the close price indicator
     * @param gainPercentage the gain percentage
     */
    public StopGainRule(Indicator<Num> signalPrice, Num gainPercentage) {
        this.signalPrice = signalPrice;
        this.gainPercentage = gainPercentage;
        HUNDRED = signalPrice.numOf(100);
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
                    satisfied = isBuyGainSatisfied(entryPrice, currentPrice);
                } else {
                    satisfied = isSellGainSatisfied(entryPrice, currentPrice);
                }
            }
        }
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }

    private boolean isBuyGainSatisfied(Num entryPrice, Num currentPrice) {
        Num lossRatioThreshold = HUNDRED.plus(gainPercentage).dividedBy(HUNDRED);
        Num threshold = entryPrice.multipliedBy(lossRatioThreshold);
        return currentPrice.isGreaterThanOrEqual(threshold);
    }

    private boolean isSellGainSatisfied(Num entryPrice, Num currentPrice) {
        Num lossRatioThreshold = HUNDRED.minus(gainPercentage).dividedBy(HUNDRED);
        Num threshold = entryPrice.multipliedBy(lossRatioThreshold);
        return currentPrice.isLessThanOrEqual(threshold);
    }
}
