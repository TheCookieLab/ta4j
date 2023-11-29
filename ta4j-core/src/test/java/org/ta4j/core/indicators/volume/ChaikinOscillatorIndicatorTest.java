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

import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class ChaikinOscillatorIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    public ChaikinOscillatorIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Test
    public void getValue() {
        List<Bar> bars = new ArrayList<>();
        bars.add(new MockBar(12.915, 13.600, 12.890, 13.550, 264266, numFunction));
        bars.add(new MockBar(13.550, 13.770, 13.310, 13.505, 305427, numFunction));
        bars.add(new MockBar(13.510, 13.590, 13.425, 13.490, 104077, numFunction));
        bars.add(new MockBar(13.515, 13.545, 13.400, 13.480, 136135, numFunction));
        bars.add(new MockBar(13.490, 13.495, 13.310, 13.345, 92090, numFunction));
        bars.add(new MockBar(13.350, 13.490, 13.325, 13.420, 80948, numFunction));
        bars.add(new MockBar(13.415, 13.460, 13.290, 13.300, 82983, numFunction));
        bars.add(new MockBar(13.320, 13.320, 13.090, 13.130, 126918, numFunction));
        bars.add(new MockBar(13.145, 13.225, 13.090, 13.150, 68560, numFunction));
        bars.add(new MockBar(13.150, 13.250, 13.110, 13.245, 41178, numFunction));
        bars.add(new MockBar(13.245, 13.250, 13.120, 13.210, 63606, numFunction));
        bars.add(new MockBar(13.210, 13.275, 13.185, 13.275, 34402, numFunction));

        BarSeries series = new MockBarSeries(bars);
        ChaikinOscillatorIndicator co = new ChaikinOscillatorIndicator(series);

        assertNumEquals(0.0, co.getValue(0));
        assertNumEquals(-361315.15734265576, co.getValue(1));
        assertNumEquals(-611288.0465670675, co.getValue(2));
        assertNumEquals(-771681.707243684, co.getValue(3));
        assertNumEquals(-1047600.3223165069, co.getValue(4));
        assertNumEquals(-1128952.3867409695, co.getValue(5));
        assertNumEquals(-1930922.241574394, co.getValue(6));
        assertNumEquals(-2507483.932954022, co.getValue(7));
        assertNumEquals(-2591747.9037044123, co.getValue(8));
        assertNumEquals(-2404678.698472605, co.getValue(9));
        assertNumEquals(-2147771.081319658, co.getValue(10));
        assertNumEquals(-1858366.685091666, co.getValue(11));
    }
}
