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
package org.ta4j.core.indicators;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.num.NaN;
import static org.ta4j.core.num.NaN.NaN;
import org.ta4j.core.num.Num;

/**
 * Recent Swing High Indicator.
 */
public class RecentSwingHighIndicator extends CachedIndicator<Num> {

    /**
     * A swing high is a bar with a higher high than the bars both before and after
     * it. Defines the number of bars to consider on each side (e.g., 2 bars on each
     * side).
     */
    private final int surroundingBars;

    /**
     * *
     *
     * @param series
     * @param surroundingBars
     */
    public RecentSwingHighIndicator(BarSeries series, int surroundingBars) {
        super(series);

        if (surroundingBars <= 0) {
            throw new IllegalArgumentException("surroundingBars must be greater than 0");
        }
        this.surroundingBars = surroundingBars;
    }

    /**
     * * g
     *
     * @param series
     */
    public RecentSwingHighIndicator(BarSeries series) {
        this(series, 2);
    }

    /**
     * Calculates the value of the most recent swing high
     *
     * @param index the bar index
     * @return the value of the most recent swing high, otherwise {@link NaN}
     */
    @Override
    protected Num calculate(int index) {
        if (index < surroundingBars) {
            return NaN;
        }

        for (int i = index - 1; i >= surroundingBars; i--) {
            boolean isSwingHigh = true;
            Bar currentBar = getBarSeries().getBar(i);

            for (int j = 1; j <= surroundingBars; j++) {
                if (i + j > getBarSeries().getEndIndex()
                        || currentBar.getHighPrice().isLessThanOrEqual(getBarSeries().getBar(i - j).getHighPrice())
                        || currentBar.getHighPrice().isLessThanOrEqual(getBarSeries().getBar(i + j).getHighPrice())) {
                    isSwingHigh = false;
                    break;
                }
            }

            if (isSwingHigh) {
                return currentBar.getHighPrice();
            }
        }

        return NaN;
    }
}
