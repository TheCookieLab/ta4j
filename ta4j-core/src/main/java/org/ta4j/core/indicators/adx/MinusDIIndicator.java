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
package org.ta4j.core.indicators.adx;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.MMAIndicator;
import org.ta4j.core.num.Num;

/**
 * -DI indicator. Part of the Directional Movement System
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_directional_index_adx">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:average_directional_index_adx</a>
 * @see <a href=
 *      "https://www.investopedia.com/terms/a/adx.asp">https://www.investopedia.com/terms/a/adx.asp</a>
 */
public class MinusDIIndicator extends CachedIndicator<Num> {

    private final MMAIndicator avgMinusDMIndicator;
    private final ATRIndicator atrIndicator;
    private final int barCount;

    public MinusDIIndicator(BarSeries series, int barCount) {
        super(series);
        this.barCount = barCount;
        this.avgMinusDMIndicator = new MMAIndicator(new MinusDMIndicator(series), barCount);
        this.atrIndicator = new ATRIndicator(series, barCount);
    }

    @Override
    protected Num calculate(int index) {
        return avgMinusDMIndicator.getValue(index).dividedBy(atrIndicator.getValue(index)).multipliedBy(numOf(100));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }
}
