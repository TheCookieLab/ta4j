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

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;

/**
 * Recursive cached {@link Indicator indicator}.
 *
 * Recursive indicators should extend this class.<br>
 * This class is only here to avoid (OK, to postpone) the StackOverflowError
 * that may be thrown on the first getValue(int) call of a recursive indicator.
 * Concretely when an index value is asked, if the last cached value is too
 * old/far, the computation of all the values between the last cached and the
 * asked one is executed iteratively.
 */
public abstract class RecursiveCachedIndicator<T> extends CachedIndicator<T> {

    /**
     * The recursion threshold for which an iterative calculation is executed. TODO
     * Should be variable (depending on the sub-indicators used in this indicator)
     */
    private static final int RECURSION_THRESHOLD = 100;

    /**
     * Constructor.
     *
     * @param series the related bar series
     */
    protected RecursiveCachedIndicator(BarSeries series) {
        super(series);
    }

    /**
     * Constructor.
     *
     * @param indicator a related indicator (with a bar series)
     */
    protected RecursiveCachedIndicator(Indicator<?> indicator) {
        this(indicator.getBarSeries());
    }

    @Override
    public T getValue(int index) {
        BarSeries series = getBarSeries();
        if (series != null) {
            final int seriesEndIndex = series.getEndIndex();
            if (index <= seriesEndIndex) {
                // We are not after the end of the series
                final int removedBarsCount = series.getRemovedBarsCount();
                int startIndex = Math.max(removedBarsCount, highestResultIndex);
                if (index - startIndex > RECURSION_THRESHOLD) {
                    // Too many uncalculated values; the risk for a StackOverflowError becomes high.
                    // Calculating the previous values iteratively
                    for (int prevIdx = startIndex; prevIdx < index; prevIdx++) {
                        super.getValue(prevIdx);
                    }
                }
            }
        }

        return super.getValue(index);
    }
}
