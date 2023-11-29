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

public class TripleEMAIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private ClosePriceIndicator closePrice;

    public TripleEMAIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        BarSeries data = new MockBarSeries(numFunction, 0.73, 0.72, 0.86, 0.72, 0.62, 0.76, 0.84, 0.69, 0.65, 0.71,
                0.53, 0.73, 0.77, 0.67, 0.68);
        closePrice = new ClosePriceIndicator(data);
    }

    @Test
    public void tripleEMAUsingBarCount5UsingClosePrice() {
        TripleEMAIndicator tripleEma = new TripleEMAIndicator(closePrice, 5);

        assertNumEquals(0.73, tripleEma.getValue(0));
        assertNumEquals(0.7229, tripleEma.getValue(1));
        assertNumEquals(0.8185, tripleEma.getValue(2));

        assertNumEquals(0.8027, tripleEma.getValue(6));
        assertNumEquals(0.7328, tripleEma.getValue(7));
        assertNumEquals(0.6725, tripleEma.getValue(8));

        assertNumEquals(0.7386, tripleEma.getValue(12));
        assertNumEquals(0.6994, tripleEma.getValue(13));
        assertNumEquals(0.6876, tripleEma.getValue(14));
    }
}
