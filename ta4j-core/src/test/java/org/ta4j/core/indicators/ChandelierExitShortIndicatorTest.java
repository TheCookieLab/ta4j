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

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.num.Num;

public class ChandelierExitShortIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries data;

    public ChandelierExitShortIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        List<Bar> bars = new ArrayList<Bar>();
        // open, close, high, low
        bars.add(new MockBar(44.98, 45.05, 45.17, 44.96, numFunction));
        bars.add(new MockBar(45.05, 45.10, 45.15, 44.99, numFunction));
        bars.add(new MockBar(45.11, 45.19, 45.32, 45.11, numFunction));
        bars.add(new MockBar(45.19, 45.14, 45.25, 45.04, numFunction));
        bars.add(new MockBar(45.12, 45.15, 45.20, 45.10, numFunction));
        bars.add(new MockBar(45.15, 45.14, 45.20, 45.10, numFunction));
        bars.add(new MockBar(45.13, 45.10, 45.16, 45.07, numFunction));
        bars.add(new MockBar(45.12, 45.15, 45.22, 45.10, numFunction));
        bars.add(new MockBar(45.15, 45.22, 45.27, 45.14, numFunction));
        bars.add(new MockBar(45.24, 45.43, 45.45, 45.20, numFunction));
        bars.add(new MockBar(45.43, 45.44, 45.50, 45.39, numFunction));
        bars.add(new MockBar(45.43, 45.55, 45.60, 45.35, numFunction));
        bars.add(new MockBar(45.58, 45.55, 45.61, 45.39, numFunction));
        bars.add(new MockBar(45.45, 45.01, 45.55, 44.80, numFunction));
        bars.add(new MockBar(45.03, 44.23, 45.04, 44.17, numFunction));

        data = new BaseBarSeries(bars);
    }

    @Test
    public void massIndexUsing3And8BarCounts() {
        ChandelierExitShortIndicator ces = new ChandelierExitShortIndicator(data, 5, 2);

        assertNumEquals(45.3246, ces.getValue(5));
        assertNumEquals(45.3437, ces.getValue(6));
        assertNumEquals(45.3309, ces.getValue(7));
        assertNumEquals(45.3547, ces.getValue(8));
        assertNumEquals(45.3978, ces.getValue(9));
        assertNumEquals(45.3762, ces.getValue(10));
        assertNumEquals(45.4450, ces.getValue(11));
        assertNumEquals(45.5040, ces.getValue(12));
        assertNumEquals(45.3912, ces.getValue(13));
        assertNumEquals(44.9909, ces.getValue(14));
    }
}
