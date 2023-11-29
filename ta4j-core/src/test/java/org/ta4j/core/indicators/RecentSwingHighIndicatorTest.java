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

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

import java.util.function.Function;

import org.ta4j.core.Bar;
import static org.ta4j.core.TestUtils.assertNumEquals;
import org.ta4j.core.mocks.MockBar;

public class RecentSwingHighIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    BarSeries series;

    public RecentSwingHighIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        List<Bar> bars = new ArrayList<>();
        bars.add(new MockBar(10, 10, 10, 10, numFunction));
        bars.add(new MockBar(11, 11, 11, 11, numFunction));
        bars.add(new MockBar(12, 12, 12, 12, numFunction));
        bars.add(new MockBar(11, 11, 11, 11, numFunction));
        bars.add(new MockBar(10, 10, 10, 10, numFunction));
        bars.add(new MockBar(13, 13, 13, 13, numFunction));
        bars.add(new MockBar(10, 10, 10, 10, numFunction));
        this.series = new MockBarSeries(bars);
    }

    @Test
    public void testCalculate_BelowSurroundingBars_ReturnsNaN() {
        RecentSwingHighIndicator swingHighIndicator = new RecentSwingHighIndicator(series, 2);

        assertNumEquals(NaN.NaN, swingHighIndicator.getValue(0));
        assertNumEquals(NaN.NaN, swingHighIndicator.getValue(1));
        assertNumEquals(NaN.NaN, swingHighIndicator.getValue(2));
    }

    @Test
    public void testCalculate_AboveSurroundingBars_ReturnsValue() {
        RecentSwingHighIndicator swingHighIndicator = new RecentSwingHighIndicator(series, 2);

        assertNumEquals(12, swingHighIndicator.getValue(3));
        assertNumEquals(12, swingHighIndicator.getValue(4));
        assertNumEquals(12, swingHighIndicator.getValue(5));
    }

    @Test
    public void testCalculate_NotEnoughSurroundingBarsAfter_ReturnsPreviousValue() {
        RecentSwingHighIndicator swingHighIndicator = new RecentSwingHighIndicator(series, 2);

        assertNumEquals(12, swingHighIndicator.getValue(6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_SurroundingBarsZero() {
        RecentSwingHighIndicator swingHighIndicator = new RecentSwingHighIndicator(series, 0);
    }

}
