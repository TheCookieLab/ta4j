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

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.DistinctConsecutiveValuesOfIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class DistinctConsecutiveValuesOfIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private final double[] closePriceValues = new double[]{100, 101, 101, 101, 102, 102, 102, 103, 104, 104, 105, 105, 105, 104, 103, 103, 103, 102, 102, 101, 100, 100};

    private ClosePriceIndicator closePrice;

    public DistinctConsecutiveValuesOfIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        closePrice = new ClosePriceIndicator(new MockBarSeries(numFunction, closePriceValues));
    }

    @Test
    public void getValue() {
        DistinctConsecutiveValuesOfIndicator subject = new DistinctConsecutiveValuesOfIndicator(closePrice);
                
        assertNumEquals(100, subject.getValue(0));
        assertNumEquals(101, subject.getValue(1));
        assertNumEquals(102, subject.getValue(2));
        assertNumEquals(103, subject.getValue(3));
        assertNumEquals(104, subject.getValue(4));
        assertNumEquals(105, subject.getValue(5));
        assertNumEquals(104, subject.getValue(6));
        assertNumEquals(103, subject.getValue(7));
        assertNumEquals(102, subject.getValue(8));
        assertNumEquals(101, subject.getValue(9));
        assertNumEquals(100, subject.getValue(10));
        
        assertNumEquals(100, subject.getValue(11));
        
    }
}
