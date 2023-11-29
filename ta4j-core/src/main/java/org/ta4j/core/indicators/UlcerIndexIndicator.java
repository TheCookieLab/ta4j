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

import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

/**
 * Ulcer index indicator.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ulcer_index">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ulcer_index</a>
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/Ulcer_index">https://en.wikipedia.org/wiki/Ulcer_index</a>
 */
public class UlcerIndexIndicator extends CachedIndicator<Num> {

    private final Num hundred;
    private final Num zero;

    private Indicator<Num> indicator;
    private int barCount;

    /**
     * Constructor.
     *
     * @param indicator the indicator
     * @param barCount  the time frame
     */
    public UlcerIndexIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator);
        this.indicator = indicator;
        this.barCount = barCount;
        this.zero = numOf(0);
        this.hundred = numOf(100);
    }

    @Override
    protected Num calculate(int index) {
        final int startIndex = Math.max(0, index - barCount + 1);
        final int numberOfObservations = index - startIndex + 1;
        Num squaredAverage = zero;
        Num highestValue = indicator.getValue(startIndex);
        for (int i = startIndex; i <= index; i++) {
            Num currentValue = indicator.getValue(i);
            if (currentValue.isGreaterThan(highestValue)) {
                highestValue = currentValue;
            }
            Num percentageDrawdown = currentValue.minus(highestValue).dividedBy(highestValue).multipliedBy(hundred);
            squaredAverage = squaredAverage.plus(percentageDrawdown.pow(2));
        }
        squaredAverage = squaredAverage.dividedBy(numOf(numberOfObservations));
        return squaredAverage.sqrt();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }
}
