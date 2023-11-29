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

import org.ta4j.core.Position;
import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

/**
 * A variation on the stop-gain rule where upon reaching the target percentage
 * gain we initiate a trailing stop loss instead of directly closing the
 * position.
 *
 * Initiates a trailing stop when the close price reaches the gain threshold.
 */
public class TrailingStopGainRule extends AbstractRule {

    /**
     * Constant value for 100
     */
    private final Num HUNDRED;

    /**
     * The close price indicator
     */
    private final ClosePriceIndicator closePrice;

    /**
     * The gain percentage that initiates the trailing stop
     */
    private final Num gainPercentageTrigger;

    /**
     * * The trailing stop percentage
     */
    private final Num trailingStopPercentage;

    /**
     * * The trailing stop initiated once the stop gain percentage is reached
     */
    private Rule trailingStopLossRule;

    /**
     * Constructor.
     *
     * @param closePrice             the close price indicator
     * @param gainPercentageTrigger
     * @param trailingStopPercentage
     */
    public TrailingStopGainRule(ClosePriceIndicator closePrice, Number gainPercentageTrigger,
            Number trailingStopPercentage) {
        this(closePrice, closePrice.numOf(gainPercentageTrigger), closePrice.numOf(trailingStopPercentage));
    }

    /**
     * Constructor.
     *
     * @param closePrice             the close price indicator
     * @param gainPercentageTrigger
     * @param trailingStopPercentage
     */
    public TrailingStopGainRule(ClosePriceIndicator closePrice, Num gainPercentageTrigger, Num trailingStopPercentage) {
        this.closePrice = closePrice;
        this.gainPercentageTrigger = gainPercentageTrigger;
        this.trailingStopPercentage = trailingStopPercentage;
        this.trailingStopLossRule = null;
        HUNDRED = closePrice.numOf(100);
    }

    /**
     * This rule uses the {@code tradingRecord}.
     */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        boolean satisfied = false;

        if (this.trailingStopLossRule != null) {
            satisfied = this.trailingStopLossRule.isSatisfied(index, tradingRecord);
        } else if (tradingRecord != null) {
            Position currentPosition = tradingRecord.getCurrentPosition();
            if (currentPosition.isOpened()) {

                Num entryPrice = currentPosition.getEntry().getNetPrice();
                Num currentPrice = closePrice.getValue(index);

                if ((currentPosition.getEntry().isBuy() && isBuyGainSatisfied(entryPrice, currentPrice))
                        || (currentPosition.getEntry().isSell() && isSellGainSatisfied(entryPrice, currentPrice))) {
                    this.trailingStopLossRule = new TrailingStopLossRule(closePrice, trailingStopPercentage);
                }
            }
        }
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }

    private boolean isBuyGainSatisfied(Num entryPrice, Num currentPrice) {
        Num lossRatioThreshold = HUNDRED.plus(gainPercentageTrigger).dividedBy(HUNDRED);
        Num threshold = entryPrice.multipliedBy(lossRatioThreshold);
        return currentPrice.isGreaterThanOrEqual(threshold);
    }

    private boolean isSellGainSatisfied(Num entryPrice, Num currentPrice) {
        Num lossRatioThreshold = HUNDRED.minus(gainPercentageTrigger).dividedBy(HUNDRED);
        Num threshold = entryPrice.multipliedBy(lossRatioThreshold);
        return currentPrice.isLessThanOrEqual(threshold);
    }
}
