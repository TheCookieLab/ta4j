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
package org.ta4j.core.indicators.helpers;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * An indicator that returns the length of the current consecutive streak of
 * true or false values from another boolean indicator.
 */
public class ConsecutiveBooleanStreakIndicator extends CachedIndicator<Num> {

    private final Indicator<Boolean> indicator;
    private final boolean checkValue;

    /**
     * Constructor.
     *
     * @param indicator  the boolean indicator to check for streaks
     * @param checkValue the value (true or false) for which to count consecutive
     *                   streaks
     */
    public ConsecutiveBooleanStreakIndicator(Indicator<Boolean> indicator, boolean checkValue) {
        super(indicator.getBarSeries());
        this.indicator = indicator;
        this.checkValue = checkValue;
    }

    /**
     * Calculates the length of the current consecutive streak of the checkValue.
     *
     * @param index the bar index
     * @return the length of the streak as a Num
     */
    @Override
    protected Num calculate(int index) {
        int streak = 0;

        for (int i = index; i >= 0; i--) {
            if (this.indicator.getValue(i) != checkValue) {
                break;
            }

            streak++;
        }

        return this.indicator.numOf(streak);
    }
}
