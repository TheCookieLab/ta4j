/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
 * The Vortex Indicator (VI) is composed of two lines - VI+ (Plus) and VI- (Minus). The VM+ is calculated
 * using the previous low price and the current high price.
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