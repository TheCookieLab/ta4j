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

public class LossIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries data;

    public LossIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        data = new MockBarSeries(numFunction, 1, 2, 3, 4, 3, 4, 7, 4, 3, 3, 5, 3, 2);
    }

    @Test
    public void lossUsingClosePrice() {
        LossIndicator loss = new LossIndicator(new ClosePriceIndicator(data));
        assertNumEquals(0, loss.getValue(0));
        assertNumEquals(0, loss.getValue(1));
        assertNumEquals(0, loss.getValue(2));
        assertNumEquals(0, loss.getValue(3));
        assertNumEquals(1, loss.getValue(4));
        assertNumEquals(0, loss.getValue(5));
        assertNumEquals(0, loss.getValue(6));
        assertNumEquals(3, loss.getValue(7));
        assertNumEquals(1, loss.getValue(8));
        assertNumEquals(0, loss.getValue(9));
        assertNumEquals(0, loss.getValue(10));
        assertNumEquals(2, loss.getValue(11));
        assertNumEquals(1, loss.getValue(12));
    }
}
