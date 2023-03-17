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

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import static org.ta4j.core.TestUtils.assertNumEquals;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class ConsecutiveBooleanStreakIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries barSeries;

    public ConsecutiveBooleanStreakIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        barSeries = new MockBarSeries(numFunction, 1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d, 10d);
    }

    @Test
    public void givenTrueCheckValue_whenGetValue_thenReturnsCorrectStreak() {
        FixedBooleanIndicator booleanValues = new FixedBooleanIndicator(barSeries, true, true, true, false, true, true, false, false, true, false);

        ConsecutiveBooleanStreakIndicator subject = new ConsecutiveBooleanStreakIndicator(booleanValues, true);

        assertNumEquals(3, subject.getValue(2));
        assertNumEquals(0, subject.getValue(3));
        assertNumEquals(2, subject.getValue(5));
        assertNumEquals(0, subject.getValue(9));
    }

    @Test
    public void givenFalseCheckValue_whenGetValue_thenReturnsCorrectStreak() {
        FixedBooleanIndicator booleanValues = new FixedBooleanIndicator(barSeries, true, true, true, false, true, true, false, false, true, false);

        ConsecutiveBooleanStreakIndicator subject = new ConsecutiveBooleanStreakIndicator(booleanValues, false);

        assertNumEquals(0, subject.getValue(2));
        assertNumEquals(1, subject.getValue(3));
        assertNumEquals(0, subject.getValue(5));
        assertNumEquals(2, subject.getValue(7));
    }

    @Test
    public void testEdgeCases() {
        FixedBooleanIndicator booleanValues = new FixedBooleanIndicator(barSeries, true, false, false, false, false, false, false, false, false, false);

        ConsecutiveBooleanStreakIndicator subjectTrue = new ConsecutiveBooleanStreakIndicator(booleanValues, true);
        ConsecutiveBooleanStreakIndicator subjectFalse = new ConsecutiveBooleanStreakIndicator(booleanValues, false);

        assertNumEquals(1, subjectTrue.getValue(0));
        assertNumEquals(9, subjectFalse.getValue(9));

        booleanValues = new FixedBooleanIndicator(barSeries, true, true, true, true, true, true, true, true, true, true);
        subjectTrue = new ConsecutiveBooleanStreakIndicator(booleanValues, true);
        subjectFalse = new ConsecutiveBooleanStreakIndicator(booleanValues, false);

        assertNumEquals(10, subjectTrue.getValue(9));
        assertNumEquals(0, subjectFalse.getValue(9));
    }
}
