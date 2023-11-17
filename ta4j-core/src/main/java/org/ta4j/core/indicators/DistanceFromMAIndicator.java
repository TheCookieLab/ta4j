/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2022 Ta4j Organization & respective
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
package org.ta4j.core.indicators;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

/**
 * Distance From Moving Average (signal - MA)
 *
 * @see <a href=
 *      "https://school.stockcharts.com/doku.php?id=technical_indicators:distance_from_ma">
 *      https://school.stockcharts.com/doku.php?id=technical_indicators:distance_from_ma
 *      </a>
 */
public class DistanceFromMAIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> movingAverage;
    private final Indicator<Num> signal;

    /**
     * Constructor.
     *
     * @param series        the bar series {@link BarSeries}.
     * @param signal
     * @param movingAverage the moving average.
     */
    public DistanceFromMAIndicator(BarSeries series, Indicator<Num> signal, Indicator<Num> movingAverage) {
        super(series);

        this.movingAverage = movingAverage;
        this.signal = signal;
    }

    public DistanceFromMAIndicator(BarSeries series, Indicator<Num> movingAverage) {
        this(series, new ClosePriceIndicator(series), movingAverage);
    }

    @Override
    protected Num calculate(int index) {
        Num signalPrice = this.signal.getValue(index);
        Num maValue = (Num) movingAverage.getValue(index);
        return signalPrice.minus(maValue);
    }
}
