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
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.HighestValueIndicator;
import org.ta4j.core.num.Num;

/**
 * The Chandelier Exit (long) Indicator.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chandelier_exit">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chandelier_exit</a>
 */
public class ChandelierExitLongIndicator extends CachedIndicator<Num> {

    private final HighestValueIndicator high;
    private final ATRIndicator atr;
    private final Num k;

    /**
     * Constructor.
     *
     * @param series the bar series
     */
    public ChandelierExitLongIndicator(BarSeries series) {
        this(series, 22, 3);
    }

    /**
     * Constructor.
     *
     * @param series   the bar series
     * @param barCount the time frame (usually 22)
     * @param k        the K multiplier for ATR (usually 3.0)
     */
    public ChandelierExitLongIndicator(BarSeries series, int barCount, double k) {
        super(series);
        high = new HighestValueIndicator(new HighPriceIndicator(series), barCount);
        atr = new ATRIndicator(series, barCount);
        this.k = numOf(k);
    }

    @Override
    protected Num calculate(int index) {
        return high.getValue(index).minus(atr.getValue(index).multipliedBy(k));
    }
}
