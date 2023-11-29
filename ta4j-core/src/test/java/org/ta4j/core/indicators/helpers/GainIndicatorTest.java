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
package org.ta4j.core.indicators.helpers;

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class GainIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries data;

    public GainIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        data = new MockBarSeries(numFunction, 1, 2, 3, 4, 3, 4, 7, 4, 3, 3, 5, 3, 2);
    }

    @Test
    public void gainUsingClosePrice() {
        GainIndicator gain = new GainIndicator(new ClosePriceIndicator(data));
        assertNumEquals(0, gain.getValue(0));
        assertNumEquals(1, gain.getValue(1));
        assertNumEquals(1, gain.getValue(2));
        assertNumEquals(1, gain.getValue(3));
        assertNumEquals(0, gain.getValue(4));
        assertNumEquals(1, gain.getValue(5));
        assertNumEquals(3, gain.getValue(6));
        assertNumEquals(0, gain.getValue(7));
        assertNumEquals(0, gain.getValue(8));
        assertNumEquals(0, gain.getValue(9));
        assertNumEquals(2, gain.getValue(10));
        assertNumEquals(0, gain.getValue(11));
        assertNumEquals(0, gain.getValue(12));
    }
}
