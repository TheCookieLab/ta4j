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
import org.ta4j.core.indicators.helpers.CloseLocationValueIndicator;
import org.ta4j.core.num.Num;

/**
 * Accumulation-distribution indicator.
 */
public class AccumulationDistributionIndicator extends RecursiveCachedIndicator<Num> {

    private final CloseLocationValueIndicator clvIndicator;

    public AccumulationDistributionIndicator(BarSeries series) {
        super(series);
        this.clvIndicator = new CloseLocationValueIndicator(series);
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return numOf(0);
        }

        // Calculating the money flow multiplier
        Num moneyFlowMultiplier = clvIndicator.getValue(index);

        // Calculating the money flow volume
        Num moneyFlowVolume = moneyFlowMultiplier.multipliedBy(getBarSeries().getBar(index).getVolume());

        return moneyFlowVolume.plus(getValue(index - 1));
    }
}
