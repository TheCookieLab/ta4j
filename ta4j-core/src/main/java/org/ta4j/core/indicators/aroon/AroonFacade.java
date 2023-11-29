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
package org.ta4j.core.indicators.aroon;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.numeric.NumericIndicator;

/**
 * A facade to create the two Aroon indicators. The Aroon Oscillator can also be
 * created on demand.
 *
 * <p>
 * This class creates lightweight "fluent" numeric indicators. These objects are
 * not cached, although they may be wrapped around cached objects.
 */
public class AroonFacade {
    private final NumericIndicator up;
    private final NumericIndicator down;

    /**
     * Create the Aroon facade.
     * 
     * @param bs a bar series
     * @param n  the number of periods (barCount) used for the indicators
     */
    public AroonFacade(BarSeries bs, int n) {
        this.up = NumericIndicator.of(new AroonUpIndicator(bs, n));
        this.down = NumericIndicator.of(new AroonDownIndicator(bs, n));
    }

    /**
     * A fluent AroonUp indicator.
     * 
     * @return a NumericIndicator wrapped around a cached AroonUpIndicator
     */
    public NumericIndicator up() {
        return up;
    }

    /**
     * A fluent AroonDown indicator.
     * 
     * @return a NumericIndicator wrapped around a cached AroonDownIndicator
     */
    public NumericIndicator down() {
        return down;
    }

    /**
     * A lightweight fluent AroonOscillator.
     * 
     * @return an uncached object that calculates the difference between AoonUp and
     *         AroonDown
     */
    public NumericIndicator oscillator() {
        return up.minus(down);
    }

}
