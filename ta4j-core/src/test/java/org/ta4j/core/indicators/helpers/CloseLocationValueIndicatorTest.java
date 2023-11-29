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
package org.ta4j.core.indicators.helpers;

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

public class CloseLocationValueIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries series;

    public CloseLocationValueIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        List<Bar> bars = new ArrayList<Bar>();
        // open, close, high, low
        bars.add(new MockBar(10, 18, 20, 10, numFunction));
        bars.add(new MockBar(17, 20, 21, 17, numFunction));
        bars.add(new MockBar(15, 15, 16, 14, numFunction));
        bars.add(new MockBar(15, 11, 15, 8, numFunction));
        bars.add(new MockBar(11, 12, 12, 10, numFunction));
        bars.add(new MockBar(10, 10, 10, 10, numFunction));
        bars.add(new MockBar(11, 12, 12, 10, numFunction));
        bars.add(new MockBar(11, 120, 140, 100, numFunction));
        series = new MockBarSeries(bars);
    }

    @Test
    public void getValue() {
        CloseLocationValueIndicator clv = new CloseLocationValueIndicator(series);
        assertNumEquals(0.6, clv.getValue(0));
        assertNumEquals(0.5, clv.getValue(1));
        assertNumEquals(0, clv.getValue(2));
        assertNumEquals(-1d / 7, clv.getValue(3));
        assertNumEquals(1, clv.getValue(4));
    }

    @Test
    public void returnZeroIfHighEqualsLow() {
        CloseLocationValueIndicator clv = new CloseLocationValueIndicator(series);
        assertNumEquals(NaN.NaN, clv.getValue(5));
        assertNumEquals(1, clv.getValue(6));
        assertNumEquals(0, clv.getValue(7));
    }
}
