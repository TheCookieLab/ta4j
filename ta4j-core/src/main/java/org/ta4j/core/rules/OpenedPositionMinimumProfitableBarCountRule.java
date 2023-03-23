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
package org.ta4j.core.rules;

import org.ta4j.core.BarSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * Rule to specify minimum profitable bar count for opened position. Using this
 * rule makes sense only for exit rule (for entry rule
 * {@link OpenedPositionMinimumProfitableBarCountRule#isSatisfied(int, TradingRecord)}
 * always return false.
 */
public class OpenedPositionMinimumProfitableBarCountRule extends AbstractRule {

    private final BarSeries series;
    /**
     * Minimum profitable bar count for opened trade.
     */
    private final int barCount;

    public OpenedPositionMinimumProfitableBarCountRule(BarSeries series, int barCount) {
        if (barCount < 1) {
            throw new IllegalArgumentException("Bar count must be positive");
        }
        this.series = series;
        this.barCount = barCount;
    }

    /**
     * Returns true if opened trade reached minimum bar count specified in
     * {@link OpenedPositionMinimumProfitableBarCountRule#barCount}
     *
     * @param index the bar index
     * @param tradingRecord the required trading history
     * @return true if opened trade reached minimum bar count specified in
     * {@link OpenedPositionMinimumProfitableBarCountRule#barCount}, otherwise
     * false
     */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (tradingRecord == null || tradingRecord.isClosed()) {
            return false;
        }

        final int entryIndex = tradingRecord.getCurrentPosition().getEntry().getIndex();

        int profitableBars = 0;

        for (int i = index; i >= entryIndex; i--) {
            Num currentPrice = this.series.getBar(i).getClosePrice();
            Num estimatedExitFee = tradingRecord.getTransactionCostModel().calculate(currentPrice, tradingRecord.getCurrentPosition().getEntry().getAmount());

            if (tradingRecord.getCurrentPosition().getEntry().isBuy()) {
                Num breakEvenPrice = tradingRecord.getCurrentPosition().getEntry().getNetPrice().plus(estimatedExitFee);
                if (currentPrice.isGreaterThan(breakEvenPrice)) {
                    profitableBars++;
                } else {
                    break;
                }
            } else {
                Num breakEvenPrice = tradingRecord.getCurrentPosition().getEntry().getNetPrice().minus(estimatedExitFee);

                if (currentPrice.isLessThan(breakEvenPrice)) {
                    profitableBars++;
                } else {
                    break;
                }
            }

        }

        return profitableBars >= barCount;
    }

    /**
     * @return the {@link #barCount}
     */
    public int getBarCount() {
        return barCount;
    }
}
