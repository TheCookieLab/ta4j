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
 * Returns the previous (n-th) value of an indicator
 */
public class PreviousValueIndicator extends CachedIndicator<Num> {

    private final int n;
    private Indicator<Num> indicator;

    /**
     * Constructor.
     *
     * @param indicator the indicator of which the previous value should be
     *                  calculated
     */
    public PreviousValueIndicator(Indicator<Num> indicator) {
        this(indicator, 1);
    }

    /**
     * Constructor.
     *
     * @param indicator the indicator of which the previous value should be
     *                  calculated
     * @param n         parameter defines the previous n-th value
     */
    public PreviousValueIndicator(Indicator<Num> indicator, int n) {
        super(indicator);
        if (n < 1) {
            throw new IllegalArgumentException("n must be positive number, but was: " + n);
        }
        this.n = n;
        this.indicator = indicator;
    }

    @Override
    protected Num calculate(int index) {
        int previousValue = Math.max(0, (index - n));
        return this.indicator.getValue(previousValue);
    }

    @Override
    public String toString() {
        final String nInfo = n == 1 ? "" : "(" + n + ")";
        return getClass().getSimpleName() + nInfo + "[" + this.indicator + "]";
    }
}