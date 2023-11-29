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

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class BearishCandleStreakIndicatorTest extends AbstractIndicatorTest<BarSeries, Num> {

    private BarSeries series;

    public BearishCandleStreakIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        List<Bar> bars = new ArrayList<>();
        // open, close, high, low
        bars.add(new MockBar(10, 9, 10, 9, numFunction));
        bars.add(new MockBar(10, 9, 10, 9, numFunction));
        bars.add(new MockBar(10, 9, 10, 9, numFunction));
        bars.add(new MockBar(10, 9, 10, 9, numFunction));
        bars.add(new MockBar(10, 9, 10, 9, numFunction));
        series = new MockBarSeries(bars);
    }

    @Test
    public void getValue() {
        BearishCandleStreakIndicator subject = new BearishCandleStreakIndicator(series);
        assertNumEquals(5, subject.getValue(4));
        assertNumEquals(4, subject.getValue(3));
        assertNumEquals(3, subject.getValue(2));
        assertNumEquals(2, subject.getValue(1));
        assertNumEquals(1, subject.getValue(0));
    }
}
