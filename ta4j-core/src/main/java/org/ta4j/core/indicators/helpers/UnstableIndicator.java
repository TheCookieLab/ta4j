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

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

/**
 * Indicator that returns NaN in unstable period
 */
public class UnstableIndicator extends CachedIndicator<Num> {

    private final int unstablePeriod;
    private final Indicator<Num> indicator;

    public UnstableIndicator(Indicator<Num> indicator, int unstablePeriod) {
        super(indicator);
        this.indicator = indicator;
        this.unstablePeriod = unstablePeriod;
    }

    @Override
    protected Num calculate(int index) {
        if (index < unstablePeriod) {
            return NaN.NaN;
        }
        return indicator.getValue(index);
    }
}
