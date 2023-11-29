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

/**
 * Ichimoku clouds: Tenkan-sen (Conversion line) indicator
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ichimoku_cloud</a>
 */
public class IchimokuTenkanSenIndicator extends IchimokuLineIndicator {

    /**
     * Constructor.
     * 
     * @param series the series
     */
    public IchimokuTenkanSenIndicator(BarSeries series) {
        this(series, 9);
    }

    /**
     * Constructor.
     * 
     * @param series   the series
     * @param barCount the time frame (usually 9)
     */
    public IchimokuTenkanSenIndicator(BarSeries series, int barCount) {
        super(series, barCount);
    }
}
