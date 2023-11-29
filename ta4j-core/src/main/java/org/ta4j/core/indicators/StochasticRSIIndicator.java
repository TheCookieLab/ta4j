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
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.HighestValueIndicator;
import org.ta4j.core.indicators.helpers.LowestValueIndicator;
import org.ta4j.core.num.Num;

/**
 * The Stochastic RSI Indicator.
 * 
 * Stoch RSI = (RSI - MinimumRSIn) / (MaximumRSIn - MinimumRSIn)
 */
public class StochasticRSIIndicator extends CachedIndicator<Num> {

    private final RSIIndicator rsi;
    private final LowestValueIndicator minRsi;
    private final HighestValueIndicator maxRsi;

    /**
     * Constructor. In most cases, this should be used to avoid confusion over what
     * Indicator parameters should be used.
     * 
     * @param series   the series
     * @param barCount the time frame
     */
    public StochasticRSIIndicator(BarSeries series, int barCount) {
        this(new ClosePriceIndicator(series), barCount);
    }

    /**
     * Constructor.
     * 
     * @param indicator the Indicator, in practice is always a ClosePriceIndicator.
     * @param barCount  the time frame
     */
    public StochasticRSIIndicator(Indicator<Num> indicator, int barCount) {
        this(new RSIIndicator(indicator, barCount), barCount);
    }

    /**
     * Constructor.
     * 
     * @param rsiIndicator the rsi indicator
     * @param barCount     the time frame
     */
    public StochasticRSIIndicator(RSIIndicator rsiIndicator, int barCount) {
        super(rsiIndicator);
        this.rsi = rsiIndicator;
        minRsi = new LowestValueIndicator(rsiIndicator, barCount);
        maxRsi = new HighestValueIndicator(rsiIndicator, barCount);
    }

    @Override
    protected Num calculate(int index) {
        Num minRsiValue = minRsi.getValue(index);
        return rsi.getValue(index).minus(minRsiValue).dividedBy(maxRsi.getValue(index).minus(minRsiValue));
    }

}
