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

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.GainIndicator;
import org.ta4j.core.indicators.helpers.LossIndicator;
import org.ta4j.core.num.Num;

/**
 * Relative strength index indicator.
 *
 * Computed using original Welles Wilder formula.
 */
public class RSIIndicator extends CachedIndicator<Num> {

    private final MMAIndicator averageGainIndicator;
    private final MMAIndicator averageLossIndicator;

    public RSIIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator);
        this.averageGainIndicator = new MMAIndicator(new GainIndicator(indicator), barCount);
        this.averageLossIndicator = new MMAIndicator(new LossIndicator(indicator), barCount);
    }

    @Override
    protected Num calculate(int index) {
        // compute relative strength
        Num averageGain = averageGainIndicator.getValue(index);
        Num averageLoss = averageLossIndicator.getValue(index);
        if (averageLoss.isZero()) {
            if (averageGain.isZero()) {
                return numOf(0);
            } else {
                return numOf(100);
            }
        }
        Num relativeStrength = averageGain.dividedBy(averageLoss);
        // compute relative strength index
        return numOf(100).minus(numOf(100).dividedBy(numOf(1).plus(relativeStrength)));
    }
}
