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
package org.ta4j.core.indicators.bollinger;

import static org.junit.Assert.assertTrue;
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

public class PercentBIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private ClosePriceIndicator closePrice;

    public PercentBIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        BarSeries data = new MockBarSeries(numFunction, 10, 12, 15, 14, 17, 20, 21, 20, 20, 19, 20, 17, 12, 12, 9, 8, 9,
                10, 9, 10);
        closePrice = new ClosePriceIndicator(data);
    }

    @Test
    public void percentBUsingSMAAndStandardDeviation() {

        PercentBIndicator pcb = new PercentBIndicator(closePrice, 5, 2);

        assertTrue(pcb.getValue(0).isNaN());
        assertNumEquals(0.75, pcb.getValue(1));
        assertNumEquals(0.8244, pcb.getValue(2));
        assertNumEquals(0.6627, pcb.getValue(3));
        assertNumEquals(0.8517, pcb.getValue(4));
        assertNumEquals(0.90328, pcb.getValue(5));
        assertNumEquals(0.83, pcb.getValue(6));
        assertNumEquals(0.6552, pcb.getValue(7));
        assertNumEquals(0.5737, pcb.getValue(8));
        assertNumEquals(0.1047, pcb.getValue(9));
        assertNumEquals(0.5, pcb.getValue(10));
        assertNumEquals(0.0284, pcb.getValue(11));
        assertNumEquals(0.0344, pcb.getValue(12));
        assertNumEquals(0.2064, pcb.getValue(13));
        assertNumEquals(0.1835, pcb.getValue(14));
        assertNumEquals(0.2131, pcb.getValue(15));
        assertNumEquals(0.3506, pcb.getValue(16));
        assertNumEquals(0.5737, pcb.getValue(17));
        assertNumEquals(0.5, pcb.getValue(18));
        assertNumEquals(0.7673, pcb.getValue(19));
    }
}
