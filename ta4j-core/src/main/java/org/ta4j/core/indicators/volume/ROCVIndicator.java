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
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Rate of change of volume (ROCVIndicator) indicator. Aka. Momentum of Volume
 *
 * The ROCVIndicator calculation compares the current volume with the volume "n"
 * periods ago.
 */
public class ROCVIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final Num hundred;

    /**
     * Constructor.
     *
     * @param series   the bar series
     * @param barCount the time frame
     */
    public ROCVIndicator(BarSeries series, int barCount) {
        super(series);
        this.barCount = barCount;
        this.hundred = numOf(100);
    }

    @Override
    protected Num calculate(int index) {
        int nIndex = Math.max(index - barCount, 0);
        Num nPeriodsAgoValue = getBarSeries().getBar(nIndex).getVolume();
        Num currentValue = getBarSeries().getBar(index).getVolume();
        return currentValue.minus(nPeriodsAgoValue).dividedBy(nPeriodsAgoValue).multipliedBy(hundred);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }
}
