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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

/**
 * Distance From Moving Average (close - MA)/MA
 *
 * @see <a href=
 *      "https://school.stockcharts.com/doku.php?id=technical_indicators:distance_from_ma">
 *      https://school.stockcharts.com/doku.php?id=technical_indicators:distance_from_ma
 *      </a>
 */
public class DistanceFromMAPercentageIndicator extends CachedIndicator<Num> {
    private static final Set<Class<?>> supportedMovingAverages = new HashSet<>(
            Arrays.asList(EMAIndicator.class, DoubleEMAIndicator.class, TripleEMAIndicator.class, SMAIndicator.class,
                    WMAIndicator.class, ZLEMAIndicator.class, HMAIndicator.class, KAMAIndicator.class,
                    LWMAIndicator.class, AbstractEMAIndicator.class, MMAIndicator.class));
    private final Indicator<Num> movingAverage;

    /**
     * Constructor.
     * 
     * @param series        the bar series {@link BarSeries}.
     * @param movingAverage the moving average.
     */
    public DistanceFromMAPercentageIndicator(BarSeries series, Indicator<Num> movingAverage) {
        super(series);
        if (!(supportedMovingAverages.contains(movingAverage.getClass()))) {
            throw new IllegalArgumentException(
                    "Passed indicator must be a moving average based indicator. " + movingAverage.toString());
        }
        this.movingAverage = movingAverage;
    }

    @Override
    protected Num calculate(int index) {
        Bar currentBar = getBarSeries().getBar(index);
        Num closePrice = currentBar.getClosePrice();
        Num maValue = (Num) movingAverage.getValue(index);
        return (closePrice.minus(maValue)).dividedBy(maValue);
    }
}
