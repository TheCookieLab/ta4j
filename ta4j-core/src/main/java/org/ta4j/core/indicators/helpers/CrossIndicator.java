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
 * Cross indicator.
 *
 * Boolean indicator which monitors two-indicators crossings.
 */
public class CrossIndicator extends CachedIndicator<Boolean> {

    /** Upper indicator */
    private final Indicator<Num> up;
    /** Lower indicator */
    private final Indicator<Num> low;

    /**
     * Constructor.
     * 
     * @param up  the upper indicator
     * @param low the lower indicator
     */
    public CrossIndicator(Indicator<Num> up, Indicator<Num> low) {
        // TODO: check if up series is equal to low series
        super(up);
        this.up = up;
        this.low = low;
    }

    @Override
    protected Boolean calculate(int index) {

        int i = index;
        if (i == 0 || up.getValue(i).isGreaterThanOrEqual(low.getValue(i))) {
            return false;
        }

        i--;
        if (up.getValue(i).isGreaterThan(low.getValue(i))) {
            return true;
        }
        while (i > 0 && up.getValue(i).isEqual(low.getValue(i))) {
            i--;
        }
        return (i != 0) && (up.getValue(i).isGreaterThan(low.getValue(i)));
    }

    /**
     * @return the initial lower indicator
     */
    public Indicator<Num> getLow() {
        return low;
    }

    /**
     * @return the initial upper indicator
     */
    public Indicator<Num> getUp() {
        return up;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + low + " " + up;
    }
}
