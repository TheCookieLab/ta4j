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

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class LWMAIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries data;

    public LWMAIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        data = new MockBarSeries(numFunction, 37.08, 36.7, 36.11, 35.85, 35.71, 36.04, 36.41, 37.67, 38.01, 37.79,
                36.83);
    }

    @Test
    public void lwmaUsingBarCount5UsingClosePrice() {
        LWMAIndicator lwma = new LWMAIndicator(new ClosePriceIndicator(data), 5);
        assertNumEquals(0.0, lwma.getValue(0));
        assertNumEquals(0.0, lwma.getValue(1));
        assertNumEquals(0.0, lwma.getValue(2));
        assertNumEquals(0.0, lwma.getValue(3));
        assertNumEquals(36.0506, lwma.getValue(4));
        assertNumEquals(35.9673, lwma.getValue(5));
        assertNumEquals(36.0766, lwma.getValue(6));
        assertNumEquals(36.6253, lwma.getValue(7));
        assertNumEquals(37.1833, lwma.getValue(8));
        assertNumEquals(37.5240, lwma.getValue(9));
        assertNumEquals(37.4060, lwma.getValue(10));
    }
}
