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

public class StochasticOscillatorKIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries data;

    public StochasticOscillatorKIndicatorTest(Function<Number, Num> function) {
        super(function);
    }

    @Before
    public void setUp() {

        List<Bar> bars = new ArrayList<Bar>();
        bars.add(new MockBar(44.98, 119.13, 119.50, 116.00, numFunction));
        bars.add(new MockBar(45.05, 116.75, 119.94, 116.00, numFunction));
        bars.add(new MockBar(45.11, 113.50, 118.44, 111.63, numFunction));
        bars.add(new MockBar(45.19, 111.56, 114.19, 110.06, numFunction));
        bars.add(new MockBar(45.12, 112.25, 112.81, 109.63, numFunction));
        bars.add(new MockBar(45.15, 110.00, 113.44, 109.13, numFunction));
        bars.add(new MockBar(45.13, 113.50, 115.81, 110.38, numFunction));
        bars.add(new MockBar(45.12, 117.13, 117.50, 114.06, numFunction));
        bars.add(new MockBar(45.15, 115.63, 118.44, 114.81, numFunction));
        bars.add(new MockBar(45.24, 114.13, 116.88, 113.13, numFunction));
        bars.add(new MockBar(45.43, 118.81, 119.00, 116.19, numFunction));
        bars.add(new MockBar(45.43, 117.38, 119.75, 117.00, numFunction));
        bars.add(new MockBar(45.58, 119.13, 119.13, 116.88, numFunction));
        bars.add(new MockBar(45.58, 115.38, 119.44, 114.56, numFunction));

        data = new BaseBarSeries(bars);
    }

    @Test
    public void stochasticOscilatorKParam14() {

        StochasticOscillatorKIndicator sof = new StochasticOscillatorKIndicator(data, 14);

        assertNumEquals(313 / 3.5, sof.getValue(0));
        assertNumEquals(1000 / 10.81, sof.getValue(12));
        assertNumEquals(57.8168, sof.getValue(13));
    }
}
