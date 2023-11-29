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
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Aroon Oscillator.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon_oscillator">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:aroon_oscillator</a>
 */
public class AroonOscillatorIndicator extends CachedIndicator<Num> {

    private final AroonDownIndicator aroonDownIndicator;
    private final AroonUpIndicator aroonUpIndicator;
    private final int barCount;

    public AroonOscillatorIndicator(BarSeries series, int barCount) {
        super(series);
        this.barCount = barCount;
        this.aroonDownIndicator = new AroonDownIndicator(series, barCount);
        this.aroonUpIndicator = new AroonUpIndicator(series, barCount);
    }

    @Override
    protected Num calculate(int index) {
        return aroonUpIndicator.getValue(index).minus(aroonDownIndicator.getValue(index));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }

    public AroonDownIndicator getAroonDownIndicator() {
        return aroonDownIndicator;
    }

    public AroonUpIndicator getAroonUpIndicator() {
        return aroonUpIndicator;
    }
}
