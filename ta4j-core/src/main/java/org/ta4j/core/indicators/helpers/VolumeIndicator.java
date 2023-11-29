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
 * Volume indicator.
 */
public class VolumeIndicator extends CachedIndicator<Num> {

    private int barCount;

    public VolumeIndicator(BarSeries series) {
        this(series, 1);
    }

    public VolumeIndicator(BarSeries series, int barCount) {
        super(series);
        this.barCount = barCount;
    }

    @Override
    protected Num calculate(int index) {
        int startIndex = Math.max(0, index - barCount + 1);
        Num sumOfVolume = numOf(0);
        for (int i = startIndex; i <= index; i++) {
            sumOfVolume = sumOfVolume.plus(getBarSeries().getBar(i).getVolume());
        }
        return sumOfVolume;
    }
}