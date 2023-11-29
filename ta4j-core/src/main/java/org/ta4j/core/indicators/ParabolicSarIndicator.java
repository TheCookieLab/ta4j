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

import static org.ta4j.core.num.NaN.NaN;

import java.util.HashMap;
import java.util.Map;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.HighestValueIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.indicators.helpers.LowestValueIndicator;
import org.ta4j.core.num.Num;

/**
 * Parabolic SAR indicator.
 *
 * @see <a href=
 *      "https://www.investopedia.com/trading/introduction-to-parabolic-sar/">
 *      https://www.investopedia.com/trading/introduction-to-parabolic-sar/</a>
 * @see <a href="https://www.investopedia.com/terms/p/parabolicindicator.asp">
 *      https://www.investopedia.com/terms/p/parabolicindicator.asp</a>
 */
public class ParabolicSarIndicator extends RecursiveCachedIndicator<Num> {

    private final LowPriceIndicator lowPriceIndicator;
    private final HighPriceIndicator highPriceIndicator;

    private final Num maxAcceleration;
    private final Num accelerationStart;
    private final Num accelerationIncrement;

    private final Map<Integer, Boolean> isUpTrendMap = new HashMap<>();
    private final Map<Integer, Num> lastExtreme = new HashMap<>();
    private final Map<Integer, Num> lastAf = new HashMap<>();

    /**
     * If series have removed bars, first actual bar won't have 0 index.
     */
    private int seriesStartIndex = getBarSeries().getBeginIndex();

    /**
     * Constructor with:
     *
     * <ul>
     * <li>{@code aF} = 0.02
     * <li>{@code maxA} = 0.2
     * <li>{@code increment} = 0.02
     * </ul>
     *
     * @param series the bar series for this indicator
     */
    public ParabolicSarIndicator(BarSeries series) {
        this(series, series.numOf(0.02), series.numOf(0.2), series.numOf(0.02));
    }

    /**
     * Constructor with {@code increment} = 0.02.
     *
     * @param series the bar series for this indicator
     * @param aF     acceleration factor
     * @param maxA   maximum acceleration
     */
    public ParabolicSarIndicator(BarSeries series, Num aF, Num maxA) {
        this(series, aF, maxA, series.numOf(0.02));
    }

    /**
     * Constructor.
     *
     * @param series    the bar series for this indicator
     * @param aF        acceleration factor (usually 0.02)
     * @param maxA      maximum acceleration (usually 0.2)
     * @param increment the increment step (usually 0.02)
     */
    public ParabolicSarIndicator(BarSeries series, Num aF, Num maxA, Num increment) {
        super(series);
        this.lowPriceIndicator = new LowPriceIndicator(series);
        this.highPriceIndicator = new HighPriceIndicator(series);
        this.maxAcceleration = maxA;
        this.accelerationStart = aF;
        this.accelerationIncrement = increment;
    }

    @Override
    protected Num calculate(int index) {
        lastExtreme.clear();
        lastAf.clear();
        isUpTrendMap.clear();

        // Caching of this indicator value calculation is essential for performance!
        //
        // clear the maps and recalculate the values for start to index
        // the internal calculations until the previous index will fill the
        // required maps for the acceleration factor, the trend direction and the
        // last extreme value.
        // Cache doesn't require more than current and previous values.
        if (index < getBarSeries().getBeginIndex()) {
            return NaN;
        }

        seriesStartIndex = getBarSeries().getRemovedBarsCount();
        if (index < seriesStartIndex) {
            index = seriesStartIndex;
        }

        for (int i = seriesStartIndex; i < index; i++) {
            calculateInternal(i);
        }

        return calculateInternal(index);
    }

    private Num calculateInternal(int index) {
        Num sar = NaN;
        boolean is_up_trend;

        if (index == seriesStartIndex) {
            lastExtreme.put(index, getBarSeries().getBar(index).getClosePrice());
            lastAf.put(index, lowPriceIndicator.numOf(0));
            isUpTrendMap.put(index, false);
            return sar; // no trend detection possible for the first value
        } else if (index == seriesStartIndex + 1) { // start trend detection
            is_up_trend = defineUpTrend(index);
            lastAf.put(index, accelerationStart);
            isUpTrendMap.put(index, is_up_trend);

            if (is_up_trend) { // up trend
                sar = new LowestValueIndicator(lowPriceIndicator, 2).getValue(index - 1); // put the lowest low value of
                // two
                lastExtreme.put(index, new HighestValueIndicator(highPriceIndicator, 2).getValue(index - 1));
            } else { // down trend
                sar = new HighestValueIndicator(highPriceIndicator, 2).getValue(index - 1); // put the highest high
                // value of
                lastExtreme.put(index, new LowestValueIndicator(lowPriceIndicator, 2).getValue(index - 1));
            }
            return sar;
        }

        Num priorSar = getValue(index - 1);

        is_up_trend = isUpTrendMap.get(index - 1);

        Num currentExtremePoint = lastExtreme.get(index - 1);
        Num cur_high = highPriceIndicator.getValue(index);
        Num cur_low = lowPriceIndicator.getValue(index);
        Num cur_af = lastAf.get(index - 1);
        sar = priorSar.plus(cur_af.multipliedBy((currentExtremePoint.minus(priorSar))));

        if (is_up_trend) { // if up trend
            if (cur_low.isLessThan(sar)) { // check if sar touches the low price
                sar = currentExtremePoint;

                lastAf.put(index, accelerationStart);
                lastExtreme.put(index, cur_low);
                is_up_trend = false;

            } else { // up trend is going on
                if (cur_high.isGreaterThan(currentExtremePoint)) {
                    currentExtremePoint = cur_high;
                    cur_af = incrementAcceleration(index);
                }
                lastExtreme.put(index, currentExtremePoint);
                lastAf.put(index, cur_af);
            }
        } else { // downtrend
            if (cur_high.isGreaterThanOrEqual(sar)) { // check if switch to up trend
                sar = currentExtremePoint;

                lastAf.put(index, accelerationStart);
                lastExtreme.put(index, cur_high);
                is_up_trend = true;

            } else { // down trend io going on
                if (cur_low.isLessThan(currentExtremePoint)) {
                    currentExtremePoint = cur_low;
                    cur_af = incrementAcceleration(index);
                }
                lastExtreme.put(index, currentExtremePoint);
                lastAf.put(index, cur_af);

            }
        }

        if (is_up_trend) {
            Num lowestPriceOfTwoPreviousBars = new LowestValueIndicator(lowPriceIndicator, 2).getValue(index - 1);
            if (sar.isGreaterThan(lowestPriceOfTwoPreviousBars)) {
                sar = lowestPriceOfTwoPreviousBars;
            }
        } else {
            Num highestPriceOfTwoPreviousBars = new HighestValueIndicator(highPriceIndicator, 2).getValue(index - 1);
            if (sar.isLessThan(highestPriceOfTwoPreviousBars)) {
                sar = highestPriceOfTwoPreviousBars;
            }
        }
        isUpTrendMap.put(index, is_up_trend);
        return sar;
    }

    public int getUnstableBars() {
        return 0;
    }

    private boolean defineUpTrend(final int barIndex) {
        if (barIndex - 1 < seriesStartIndex) {
            return false;
        } else {
            return getBarSeries().getBar(barIndex - 1)
                    .getClosePrice()
                    .isLessThan(getBarSeries().getBar(barIndex).getClosePrice());
        }
    }

    /**
     * Increments the acceleration factor.
     */
    private Num incrementAcceleration(int index) {
        Num cur_af = lastAf.get(index - 1);
        cur_af = cur_af.plus(accelerationIncrement);
        if (cur_af.isGreaterThan(maxAcceleration)) {
            cur_af = maxAcceleration;
        }
        return cur_af;
    }
}