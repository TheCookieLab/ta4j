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
package org.ta4j.core.indicators.vortex;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.Indicator;
import static org.ta4j.core.TestUtils.assertNumEquals;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBarSeries;
import static org.ta4j.core.num.NaN.NaN;
import org.ta4j.core.num.Num;

public class MinusVMIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private MinusVMIndicator minusVMIndicator;
    private BarSeries series;

    public MinusVMIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        List<Bar> bars = new ArrayList<>();
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 10, 11, 9, 10, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 20, 21, 19, 20, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 30, 31, 29, 30, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 40, 41, 39, 40, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 30, 31, 29, 30, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 20, 21, 19, 20, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 10, 11, 9, 10, 1, 0, 0, numFunction));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 5, 6, 4, 5, 1, 0, 0, numFunction));

        series = new MockBarSeries(bars);
        minusVMIndicator = new MinusVMIndicator(series);
    }

    @Test
    public void givenValidSetup_whenGetValue_thenReturnsCorrectValues() {
        assertNumEquals(NaN, minusVMIndicator.getValue(0));
        assertNumEquals(8, minusVMIndicator.getValue(1));
        assertNumEquals(8, minusVMIndicator.getValue(2));
        assertNumEquals(8, minusVMIndicator.getValue(3));
        assertNumEquals(12, minusVMIndicator.getValue(4));
        assertNumEquals(12, minusVMIndicator.getValue(5));
        assertNumEquals(12, minusVMIndicator.getValue(6));
        assertNumEquals(7, minusVMIndicator.getValue(7));
    }
}
