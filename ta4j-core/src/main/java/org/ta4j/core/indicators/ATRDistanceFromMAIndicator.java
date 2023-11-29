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
import org.ta4j.core.num.Num;

/**
 * Distance From Moving Average (signal - MA) in terms of ATR
 *
 * @see <a href=
 *      "https://school.stockcharts.com/doku.php?id=technical_indicators:distance_from_ma">
 *      https://school.stockcharts.com/doku.php?id=technical_indicators:distance_from_ma
 *      </a>
 */
public class ATRDistanceFromMAIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> movingAverage;
    private final Indicator<Num> signal;
    private final ATRIndicator atr;

    /**
     * Constructor.
     *
     * @param series        the bar series {@link BarSeries}.
     * @param signal
     * @param movingAverage the moving average.
     * @param atrBarCount
     */
    public ATRDistanceFromMAIndicator(BarSeries series, Indicator<Num> signal, Indicator<Num> movingAverage,
            int atrBarCount) {
        super(series);

        this.atr = new ATRIndicator(series, atrBarCount);
        this.movingAverage = movingAverage;
        this.signal = signal;
    }

    public ATRDistanceFromMAIndicator(BarSeries series, Indicator<Num> movingAverage, int atrBarCount) {
        this(series, new ClosePriceIndicator(series), movingAverage, atrBarCount);
    }

    @Override
    protected Num calculate(int index) {
        Num signalPrice = this.signal.getValue(index);
        Num maValue = (Num) this.movingAverage.getValue(index);
        Num atrValue = this.atr.getValue(index);

        return (signalPrice.minus(maValue)).dividedBy(atrValue);
    }
}
