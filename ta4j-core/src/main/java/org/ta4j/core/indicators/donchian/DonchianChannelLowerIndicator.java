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
package org.ta4j.core.indicators.donchian;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.indicators.helpers.LowestValueIndicator;
import org.ta4j.core.num.Num;

/***
 * https://www.investopedia.com/terms/d/donchianchannels.asp
 */
public class DonchianChannelLowerIndicator extends CachedIndicator<Num> {

    private final LowestValueIndicator lowestPrice;
    private final LowPriceIndicator lowPrice;
    private final int barCount;

    public DonchianChannelLowerIndicator(BarSeries series, int barCount) {
        super(series);

        this.barCount = barCount;
        this.lowPrice = new LowPriceIndicator(series);
        this.lowestPrice = new LowestValueIndicator(this.lowPrice, barCount);
    }

    @Override
    protected Num calculate(int index) {
        return this.lowestPrice.getValue(index);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "timePeriod: " + barCount;
    }
}
