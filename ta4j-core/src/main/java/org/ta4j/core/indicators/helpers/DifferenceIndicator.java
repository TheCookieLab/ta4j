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
 * Difference indicator.
 * </p>
 * I.e.: first - second
 */
public class DifferenceIndicator extends CachedIndicator<Num> {

    private Indicator<Num> first;

    private Indicator<Num> second;

    /**
     * Constructor. (first minus second)
     * 
     * @param first  the first indicator
     * @param second the second indicator
     */
    public DifferenceIndicator(Indicator<Num> first, Indicator<Num> second) {
        // TODO: check if first series is equal to second one
        super(first);
        this.first = first;
        this.second = second;
    }

    @Override
    protected Num calculate(int index) {
        return first.getValue(index).minus(second.getValue(index));
    }
}
