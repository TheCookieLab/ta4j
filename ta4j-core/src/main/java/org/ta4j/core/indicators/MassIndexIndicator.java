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
import org.ta4j.core.indicators.helpers.CombineIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.num.Num;

/**
 * Mass index indicator.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:mass_index">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:mass_index</a>
 */
public class MassIndexIndicator extends CachedIndicator<Num> {

    private final EMAIndicator singleEma;
    private final EMAIndicator doubleEma;
    private final int barCount;

    /**
     * Constructor.
     *
     * @param series      the bar series
     * @param emaBarCount the time frame for EMAs (usually 9)
     * @param barCount    the time frame
     */
    public MassIndexIndicator(BarSeries series, int emaBarCount, int barCount) {
        super(series);
        Indicator<Num> highLowDifferential = CombineIndicator.minus(new HighPriceIndicator(series),
                new LowPriceIndicator(series));
        singleEma = new EMAIndicator(highLowDifferential, emaBarCount);
        doubleEma = new EMAIndicator(singleEma, emaBarCount); // Not the same formula as DoubleEMAIndicator
        this.barCount = barCount;
    }

    @Override
    protected Num calculate(int index) {
        final int startIndex = Math.max(0, index - barCount + 1);
        Num massIndex = numOf(0);
        for (int i = startIndex; i <= index; i++) {
            Num emaRatio = singleEma.getValue(i).dividedBy(doubleEma.getValue(i));
            massIndex = massIndex.plus(emaRatio);
        }
        return massIndex;
    }
}
