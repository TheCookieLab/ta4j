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
package org.ta4j.core.indicators.candles;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * * Counts the number of consecutive bearish bars (close less than open)
 * starting from the current index and working backwards
 * 
 * @author David
 */
public class BearishCandleStreakIndicator extends CachedIndicator<Num> {

    /**
     * Constructor.
     *
     * @param series a bar series
     */
    public BearishCandleStreakIndicator(BarSeries series) {
        super(series);
    }

    @Override
    protected Num calculate(int index) {
        int streak = 0;

        for (int i = index; i >= 0; i--) {
            if (this.getBarSeries().getBar(i).isBearish()) {
                streak++;
            } else {
                break;
            }
        }

        return numOf(streak);

    }
}
