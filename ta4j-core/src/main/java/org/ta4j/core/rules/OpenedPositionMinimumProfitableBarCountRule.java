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
     * @param index         the bar index
     * @param tradingRecord the required trading history
     * @return true if opened trade reached minimum bar count specified in
     *         {@link OpenedPositionMinimumProfitableBarCountRule#barCount},
     *         otherwise false
     */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (tradingRecord == null || tradingRecord.isClosed()) {
            return false;
        }

        final int entryIndex = tradingRecord.getCurrentPosition().getEntry().getIndex();

        int profitableBars = 0;

        for (int i = index; i > entryIndex; i--) {
            Num currentPrice = this.series.getBar(i).getClosePrice();
            Num estimatedExitFee = tradingRecord.getTransactionCostModel()
                    .calculate(currentPrice, tradingRecord.getCurrentPosition().getEntry().getAmount());

            if (tradingRecord.getCurrentPosition().getEntry().isBuy()) {
                Num breakEvenPrice = tradingRecord.getCurrentPosition().getEntry().getNetPrice().plus(estimatedExitFee);
                if (currentPrice.isGreaterThan(breakEvenPrice)) {
                    profitableBars++;
                } else {
                    break;
                }
            } else {
                Num breakEvenPrice = tradingRecord.getCurrentPosition()
                        .getEntry()
                        .getNetPrice()
                        .minus(estimatedExitFee);

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
