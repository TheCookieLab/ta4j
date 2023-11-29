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
import org.ta4j.core.indicators.helpers.MedianPriceIndicator;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class AwesomeOscillatorIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {
    private BarSeries series;

    /**
     * Constructor.
     *
     * @param function
     */
    public AwesomeOscillatorIndicatorTest(Function<Number, Num> function) {
        super(function);
    }

    @Before
    public void setUp() {

        List<Bar> bars = new ArrayList<Bar>();

        bars.add(new MockBar(0, 0, 16, 8, numFunction));
        bars.add(new MockBar(0, 0, 12, 6, numFunction));
        bars.add(new MockBar(0, 0, 18, 14, numFunction));
        bars.add(new MockBar(0, 0, 10, 6, numFunction));
        bars.add(new MockBar(0, 0, 8, 4, numFunction));

        this.series = new MockBarSeries(bars);
    }

    @Test
    public void calculateWithSma2AndSma3() {
        AwesomeOscillatorIndicator awesome = new AwesomeOscillatorIndicator(new MedianPriceIndicator(series), 2, 3);

        assertNumEquals(0, awesome.getValue(0));
        assertNumEquals(0, awesome.getValue(1));
        assertNumEquals(1d / 6, awesome.getValue(2));
        assertNumEquals(1, awesome.getValue(3));
        assertNumEquals(-3, awesome.getValue(4));
    }

    @Test
    public void withSma1AndSma2() {
        AwesomeOscillatorIndicator awesome = new AwesomeOscillatorIndicator(new MedianPriceIndicator(series), 1, 2);

        assertNumEquals(0, awesome.getValue(0));
        assertNumEquals("-1.5", awesome.getValue(1));
        assertNumEquals("3.5", awesome.getValue(2));
        assertNumEquals(-4, awesome.getValue(3));
        assertNumEquals(-1, awesome.getValue(4));
    }

    @Test
    public void withSmaDefault() {
        AwesomeOscillatorIndicator awesome = new AwesomeOscillatorIndicator(new MedianPriceIndicator(series));

        assertNumEquals(0, awesome.getValue(0));
        assertNumEquals(0, awesome.getValue(1));
        assertNumEquals(0, awesome.getValue(2));
        assertNumEquals(0, awesome.getValue(3));
        assertNumEquals(0, awesome.getValue(4));
    }

}
