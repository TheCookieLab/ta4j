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
 * Chande Momentum Oscillator indicator.
 *
 * @see <a href=
 *      "http://tradingsim.com/blog/chande-momentum-oscillator-cmo-technical-indicator/">
 *      http://tradingsim.com/blog/chande-momentum-oscillator-cmo-technical-indicator/</a>
 * @see <a href=
 *      "http://www.investopedia.com/terms/c/chandemomentumoscillator.asp">
 *      href="http://www.investopedia.com/terms/c/chandemomentumoscillator.asp"</a>
 */
public class CMOIndicator extends CachedIndicator<Num> {

    private final GainIndicator gainIndicator;
    private final LossIndicator lossIndicator;
    private final int barCount;

    /**
     * Constructor.
     *
     * @param indicator a price indicator
     * @param barCount  the time frame
     */
    public CMOIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator);
        this.gainIndicator = new GainIndicator(indicator);
        this.lossIndicator = new LossIndicator(indicator);
        this.barCount = barCount;
    }

    @Override
    protected Num calculate(int index) {
        Num sumOfGains = numOf(0);
        for (int i = Math.max(1, index - barCount + 1); i <= index; i++) {
            sumOfGains = sumOfGains.plus(gainIndicator.getValue(i));
        }
        Num sumOfLosses = numOf(0);
        for (int i = Math.max(1, index - barCount + 1); i <= index; i++) {
            sumOfLosses = sumOfLosses.plus(lossIndicator.getValue(i));
        }
        return sumOfGains.minus(sumOfLosses).dividedBy(sumOfGains.plus(sumOfLosses)).multipliedBy(numOf(100));
    }
}
