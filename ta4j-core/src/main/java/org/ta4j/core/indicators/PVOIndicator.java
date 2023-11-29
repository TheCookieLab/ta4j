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
import org.ta4j.core.indicators.helpers.VolumeIndicator;

/**
 * Percentage Volume Oscillator (PVO): ((12-day EMA of Volume - 26-day EMA of
 * Volume)/26-day EMA of Volume) x 100
 *
 * @see <a href=
 *      "https://school.stockcharts.com/doku.php?id=technical_indicators:percentage_volume_oscillator_pvo">
 *      https://school.stockcharts.com/doku.php?id=technical_indicators:percentage_volume_oscillator_pvo
 *      </a>
 */
public class PVOIndicator extends PPOIndicator {

    /**
     * @param series the bar series {@link BarSeries}. Will use PPO default
     *               constructor with shortBarCount "12" and longBarCount "26".
     */
    public PVOIndicator(BarSeries series) {
        super(new VolumeIndicator(series));
    }

    /**
     * @param series         the bar series {@link BarSeries}.
     * @param volumeBarCount Volume Indicator bar count. Will use PPO default
     *                       constructor with shortBarCount "12" and longBarCount
     *                       "26".
     */
    public PVOIndicator(BarSeries series, int volumeBarCount) {
        super(new VolumeIndicator(series, volumeBarCount));
    }

    /**
     * @param series        the bar series {@link BarSeries}.
     * @param shortBarCount PPO short time frame.
     * @param longBarCount  PPO long time frame.
     */
    public PVOIndicator(BarSeries series, int shortBarCount, int longBarCount) {
        super(new VolumeIndicator(series), shortBarCount, longBarCount);
    }

    /**
     * @param series         the bar series {@link BarSeries}.
     * @param volumeBarCount Volume Indicator bar count.
     * @param shortBarCount  PPO short time frame.
     * @param longBarCount   PPO long time frame.
     */
    public PVOIndicator(BarSeries series, int volumeBarCount, int shortBarCount, int longBarCount) {
        super(new VolumeIndicator(series, volumeBarCount), shortBarCount, longBarCount);
    }

}
