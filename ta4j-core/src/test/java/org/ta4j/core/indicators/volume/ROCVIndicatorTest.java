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
package org.ta4j.core.indicators.volume;

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
import org.ta4j.core.num.Num;

public class ROCVIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    BarSeries series;

    public ROCVIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        List<Bar> bars = new ArrayList<Bar>();
        bars.add(new MockBar(1355.69, 1000, numFunction));
        bars.add(new MockBar(1325.51, 3000, numFunction));
        bars.add(new MockBar(1335.02, 3500, numFunction));
        bars.add(new MockBar(1313.72, 2200, numFunction));
        bars.add(new MockBar(1319.99, 2300, numFunction));
        bars.add(new MockBar(1331.85, 200, numFunction));
        bars.add(new MockBar(1329.04, 2700, numFunction));
        bars.add(new MockBar(1362.16, 5000, numFunction));
        bars.add(new MockBar(1365.51, 1000, numFunction));
        bars.add(new MockBar(1374.02, 2500, numFunction));
        series = new MockBarSeries(bars);
    }

    @Test
    public void test() {
        ROCVIndicator roc = new ROCVIndicator(series, 3);

        assertNumEquals(0, roc.getValue(0));
        assertNumEquals(200, roc.getValue(1));
        assertNumEquals(250, roc.getValue(2));
        assertNumEquals(120, roc.getValue(3));
        assertNumEquals(-23.333333333333332, roc.getValue(4));
        assertNumEquals(-94.28571428571429, roc.getValue(5));
        assertNumEquals(22.727272727272727, roc.getValue(6));
        assertNumEquals(117.3913043478261, roc.getValue(7));
        assertNumEquals(400, roc.getValue(8));
        assertNumEquals(-7.407407407407407, roc.getValue(9));
    }
}
