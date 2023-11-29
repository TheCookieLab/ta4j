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
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.indicators.helpers.LowestValueIndicator;
import org.ta4j.core.num.Num;

/**
 * Aroon down indicator.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon">chart_school:technical_indicators:aroon</a>
 */
public class AroonDownIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final LowestValueIndicator lowestLowPriceIndicator;
    private final Indicator<Num> lowPriceIndicator;
    private final Num hundred;
    private final Num barCountNum;

    /**
     * Constructor.
     *
     * @param lowPriceIndicator the indicator for the low price (default
     *                          {@link LowPriceIndicator})
     * @param barCount          the time frame
     */
    public AroonDownIndicator(Indicator<Num> lowPriceIndicator, int barCount) {
        super(lowPriceIndicator);
        this.barCount = barCount;
        this.lowPriceIndicator = lowPriceIndicator;
        this.hundred = numOf(100);
        this.barCountNum = numOf(barCount);
        // + 1 needed for last possible iteration in loop
        this.lowestLowPriceIndicator = new LowestValueIndicator(lowPriceIndicator, barCount + 1);
    }

    /**
     * Default Constructor that is using the low price
     *
     * @param series   the bar series
     * @param barCount the time frame
     */
    public AroonDownIndicator(BarSeries series, int barCount) {
        this(new LowPriceIndicator(series), barCount);
    }

    @Override
    protected Num calculate(int index) {
        if (getBarSeries().getBar(index).getLowPrice().isNaN())
            return NaN;

        // Getting the number of bars since the lowest close price
        int endIndex = Math.max(0, index - barCount);
        int nbBars = 0;
        for (int i = index; i > endIndex; i--) {
            if (lowPriceIndicator.getValue(i).isEqual(lowestLowPriceIndicator.getValue(index))) {
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
