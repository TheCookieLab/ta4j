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
package org.ta4j.core.indicators.aroon;

import static org.ta4j.core.num.NaN.NaN;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.HighestValueIndicator;
import org.ta4j.core.num.Num;

/**
 * Aroon up indicator.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon">chart_school:technical_indicators:aroon</a>
 */
public class AroonUpIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final HighestValueIndicator highestHighPriceIndicator;
    private final Indicator<Num> highPriceIndicator;
    private final Num hundred;
    private final Num barCountNum;

    /**
     * Constructor.
     *
     * @param highPriceIndicator the indicator for the high price (default
     *                           {@link HighPriceIndicator})
     * @param barCount           the time frame
     */
    public AroonUpIndicator(Indicator<Num> highPriceIndicator, int barCount) {
        super(highPriceIndicator);
        this.barCount = barCount;
        this.highPriceIndicator = highPriceIndicator;
        this.hundred = numOf(100);
        this.barCountNum = numOf(barCount);
        // + 1 needed for last possible iteration in loop
        this.highestHighPriceIndicator = new HighestValueIndicator(highPriceIndicator, barCount + 1);
    }

    /**
     * Default Constructor that is using the high price
     *
     * @param series   the bar series
     * @param barCount the time frame
     */
    public AroonUpIndicator(BarSeries series, int barCount) {
        this(new HighPriceIndicator(series), barCount);
    }

    @Override
    protected Num calculate(int index) {
        if (getBarSeries().getBar(index).getHighPrice().isNaN())
            return NaN;

        // Getting the number of bars since the highest close price
        int endIndex = Math.max(0, index - barCount);
        int nbBars = 0;
        for (int i = index; i > endIndex; i--) {
            if (highPriceIndicator.getValue(i).isEqual(highestHighPriceIndicator.getValue(index))) {
                break;
            }
            nbBars++;
        }

        return numOf(barCount - nbBars).dividedBy(barCountNum).multipliedBy(hundred);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }

}
