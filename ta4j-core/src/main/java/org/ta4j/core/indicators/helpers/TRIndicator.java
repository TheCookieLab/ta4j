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

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * True range indicator.
 */
public class TRIndicator extends CachedIndicator<Num> {

    public TRIndicator(BarSeries series) {
        super(series);
    }

    @Override
    protected Num calculate(int index) {
        Num ts = getBarSeries().getBar(index).getHighPrice().minus(getBarSeries().getBar(index).getLowPrice());
        Num ys = index == 0 ? numOf(0)
                : getBarSeries().getBar(index).getHighPrice().minus(getBarSeries().getBar(index - 1).getClosePrice());
        Num yst = index == 0 ? numOf(0)
                : getBarSeries().getBar(index - 1).getClosePrice().minus(getBarSeries().getBar(index).getLowPrice());
        return ts.abs().max(ys.abs()).max(yst.abs());
    }
}
