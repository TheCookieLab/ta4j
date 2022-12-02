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
package org.ta4j.core.num;

import java.math.RoundingMode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DoubleNumTest {

    @Test
    public void testEqualsDoubleNumWithDecimalNum() {
        final DecimalNum decimalNum = DecimalNum.valueOf(3.0);
        final DoubleNum doubleNum = DoubleNum.valueOf(3.0);

        assertFalse(doubleNum.equals(decimalNum));
    }

    @Test
    public void testZeroEquals() {
        final Num num1 = DoubleNum.valueOf(-0.0);
        final Num num2 = DoubleNum.valueOf(0.0);

        assertTrue(num1.isEqual(num2));
    }

    @Test
    public void testRoundingWithDefaultRoundingMode() {
        DoubleNum subject = DoubleNum.valueOf(3.123456);

        assertEquals(DoubleNum.valueOf(3.12346), subject.round(5));
    }

    @Test
    public void testRoundingWithCustomRoundingMode() {
        DoubleNum subject = DoubleNum.valueOf(3.123456);

        assertEquals(DoubleNum.valueOf(3.12), subject.round(2, RoundingMode.FLOOR));
    }
}
