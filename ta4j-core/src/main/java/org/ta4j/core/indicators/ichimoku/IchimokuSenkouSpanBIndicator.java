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
package org.ta4j.core.indicators.ichimoku;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

/**
 * Ichimoku clouds: Senkou Span B (Leading Span B) indicator
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud</a>
 */
public class IchimokuSenkouSpanBIndicator extends CachedIndicator<Num> {

    // ichimoku avg line indicator
    IchimokuLineIndicator lineIndicator;

    /**
     * Displacement on the chart (usually 26)
     */
    private final int offset;

    /**
     * Constructor.
     * 
     * @param series the series
     */
    public IchimokuSenkouSpanBIndicator(BarSeries series) {

        this(series, 52, 26);
    }

    /**
     * Constructor.
     * 
     * @param series   the series
     * @param barCount the time frame (usually 52)
     */
    public IchimokuSenkouSpanBIndicator(BarSeries series, int barCount) {

        this(series, barCount, 26);
    }

    /**
     * Constructor.
     * 
     * @param series   the series
     * @param barCount the time frame (usually 52)
     * @param offset   displacement on the chart
     */
    public IchimokuSenkouSpanBIndicator(BarSeries series, int barCount, int offset) {

        super(series);
        lineIndicator = new IchimokuLineIndicator(series, barCount);
        this.offset = offset;
    }

    @Override
    protected Num calculate(int index) {
        int spanIndex = index - offset + 1;
        if (spanIndex >= getBarSeries().getBeginIndex()) {
            return lineIndicator.getValue(spanIndex);
        } else {
            return NaN.NaN;
        }
    }
}
