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
 * CumulativeSumIndicator calculates the cumulative sum of the values of another
 * indicator within a specified sum window.
 */
public class CumulativeSumIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> indicator;
    private final int sumWindow;

    /**
     * Constructor.
     *
     * @param indicator the indicator whose values are to be summed
     * @param sumWindow the number of bars to sum the indicator values
     * @throws IndexOutOfBoundsException if the sumWindow is negative
     */
    public CumulativeSumIndicator(Indicator<Num> indicator, int sumWindow) {
        super(indicator.getBarSeries());
        this.indicator = indicator;
        if (sumWindow < 0) {
            throw new IndexOutOfBoundsException("sumWindow cannot be negative");
        }
        this.sumWindow = sumWindow;
    }

    /**
     * Calculates the cumulative sum of the indicator values within the specified
     * bar count.
     *
     * @param index the index of the bar to calculate the cumulative sum up to
     * @return the cumulative sum of the indicator values within the specified bar
     *         count
     */
    @Override
    protected Num calculate(int index) {
        Num sum = numOf(0);
        int startIndex = Math.max(0, index - sumWindow + 1);

        for (int i = startIndex; i <= index; i++) {
            sum = sum.plus(this.indicator.getValue(i));
        }
        return sum;
    }
}
