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
package org.ta4j.core.indicators.helpers;

import java.util.Objects;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * CumulativeSumIndicator calculates the cumulative sum of the values of another
 * indicator within a specified sum window.
 */
public class CumulativeSumIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> indicator;
    private final int sumWindow;

    /**
     * Constructor.
     *
     * @param indicator the indicator whose values are to be summed
     * @param sumWindow the number of bars to sum the indicator values
     * @throws IndexOutOfBoundsException if the sumWindow is negative
     */
    public CumulativeSumIndicator(Indicator<Num> indicator, int sumWindow) {
        super(indicator.getBarSeries());
        this.indicator = indicator;
        if (sumWindow < 0) {
            throw new IndexOutOfBoundsException("sumWindow cannot be negative");
        }
        this.sumWindow = sumWindow;
    }

    /**
     * Calculates the cumulative sum of the indicator values within the
     * specified bar count.
     *
     * @param index the index of the bar to calculate the cumulative sum up to
     * @return the cumulative sum of the indicator values within the specified
     * bar count
     */
    @Override
    protected Num calculate(int index) {
        Num sum = numOf(0);
        int startIndex = Math.max(0, index - sumWindow + 1);

        for (int i = startIndex; i <= index; i++) {
            sum = sum.plus(this.indicator.getValue(i));
        }
        return sum;
    }
}
