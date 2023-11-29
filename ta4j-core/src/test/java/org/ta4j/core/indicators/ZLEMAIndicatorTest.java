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

import static junit.framework.TestCase.assertEquals;
import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class ZLEMAIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries data;

    public ZLEMAIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        data = new MockBarSeries(numFunction, 10, 15, 20, 18, 17, 18, 15, 12, 10, 8, 5, 2);
    }

    @Test
    public void ZLEMAUsingBarCount10UsingClosePrice() {
        ZLEMAIndicator zlema = new ZLEMAIndicator(new ClosePriceIndicator(data), 10);

        assertNumEquals(11.9091, zlema.getValue(9));
        assertNumEquals(8.8347, zlema.getValue(10));
        assertNumEquals(5.7739, zlema.getValue(11));
    }

    @Test
    public void ZLEMAFirstValueShouldBeEqualsToFirstDataValue() {
        ZLEMAIndicator zlema = new ZLEMAIndicator(new ClosePriceIndicator(data), 10);
        assertNumEquals(10, zlema.getValue(0));
    }

    @Test
    public void valuesLessThanBarCountMustBeEqualsToSMAValues() {
        ZLEMAIndicator zlema = new ZLEMAIndicator(new ClosePriceIndicator(data), 10);
        SMAIndicator sma = new SMAIndicator(new ClosePriceIndicator(data), 10);

        for (int i = 0; i < 9; i++) {
            assertEquals(sma.getValue(i), zlema.getValue(i));
        }
    }

    @Test
    public void smallBarCount() {
        ZLEMAIndicator zlema = new ZLEMAIndicator(new ClosePriceIndicator(data), 1);
        assertNumEquals(10, zlema.getValue(0));
    }
}
