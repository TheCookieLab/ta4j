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
package org.ta4j.core.indicators.helpers;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.num.DecimalNum;

public class MaxCompareIndicatorTest {

    @Test
    public void calculate() {
        BarSeries series = new BaseBarSeries();

        FixedDecimalIndicator fd1 = new FixedDecimalIndicator(series, 1, 1, 3, 4, 3, 6, 7, 6, 9, 1);
        FixedDecimalIndicator fd2 = new FixedDecimalIndicator(series, 0, 2, 3, 3, 5, 2, 6, 8, 4, 10);

        MaxCompareIndicator maxCompare = new MaxCompareIndicator(series, fd1, fd2);

        assertEquals(DecimalNum.valueOf(1.0), maxCompare.calculate(0));
        assertEquals(DecimalNum.valueOf(2.0), maxCompare.calculate(1));
        assertEquals(DecimalNum.valueOf(3.0), maxCompare.calculate(2));
        assertEquals(DecimalNum.valueOf(4.0), maxCompare.calculate(3));
        assertEquals(DecimalNum.valueOf(5.0), maxCompare.calculate(4));
        assertEquals(DecimalNum.valueOf(6.0), maxCompare.calculate(5));
        assertEquals(DecimalNum.valueOf(7.0), maxCompare.calculate(6));
        assertEquals(DecimalNum.valueOf(8.0), maxCompare.calculate(7));
        assertEquals(DecimalNum.valueOf(9.0), maxCompare.calculate(8));
        assertEquals(DecimalNum.valueOf(10.0), maxCompare.calculate(9));
    }
}
