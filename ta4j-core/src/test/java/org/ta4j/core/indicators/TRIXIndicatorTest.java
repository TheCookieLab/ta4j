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

import java.math.RoundingMode;
import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class TRIXIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private ClosePriceIndicator closePrice;

    public TRIXIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        BarSeries data = new MockBarSeries(numFunction, 0.73, 0.72, 0.86, 0.72, 0.62, 0.76, 0.84, 0.69, 0.65, 0.71,
                0.53, 0.73, 0.77, 0.67, 0.68);
        closePrice = new ClosePriceIndicator(data);
    }

    @Test
    public void givenBarCountOf5_whenGetValue_thenReturnsCorrectValue() {
        TRIXIndicator trix = new TRIXIndicator(closePrice, 5);

        assertNumEquals(0, trix.getValue(0));
        assertNumEquals(-0.0507, trix.getValue(1).round(4, RoundingMode.HALF_EVEN));
        assertNumEquals(0.6091, trix.getValue(2).round(4, RoundingMode.HALF_EVEN));

        assertNumEquals(0.3309, trix.getValue(6).round(4, RoundingMode.HALF_EVEN));
        assertNumEquals(0.1748, trix.getValue(7).round(4, RoundingMode.HALF_EVEN));
        assertNumEquals(-0.3621, trix.getValue(8).round(4, RoundingMode.HALF_EVEN));

        assertNumEquals(-0.6696, trix.getValue(12).round(4, RoundingMode.HALF_EVEN));
        assertNumEquals(-0.4784, trix.getValue(13).round(4, RoundingMode.HALF_EVEN));
        assertNumEquals(-0.4196, trix.getValue(14).round(4, RoundingMode.HALF_EVEN));
    }

    @Test
    public void givenBarCountOf10_whenGetValue_thenReturnsCorrectValue() {
        TRIXIndicator trix = new TRIXIndicator(closePrice, 10);

        assertNumEquals(0, trix.getValue(0));
        assertNumEquals(-0.0082, trix.getValue(1).round(4, RoundingMode.HALF_EVEN));
        assertNumEquals(0.0951, trix.getValue(2).round(4, RoundingMode.HALF_EVEN));

        assertNumEquals(0.0979, trix.getValue(6).round(4, RoundingMode.HALF_EVEN));
        assertNumEquals(0.0934, trix.getValue(7).round(4, RoundingMode.HALF_EVEN));
        assertNumEquals(0.0142, trix.getValue(8).round(4, RoundingMode.HALF_EVEN));

        assertNumEquals(-0.3221, trix.getValue(12).round(4, RoundingMode.HALF_EVEN));
        assertNumEquals(-0.3338, trix.getValue(13).round(4, RoundingMode.HALF_EVEN));
        assertNumEquals(-0.3483, trix.getValue(14).round(4, RoundingMode.HALF_EVEN));
    }

    @Test
    public void givenConstantClosePrices_whenGetValue_thenReturnsZero() {
        BarSeries constantData = new MockBarSeries(numFunction, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        ClosePriceIndicator constantClosePrice = new ClosePriceIndicator(constantData);

        TRIXIndicator trix = new TRIXIndicator(constantClosePrice, 5);

        for (int i = 0; i < constantData.getBarCount(); i++) {
            assertNumEquals(0, trix.getValue(i));
        }
    }

}
