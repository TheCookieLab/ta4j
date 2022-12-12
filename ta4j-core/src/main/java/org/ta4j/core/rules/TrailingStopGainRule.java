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

import org.ta4j.core.Position;
import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

/**
 * A variation on the stop-gain rule where upon reaching the target percentage gain we initiate a trailing stop loss instead of directly closing the position.
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
     * *
     * The trailing stop percentage
     */
    private final Num trailingStopPercentage;

    /**
     * *
     * The trailing stop initiated once the stop gain percentage is reached
     */
    private Rule trailingStopLossRule;

    /**
     * Constructor.
     *
     * @param closePrice the close price indicator
     * @param gainPercentageTrigger
     * @param trailingStopPercentage
     */
    public TrailingStopGainRule(ClosePriceIndicator closePrice, Number gainPercentageTrigger, Number trailingStopPercentage) {
        this(closePrice, closePrice.numOf(gainPercentageTrigger), closePrice.numOf(trailingStopPercentage));
    }

    /**
     * Constructor.
     *
     * @param closePrice the close price indicator
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

                if ((currentPosition.getEntry().isBuy() && isBuyGainSatisfied(entryPrice, currentPrice)) || (currentPosition.getEntry().isSell() && isSellGainSatisfied(entryPrice, currentPrice))) {
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
