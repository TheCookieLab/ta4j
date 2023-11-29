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
import org.ta4j.core.num.Num;

/**
 * Simple multiplier indicator.
 * </p>
 */
public class MultiplierIndicator extends CachedIndicator<Num> {

    private Indicator<Num> indicator;

    private Num coefficient;

    public MultiplierIndicator(Indicator<Num> indicator, double coefficient) {
        super(indicator);
        this.indicator = indicator;
        this.coefficient = numOf(coefficient);
    }

    @Override
    protected Num calculate(int index) {
        return indicator.getValue(index).multipliedBy(coefficient);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " Coefficient: " + coefficient;
    }
}
