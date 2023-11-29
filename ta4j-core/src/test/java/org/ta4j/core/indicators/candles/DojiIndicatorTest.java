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
package org.ta4j.core.indicators.candles;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import org.ta4j.core.num.Num;

public class DojiIndicatorTest extends AbstractIndicatorTest<Indicator<Boolean>, Num> {

    private BarSeries series;

    public DojiIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        List<Bar> bars = new ArrayList<Bar>();
        // open, close, high, low
        bars.add(new MockBar(19, 19, 22, 16, numFunction));
        bars.add(new MockBar(10, 18, 20, 10, numFunction));
        bars.add(new MockBar(17, 20, 21, 17, numFunction));
        bars.add(new MockBar(15, 15.1, 16, 14, numFunction));
        bars.add(new MockBar(15, 11, 15, 8, numFunction));
        bars.add(new MockBar(11, 12, 12, 10, numFunction));
        series = new MockBarSeries(bars);
    }

    @Test
    public void getValueAtIndex0() {
        DojiIndicator doji = new DojiIndicator(new MockBarSeries(numFunction, 0d), 10, 0.03);
        assertTrue(doji.getValue(0));

        doji = new DojiIndicator(new MockBarSeries(numFunction, 1d), 10, 0.03);
        assertFalse(doji.getValue(0));
    }

    @Test
    public void getValue() {
        DojiIndicator doji = new DojiIndicator(series, 3, 0.1);
        assertTrue(doji.getValue(0));
        assertFalse(doji.getValue(1));
        assertFalse(doji.getValue(2));
        assertTrue(doji.getValue(3));
        assertFalse(doji.getValue(4));
        assertFalse(doji.getValue(5));
    }
}
