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

import java.util.function.Function;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import static org.ta4j.core.TestUtils.assertNumEquals;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class CumulativeSumIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private ClosePriceIndicator closePrice;
    private BarSeries barSeries;

    public CumulativeSumIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        barSeries = new MockBarSeries(numFunction, 1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d, 10d);
        closePrice = new ClosePriceIndicator(barSeries);
    }

    @Test
    public void givenValidIndicatorAndSumWindow_whenGetValue_thenReturnsCorrectSums() {
        CumulativeSumIndicator subject = new CumulativeSumIndicator(closePrice, 5);

        assertNumEquals(40, subject.getValue(9));
        assertNumEquals(35, subject.getValue(8));
        assertNumEquals(30, subject.getValue(7));
        assertNumEquals(25, subject.getValue(6));
        assertNumEquals(20, subject.getValue(5));
        assertNumEquals(15, subject.getValue(4));
        assertNumEquals(10, subject.getValue(3));
        assertNumEquals(6, subject.getValue(2));
        assertNumEquals(3, subject.getValue(1));
        assertNumEquals(1, subject.getValue(0));
    }

    @Test
    public void givenSumWindowIsZero_whenGetValue_thenReturnsZero() {
        CumulativeSumIndicator subject = new CumulativeSumIndicator(closePrice, 0);

        assertNumEquals(0, subject.getValue(9));
        assertNumEquals(0, subject.getValue(8));
        assertNumEquals(0, subject.getValue(7));
        assertNumEquals(0, subject.getValue(6));
        assertNumEquals(0, subject.getValue(5));
        assertNumEquals(0, subject.getValue(4));
        assertNumEquals(0, subject.getValue(3));
        assertNumEquals(0, subject.getValue(2));
        assertNumEquals(0, subject.getValue(1));
        assertNumEquals(0, subject.getValue(0));
    }

    @Test
    public void givenSumWindowIsOne_whenGetValue_thenReturnsValueEqualToIndicatorGetValue() {
        CumulativeSumIndicator subject = new CumulativeSumIndicator(closePrice, 1);

        assertNumEquals(closePrice.getValue(9), subject.getValue(9));
        assertNumEquals(closePrice.getValue(8), subject.getValue(8));
        assertNumEquals(closePrice.getValue(7), subject.getValue(7));
        assertNumEquals(closePrice.getValue(6), subject.getValue(6));
        assertNumEquals(closePrice.getValue(5), subject.getValue(5));
        assertNumEquals(closePrice.getValue(4), subject.getValue(4));
        assertNumEquals(closePrice.getValue(3), subject.getValue(3));
        assertNumEquals(closePrice.getValue(2), subject.getValue(2));
        assertNumEquals(closePrice.getValue(1), subject.getValue(1));
        assertNumEquals(closePrice.getValue(0), subject.getValue(0));
    }

    @Test
    public void givenSumWindowExceedsBarSeriesBarCount_whenGetValue_thenReturnsCorrectSums() {
        CumulativeSumIndicator subject = new CumulativeSumIndicator(closePrice, 20);

        assertNumEquals(55, subject.getValue(9));
        assertNumEquals(45, subject.getValue(8));
        assertNumEquals(36, subject.getValue(7));
        assertNumEquals(28, subject.getValue(6));
        assertNumEquals(21, subject.getValue(5));
        assertNumEquals(15, subject.getValue(4));
        assertNumEquals(10, subject.getValue(3));
        assertNumEquals(6, subject.getValue(2));
        assertNumEquals(3, subject.getValue(1));
        assertNumEquals(1, subject.getValue(0));
    }

    @Test
    public void givenSumWindowEqualsBarSeriesSize_whenGetValue_thenReturnsCorrectSums() {
        CumulativeSumIndicator subject = new CumulativeSumIndicator(closePrice, barSeries.getBarCount());

        assertNumEquals(55, subject.getValue(9));
        assertNumEquals(45, subject.getValue(8));
        assertNumEquals(36, subject.getValue(7));
        assertNumEquals(28, subject.getValue(6));
        assertNumEquals(21, subject.getValue(5));
        assertNumEquals(15, subject.getValue(4));
        assertNumEquals(10, subject.getValue(3));
        assertNumEquals(6, subject.getValue(2));
        assertNumEquals(3, subject.getValue(1));
        assertNumEquals(1, subject.getValue(0));
    }

    @Test
    public void givenIndicatorIsNull_whenInstantiated_thenThrowsException() {
        Exception exception = Assert.assertThrows(NullPointerException.class, () -> {
            CumulativeSumIndicator subject = new CumulativeSumIndicator(null, 20);
        });

        assertEquals("Cannot invoke \"org.ta4j.core.Indicator.getBarSeries()\" because \"indicator\" is null", exception.getMessage());
    }

    @Test
    public void givenSumWindowIsNull_whenInstantiated_thenThrowsException() {

        Exception exception = Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            CumulativeSumIndicator subject = new CumulativeSumIndicator(closePrice, -5);
        });

        assertEquals("sumWindow cannot be negative", exception.getMessage());
    }
}
