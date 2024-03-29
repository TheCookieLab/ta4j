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
package org.ta4j.core.indicators;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.ta4j.core.TestUtils.assertNumEquals;

public class ZigZagIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries data;

    public ZigZagIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        data = new MockBarSeries(numFunction, 0.4, 0.5, 0.6, 0.7, 0.55, 0.65, 0.45);
    }

    @Test
    public void testCalculate_BelowThreshold_ReturnNaN() {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(data);
        ZigZagIndicator zigZagIndicator = new ZigZagIndicator(closePrice, numFunction.apply(0.5));
        assertNumEquals(0.4, zigZagIndicator.getValue(0));
        assertEquals(NaN.NaN, zigZagIndicator.getValue(1));
    }

    @Test
    public void testCalculate_AboveThreshold_ReturnValue() {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(data);
        ZigZagIndicator zigZagIndicator = new ZigZagIndicator(closePrice, numFunction.apply(0.2));
        assertNumEquals(0.4, zigZagIndicator.getValue(0));
        assertNumEquals(0.5, zigZagIndicator.getValue(1));
    }

//    @Test
//    public void testCalculate_MultipleValues() {
//        ClosePriceIndicator closePrice = new ClosePriceIndicator(data);
//        ZigZagIndicator zigZagIndicator = new ZigZagIndicator(closePrice, numFunction.apply(0.25));
//        assertNumEquals(0.4, zigZagIndicator.getValue(0));
//        assertNumEquals(0.5, zigZagIndicator.getValue(1));
//        assertNumEquals(NaN.NaN, zigZagIndicator.getValue(2));
//        assertNumEquals(NaN.NaN, zigZagIndicator.getValue(3));
//        assertEquals(NaN.NaN, zigZagIndicator.getValue(4));
//        assertEquals(NaN.NaN, zigZagIndicator.getValue(5));
//        assertEquals(NaN.NaN, zigZagIndicator.getValue(6));
//    }
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullThreshold() {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(data);
        ZigZagIndicator zigZag = new ZigZagIndicator(closePrice, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeThreshold() {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(data);
        ZigZagIndicator zigZag = new ZigZagIndicator(closePrice, numFunction.apply(-0.2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_ZeroThreshold() {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(data);
        ZigZagIndicator zigZag = new ZigZagIndicator(closePrice, numFunction.apply(0));
    }
}
