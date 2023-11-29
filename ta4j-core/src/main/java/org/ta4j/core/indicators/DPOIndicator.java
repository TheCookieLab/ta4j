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
import org.ta4j.core.indicators.helpers.CombineIndicator;
import org.ta4j.core.indicators.helpers.PreviousValueIndicator;
import org.ta4j.core.num.Num;

/**
 * The Detrended Price Oscillator (DPO) indicator.
 *
 * The Detrended Price Oscillator (DPO) is an indicator designed to remove trend
 * from price and make it easier to identify cycles. DPO does not extend to the
 * last date because it is based on a displaced moving average. However,
 * alignment with the most recent is not an issue because DPO is not a momentum
 * oscillator. Instead, DPO is used to identify cycles highs/lows and estimate
 * cycle length.
 *
 * In short, DPO(20) equals price 11 days ago less the 20-day SMA.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:detrended_price_osci">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:detrended_price_osci</a>
 */
public class DPOIndicator extends CachedIndicator<Num> {

    private final CombineIndicator indicatorMinusPreviousSMAIndicator;
    private final String name;

    /**
     * Constructor.
     *
     * @param series   the series
     * @param barCount the time frame
     */
    public DPOIndicator(BarSeries series, int barCount) {
        this(new ClosePriceIndicator(series), barCount);
    }

    /**
     * Constructor.
     *
     * @param price    the price
     * @param barCount the time frame
     */
    public DPOIndicator(Indicator<Num> price, int barCount) {
        super(price);
        int timeFrame = barCount / 2 + 1;
        final SMAIndicator simpleMovingAverage = new SMAIndicator(price, barCount);
        final PreviousValueIndicator previousSimpleMovingAverage = new PreviousValueIndicator(simpleMovingAverage,
                timeFrame);

        this.indicatorMinusPreviousSMAIndicator = CombineIndicator.minus(price, previousSimpleMovingAverage);
        this.name = String.format("%s barCount: %s", getClass().getSimpleName(), barCount);
    }

    @Override
    protected Num calculate(int index) {
        return indicatorMinusPreviousSMAIndicator.getValue(index);
    }

    @Override
    public String toString() {
        return name;
    }
}
