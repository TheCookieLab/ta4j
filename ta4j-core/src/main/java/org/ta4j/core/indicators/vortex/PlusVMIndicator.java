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
package org.ta4j.core.indicators.vortex;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.HighPriceIndicator;
import org.ta4j.core.indicators.helpers.LowPriceIndicator;
import org.ta4j.core.indicators.helpers.PreviousValueIndicator;
import static org.ta4j.core.num.NaN.NaN;
import org.ta4j.core.num.Num;

/**
 * Vortex VM+ (Plus) Indicator.
 * <p>
 * The Vortex Indicator (VI) is composed of two lines - VI+ (Plus) and VI-
 * (Minus). The VM+ is calculated using the previous low price and the current
 * high price.
 * <p>
 * For more information:
 * https://www.investopedia.com/terms/v/vortex-indicator-vi.asp
 */
public class PlusVMIndicator extends CachedIndicator<Num> {

    private final PreviousValueIndicator previousLowPrice;
    private final HighPriceIndicator highPrice;

    /**
     * Constructor.
     *
     * @param series the BarSeries
     */
    public PlusVMIndicator(BarSeries series) {
        super(series);

        this.previousLowPrice = new PreviousValueIndicator(new LowPriceIndicator(series));
        this.highPrice = new HighPriceIndicator(series);
    }

    @Override
    protected Num calculate(int index) {
        if (index < 1) {
            return NaN;
        }

        return highPrice.getValue(index).minus(previousLowPrice.getValue(index)).abs();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
