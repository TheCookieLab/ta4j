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
package org.ta4j.core.indicators.ichimoku;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.HighestValueIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.indicators.helpers.LowestValueIndicator;
import org.ta4j.core.num.Num;

/**
 * An abstract class for Ichimoku clouds indicators.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud</a>
 */
public class IchimokuLineIndicator extends CachedIndicator<Num> {

    /** The period high */
    private final Indicator<Num> periodHigh;

    /** The period low */
    private final Indicator<Num> periodLow;

    /**
     * Contructor.
     * 
     * @param series   the series
     * @param barCount the time frame
     */
    public IchimokuLineIndicator(BarSeries series, int barCount) {
        super(series);
        periodHigh = new HighestValueIndicator(new HighPriceIndicator(series), barCount);
        periodLow = new LowestValueIndicator(new LowPriceIndicator(series), barCount);
    }

    @Override
    protected Num calculate(int index) {
        return periodHigh.getValue(index).plus(periodLow.getValue(index)).dividedBy(numOf(2));
    }
}
