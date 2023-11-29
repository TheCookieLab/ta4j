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

public class VarianceIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {
    private BarSeries data;

    public VarianceIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        data = new MockBarSeries(numFunction, 1, 2, 3, 4, 3, 4, 5, 4, 3, 0, 9);
    }

    @Test
    public void varianceUsingBarCount4UsingClosePrice() {
        VarianceIndicator var = new VarianceIndicator(new ClosePriceIndicator(data), 4);

        assertNumEquals(0, var.getValue(0));
        assertNumEquals(0.25, var.getValue(1));
        assertNumEquals(2.0 / 3, var.getValue(2));
        assertNumEquals(1.25, var.getValue(3));
        assertNumEquals(0.5, var.getValue(4));
        assertNumEquals(0.25, var.getValue(5));
        assertNumEquals(0.5, var.getValue(6));
        assertNumEquals(0.5, var.getValue(7));
        assertNumEquals(0.5, var.getValue(8));
        assertNumEquals(3.5, var.getValue(9));
        assertNumEquals(10.5, var.getValue(10));
    }

    @Test
    public void firstValueShouldBeZero() {
        VarianceIndicator var = new VarianceIndicator(new ClosePriceIndicator(data), 4);
        assertNumEquals(0, var.getValue(0));
    }

    @Test
    public void varianceShouldBeZeroWhenBarCountIs1() {
        VarianceIndicator var = new VarianceIndicator(new ClosePriceIndicator(data), 1);
        assertNumEquals(0, var.getValue(3));
        assertNumEquals(0, var.getValue(8));
    }

    @Test
    public void varianceUsingBarCount2UsingClosePrice() {
        VarianceIndicator var = new VarianceIndicator(new ClosePriceIndicator(data), 2);

        assertNumEquals(0, var.getValue(0));
        assertNumEquals(0.25, var.getValue(1));
        assertNumEquals(0.25, var.getValue(2));
        assertNumEquals(0.25, var.getValue(3));
        assertNumEquals(2.25, var.getValue(9));
        assertNumEquals(20.25, var.getValue(10));
    }
}
