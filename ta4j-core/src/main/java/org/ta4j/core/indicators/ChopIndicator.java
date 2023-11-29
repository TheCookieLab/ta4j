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
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.HighestValueIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.indicators.helpers.LowestValueIndicator;
import org.ta4j.core.num.Num;

/**
 * The "CHOP" index is used to indicate side-ways markets see <a href=
 * "https://www.tradingview.com/wiki/Choppiness_Index_(CHOP)">https://www.tradingview.com/wiki/Choppiness_Index_(CHOP)</a>
 * 100++ * LOG10( SUM(ATR(1), n) / ( MaxHi(n) - MinLo(n) ) ) / LOG10(n) n = User
 * defined period length. LOG10(n) = base-10 LOG of n ATR(1) = Average True
 * Range (Period of 1) SUM(ATR(1), n) = Sum of the Average True Range over past
 * n bars MaxHi(n) = The highest high over past n bars
 *
 * ++ usually this index is between 0 and 100, but could be scaled differently
 * by the 'scaleTo' arg of the constructor
 *
 * @apiNote Minimal deviations in last decimal places possible. During the
 *          calculations this indicator converts {@link Num Decimal /BigDecimal}
 *          to to {@link Double double}
 */
public class ChopIndicator extends CachedIndicator<Num> {

    private final ATRIndicator atrIndicator;
    private final int timeFrame;
    private final Num log10n;
    private final HighestValueIndicator hvi;
    private final LowestValueIndicator lvi;
    private final Num scaleUpTo;

    /**
     * Constructor.
     *
     * @param barSeries   the bar series {@link BarSeries}
     * @param ciTimeFrame time-frame often something like '14'
     * @param scaleTo     maximum value to scale this oscillator, usually '1' or
     *                    '100'
     */
    public ChopIndicator(BarSeries barSeries, int ciTimeFrame, int scaleTo) {
        super(barSeries);
        this.atrIndicator = new ATRIndicator(barSeries, 1); // ATR(1) = Average True Range (Period of 1)
        hvi = new HighestValueIndicator(new HighPriceIndicator(barSeries), ciTimeFrame);
        lvi = new LowestValueIndicator(new LowPriceIndicator(barSeries), ciTimeFrame);
        this.timeFrame = ciTimeFrame;
        this.log10n = numOf(Math.log10(ciTimeFrame));
        this.scaleUpTo = numOf(scaleTo);
    }

    @Override
    public Num calculate(int index) {
        Num summ = atrIndicator.getValue(index);
        for (int i = 1; i < timeFrame; ++i) {
            summ = summ.plus(atrIndicator.getValue(index - i));
        }
        Num a = summ.dividedBy((hvi.getValue(index).minus(lvi.getValue(index))));
        // TODO: implement Num.log10(Num)
        return scaleUpTo.multipliedBy(numOf(Math.log10(a.doubleValue()))).dividedBy(log10n);
    }
}
