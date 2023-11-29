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

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Close Location Value (CLV) indicator.
 *
 * @see <a href="http://www.investopedia.com/terms/c/close_location_value.asp">
 *      http://www.investopedia.com/terms/c/close_location_value.asp</a>
 */
public class CloseLocationValueIndicator extends CachedIndicator<Num> {

    private final Num zero = numOf(0);

    public CloseLocationValueIndicator(BarSeries series) {
        super(series);
    }

    @Override
    protected Num calculate(int index) {
        final Bar bar = getBarSeries().getBar(index);
        final Num low = bar.getLowPrice();
        final Num high = bar.getHighPrice();
        final Num close = bar.getClosePrice();

        final Num diffHighLow = high.minus(low);

        return diffHighLow.isNaN() ? zero : ((close.minus(low)).minus(high.minus(close))).dividedBy(diffHighLow);
    }
}
