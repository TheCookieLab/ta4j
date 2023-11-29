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
package org.ta4j.core.indicators.volume;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.indicators.helpers.VolumeIndicator;
import org.ta4j.core.num.Num;

/**
 * Intraday Intensity Index
 *
 * @see <a href=
 *      "https://www.investopedia.com/terms/i/intradayintensityindex.asp">https://www.investopedia.com/terms/i/intradayintensityindex.asp</a>
 */
public class IIIIndicator extends CachedIndicator<Num> {

    private final ClosePriceIndicator closePriceIndicator;
    private final HighPriceIndicator highPriceIndicator;
    private final LowPriceIndicator lowPriceIndicator;
    private final VolumeIndicator volumeIndicator;
    private final Num two;

    public IIIIndicator(BarSeries series) {
        super(series);
        this.closePriceIndicator = new ClosePriceIndicator(series);
        this.highPriceIndicator = new HighPriceIndicator(series);
        this.lowPriceIndicator = new LowPriceIndicator(series);
        this.volumeIndicator = new VolumeIndicator(series);
        this.two = numOf(2);
    }

    @Override
    protected Num calculate(int index) {
        if (index == getBarSeries().getBeginIndex()) {
            return numOf(0);
        }
        final Num doubledClosePrice = two.multipliedBy(closePriceIndicator.getValue(index));
        final Num high = highPriceIndicator.getValue(index);
        final Num low = lowPriceIndicator.getValue(index);
        final Num highMinusLow = high.minus(low);
        final Num highPlusLow = high.plus(low);

        return doubledClosePrice.minus(highPlusLow)
                .dividedBy(highMinusLow.multipliedBy(volumeIndicator.getValue(index)));
    }
}
