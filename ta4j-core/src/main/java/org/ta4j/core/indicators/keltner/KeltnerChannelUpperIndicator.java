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
package org.ta4j.core.indicators.keltner;

import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Keltner Channel (upper line) indicator
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:keltner_channels">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:keltner_channels</a>
 */
public class KeltnerChannelUpperIndicator extends CachedIndicator<Num> {

    private final ATRIndicator averageTrueRangeIndicator;

    private final KeltnerChannelMiddleIndicator keltnerMiddleIndicator;

    private final Num ratio;

    public KeltnerChannelUpperIndicator(KeltnerChannelMiddleIndicator middle, double ratio, int barCountATR) {
        this(middle, new ATRIndicator(middle.getBarSeries(), barCountATR), ratio);
    }

    public KeltnerChannelUpperIndicator(KeltnerChannelMiddleIndicator middle, ATRIndicator atr, double ratio) {
        super(middle.getBarSeries());
        this.keltnerMiddleIndicator = middle;
        this.averageTrueRangeIndicator = atr;
        this.ratio = numOf(ratio);
    }

    @Override
    protected Num calculate(int index) {
        return keltnerMiddleIndicator.getValue(index)
                .plus(ratio.multipliedBy(averageTrueRangeIndicator.getValue(index)));
    }

    public int getBarCount() {
        return keltnerMiddleIndicator.getBarCount();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + getBarCount();
    }
}
