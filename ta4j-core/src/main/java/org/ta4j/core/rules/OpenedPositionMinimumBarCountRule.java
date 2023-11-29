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

import org.ta4j.core.TradingRecord;

/**
 * Rule to specify minimum bar count for opened position. Using this rule makes
 * sense only for exit rule (for entry rule
 * {@link OpenedPositionMinimumBarCountRule#isSatisfied(int, TradingRecord)}
 * always return false.
 */
public class OpenedPositionMinimumBarCountRule extends AbstractRule {

    /**
     * Minimum bar count for opened trade.
     */
    private final int barCount;

    public OpenedPositionMinimumBarCountRule(int barCount) {
        if (barCount < 1) {
            throw new IllegalArgumentException("Bar count must be positive");
        }
        this.barCount = barCount;
    }

    /**
     * Returns true if opened trade reached minimum bar count specified in
     * {@link OpenedPositionMinimumBarCountRule#barCount}
     *
     * @param index         the bar index
     * @param tradingRecord the required trading history
     * @return true if opened trade reached minimum bar count specified in
     *         {@link OpenedPositionMinimumBarCountRule#barCount}, otherwise false
     */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (tradingRecord.getCurrentPosition().isOpened()) {
            final int entryIndex = tradingRecord.getLastEntry().getIndex();
            final int currentBarCount = index - entryIndex;
            return currentBarCount >= barCount;
        }
        return false;
    }

    /** @return the {@link #barCount} */
    public int getBarCount() {
        return barCount;
    }
}
