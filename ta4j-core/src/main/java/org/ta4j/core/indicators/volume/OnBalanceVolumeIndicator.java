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
package org.ta4j.core.indicators.volume;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.RecursiveCachedIndicator;
import org.ta4j.core.num.Num;

/**
 * On-balance volume indicator.
 *
 * @see <a href="https://www.investopedia.com/terms/o/onbalancevolume.asp">
 *      https://www.investopedia.com/terms/o/onbalancevolume.asp</a>
 */
public class OnBalanceVolumeIndicator extends RecursiveCachedIndicator<Num> {

    public OnBalanceVolumeIndicator(BarSeries series) {
        super(series);
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return numOf(0);
        }
        final Num prevClose = getBarSeries().getBar(index - 1).getClosePrice();
        final Num currentClose = getBarSeries().getBar(index).getClosePrice();

        final Num obvPrev = getValue(index - 1);
        if (prevClose.isGreaterThan(currentClose)) {
            return obvPrev.minus(getBarSeries().getBar(index).getVolume());
        } else if (prevClose.isLessThan(currentClose)) {
            return obvPrev.plus(getBarSeries().getBar(index).getVolume());
        } else {
            return obvPrev;
        }
    }
}
