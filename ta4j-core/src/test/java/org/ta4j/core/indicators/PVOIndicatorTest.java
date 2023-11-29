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
import org.ta4j.core.Indicator;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class PVOIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries barSeries;

    public PVOIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        List<Bar> bars = new ArrayList<Bar>();
        bars.add(new MockBar(0, 10, numFunction));
        bars.add(new MockBar(0, 11, numFunction));
        bars.add(new MockBar(0, 12, numFunction));
        bars.add(new MockBar(0, 13, numFunction));
        bars.add(new MockBar(0, 150, numFunction));
        bars.add(new MockBar(0, 155, numFunction));
        bars.add(new MockBar(0, 160, numFunction));
        barSeries = new MockBarSeries(bars);
    }

    @Test
    public void createPvoIndicator() {
        PVOIndicator pvo = new PVOIndicator(barSeries);
        assertNumEquals(0.791855204, pvo.getValue(1));
        assertNumEquals(2.164434756, pvo.getValue(2));
        assertNumEquals(3.925400464, pvo.getValue(3));
        assertNumEquals(55.296120101, pvo.getValue(4));
        assertNumEquals(66.511722064, pvo.getValue(5));
    }
}
