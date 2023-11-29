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
package org.ta4j.core.indicators.statistics;

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class StandardDeviationIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {
    private BarSeries data;

    public StandardDeviationIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        data = new MockBarSeries(numFunction, 1, 2, 3, 4, 3, 4, 5, 4, 3, 0, 9);
    }

    @Test
    public void standardDeviationUsingBarCount4UsingClosePrice() {
        StandardDeviationIndicator sdv = new StandardDeviationIndicator(new ClosePriceIndicator(data), 4);

        assertNumEquals(0, sdv.getValue(0));
        assertNumEquals(Math.sqrt(0.25), sdv.getValue(1));
        assertNumEquals(Math.sqrt(2.0 / 3), sdv.getValue(2));
        assertNumEquals(Math.sqrt(1.25), sdv.getValue(3));
        assertNumEquals(Math.sqrt(0.5), sdv.getValue(4));
        assertNumEquals(Math.sqrt(0.25), sdv.getValue(5));
        assertNumEquals(Math.sqrt(0.5), sdv.getValue(6));
        assertNumEquals(Math.sqrt(0.5), sdv.getValue(7));
        assertNumEquals(Math.sqrt(0.5), sdv.getValue(8));
        assertNumEquals(Math.sqrt(3.5), sdv.getValue(9));
        assertNumEquals(Math.sqrt(10.5), sdv.getValue(10));
    }

    @Test
    public void standardDeviationShouldBeZeroWhenBarCountIs1() {
        StandardDeviationIndicator sdv = new StandardDeviationIndicator(new ClosePriceIndicator(data), 1);
        assertNumEquals(0, sdv.getValue(3));
        assertNumEquals(0, sdv.getValue(8));
    }
}
