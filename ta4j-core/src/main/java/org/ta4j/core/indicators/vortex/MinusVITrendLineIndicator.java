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
import org.ta4j.core.indicators.helpers.CumulativeSumIndicator;
import org.ta4j.core.indicators.helpers.TRIndicator;
import org.ta4j.core.num.Num;

/**
 * Vortex VI- (Minus) Trend Line Indicator.
 * <p>
 * The Vortex Indicator (VI) is composed of two lines - VI+ (Plus) and VI-
 * (Minus). The VI- trend line is calculated using the negative directional
 * movement over a specified period.
 * <p>
 * For more information:
 * https://www.investopedia.com/terms/v/vortex-indicator-vi.asp
 */
public class MinusVITrendLineIndicator extends CachedIndicator<Num> {

    private final CumulativeSumIndicator sumMinusVM;
    private final CumulativeSumIndicator sumTR;
    private final int barCount;

    /**
     * Constructor.
     *
     * @param series the BarSeries
     * @param barCount the number of bars to consider for the calculation
     */
    public MinusVITrendLineIndicator(BarSeries series, int barCount) {
        super(series);

        this.sumMinusVM = new CumulativeSumIndicator(new MinusVMIndicator(series), barCount);
        this.sumTR = new CumulativeSumIndicator(new TRIndicator(series), barCount);
        this.barCount = barCount;
    }

    @Override
    protected Num calculate(int index) {
        return this.sumMinusVM.getValue(index).dividedBy(this.sumTR.getValue(index));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + this.barCount;
    }
}
