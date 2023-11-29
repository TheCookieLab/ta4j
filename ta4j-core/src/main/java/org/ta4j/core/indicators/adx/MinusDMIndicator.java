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

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * -DM indicator.
 */
public class MinusDMIndicator extends CachedIndicator<Num> {

    public MinusDMIndicator(BarSeries series) {
        super(series);
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return numOf(0);
        }
        final Bar prevBar = getBarSeries().getBar(index - 1);
        final Bar currentBar = getBarSeries().getBar(index);

        final Num upMove = currentBar.getHighPrice().minus(prevBar.getHighPrice());
        final Num downMove = prevBar.getLowPrice().minus(currentBar.getLowPrice());
        if (downMove.isGreaterThan(upMove) && downMove.isGreaterThan(numOf(0))) {
            return downMove;
        } else {
            return numOf(0);
        }
    }
}
