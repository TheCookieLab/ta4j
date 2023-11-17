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
package org.ta4j.core.indicators.vortex;

import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.Indicator;
import static org.ta4j.core.TestUtils.assertNumEquals;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBarSeries;
import static org.ta4j.core.num.NaN.NaN;
import org.ta4j.core.num.Num;

public class MinusVITrendLineIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private MinusVITrendLineIndicator minusVITrendLineIndicator;
    private BarSeries series;

    public MinusVITrendLineIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        List<Bar> bars = new ArrayList<>();
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 10, 11, 9, 10, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 20, 21, 19, 20, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 30, 31, 29, 30, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 40, 41, 39, 40, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 30, 31, 29, 30, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 20, 21, 19, 20, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 10, 11, 9, 10, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 5, 6, 4, 5, 1, 0, 0, numFunction));

        series = new MockBarSeries(bars);
        minusVITrendLineIndicator = new MinusVITrendLineIndicator(series, 4);
    }

    @Test
    public void givenValidSetup_whenGetValue_thenReturnsCorrectValues() {
        assertNumEquals(NaN, minusVITrendLineIndicator.getValue(0));
        assertNumEquals(NaN, minusVITrendLineIndicator.getValue(1));
        assertNumEquals(NaN, minusVITrendLineIndicator.getValue(2));
        assertNumEquals(NaN, minusVITrendLineIndicator.getValue(3));
        assertNumEquals(0.818, minusVITrendLineIndicator.getValue(4).round(3, RoundingMode.HALF_EVEN));
        assertNumEquals(0.909, minusVITrendLineIndicator.getValue(5).round(3, RoundingMode.HALF_EVEN));
        assertNumEquals(1, minusVITrendLineIndicator.getValue(6).round(3, RoundingMode.HALF_EVEN));
        assertNumEquals(1.103, minusVITrendLineIndicator.getValue(7).round(3, RoundingMode.HALF_EVEN));
    }
}
