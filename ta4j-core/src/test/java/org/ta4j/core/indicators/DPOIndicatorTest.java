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

import static org.junit.Assert.assertEquals;
import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class DPOIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries series;

    public DPOIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        series = new MockBarSeries(numFunction, 22.27, 22.19, 22.08, 22.17, 22.18, 22.13, 22.23, 22.43, 22.24, 22.29,
                22.15, 22.39, 22.38, 22.61, 23.36, 24.05, 23.75, 23.83, 23.95, 23.63, 23.82, 23.87, 23.65, 23.19, 23.10,
                23.33, 22.68, 23.10, 22.40, 22.17, 22.27, 22.19, 22.08, 22.17, 22.18, 22.13, 22.23, 22.43, 22.24, 22.29,
                22.15, 22.39, 22.38, 22.61, 23.36, 24.05, 23.75, 23.83, 23.95, 23.63, 23.82, 23.87, 23.65, 23.19, 23.10,
                23.33, 22.68, 23.10, 22.40, 22.17, 22.27, 22.19, 22.08, 22.17, 22.18, 22.13, 22.23, 22.43, 22.24, 22.29,
                22.15, 22.39, 22.38, 22.61, 23.36, 24.05, 23.75, 23.83, 23.95, 23.63, 23.82, 23.87, 23.65, 23.19, 23.10,
                23.33, 22.68, 23.10, 22.40, 22.17);
    }

    @Test
    public void dpo() {
        DPOIndicator dpo = new DPOIndicator(series, 9);
        ClosePriceIndicator cp = new ClosePriceIndicator(series);
        SMAIndicator sma = new SMAIndicator(cp, 9);
        int timeShift = 9 / 2 + 1;

        // compare results to alternative calculation for each index
        for (int i = series.getBeginIndex(); i <= series.getEndIndex(); i++) {
            assertEquals(dpo.getValue(i), cp.getValue(i).minus(sma.getValue(i - timeShift)));
        }

        assertNumEquals(0.111999, dpo.getValue(9));
        assertNumEquals(-0.02, dpo.getValue(10));
        assertNumEquals(0.21142857142, dpo.getValue(11));
        assertNumEquals(0.169999999999999, dpo.getValue(12));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void dpoIOOBE() {
        DPOIndicator dpo = new DPOIndicator(series, 9);
        dpo.getValue(100);
    }
}
