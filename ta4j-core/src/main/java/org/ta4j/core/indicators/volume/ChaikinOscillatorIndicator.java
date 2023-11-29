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
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.num.Num;

/**
 * Chaikin Oscillator.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_oscillator">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_oscillator</a>
 */
public class ChaikinOscillatorIndicator extends CachedIndicator<Num> {

    private final EMAIndicator emaShort;
    private final EMAIndicator emaLong;

    /**
     * Constructor.
     *
     * @param series        the {@link BarSeries}
     * @param shortBarCount (usually 3)
     * @param longBarCount  (usually 10)
     */
    public ChaikinOscillatorIndicator(BarSeries series, int shortBarCount, int longBarCount) {
        super(series);
        this.emaShort = new EMAIndicator(new AccumulationDistributionIndicator(series), shortBarCount);
        this.emaLong = new EMAIndicator(new AccumulationDistributionIndicator(series), longBarCount);
    }

    /**
     * Constructor.
     *
     * @param series the {@link BarSeries}
     */
    public ChaikinOscillatorIndicator(BarSeries series) {
        this(series, 3, 10);
    }

    @Override
    protected Num calculate(int index) {
        return emaShort.getValue(index).minus(emaLong.getValue(index));
    }
}
