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

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.MedianPriceIndicator;
import org.ta4j.core.num.Num;

/**
 * Awesome oscillator. (AO)
 *
 * see https://www.tradingview.com/wiki/Awesome_Oscillator_(AO)
 */
public class AwesomeOscillatorIndicator extends CachedIndicator<Num> {

    private final SMAIndicator sma5;

    private final SMAIndicator sma34;

    /**
     * Constructor.
     *
     * @param indicator    (normally {@link MedianPriceIndicator})
     * @param barCountSma1 (normally 5)
     * @param barCountSma2 (normally 34)
     */
    public AwesomeOscillatorIndicator(Indicator<Num> indicator, int barCountSma1, int barCountSma2) {
        super(indicator);
        this.sma5 = new SMAIndicator(indicator, barCountSma1);
        this.sma34 = new SMAIndicator(indicator, barCountSma2);
    }

    /**
     * Constructor.
     *
     * @param indicator (normally {@link MedianPriceIndicator})
     */
    public AwesomeOscillatorIndicator(Indicator<Num> indicator) {
        this(indicator, 5, 34);
    }

    /**
     * Constructor.
     *
     * @param series the bar series
     */
    public AwesomeOscillatorIndicator(BarSeries series) {
        this(new MedianPriceIndicator(series), 5, 34);
    }

    @Override
    protected Num calculate(int index) {
        return sma5.getValue(index).minus(sma34.getValue(index));
    }
}
