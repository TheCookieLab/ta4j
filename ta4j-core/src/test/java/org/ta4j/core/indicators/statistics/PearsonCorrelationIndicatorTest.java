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

import java.time.ZonedDateTime;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.VolumeIndicator;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.num.Num;

public class PearsonCorrelationIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private Indicator<Num> close, volume;

    public PearsonCorrelationIndicatorTest(Function<Number, Num> function) {
        super(function);
    }

    @Before
    public void setUp() {
        BarSeries data = new BaseBarSeriesBuilder().withNumTypeOf(numFunction).build();
        int i = 20;
        // close, volume
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 6, 100, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 7, 105, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 9, 130, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 12, 160, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 11, 150, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 10, 130, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 11, 95, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 13, 120, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 15, 180, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 12, 160, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 8, 150, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 4, 200, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 3, 150, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 4, 85, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 3, 70, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 5, 90, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 8, 100, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 9, 95, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i--), 11, 110, numFunction));
        data.addBar(new MockBar(ZonedDateTime.now().minusSeconds(i), 10, 95, numFunction));

        close = new ClosePriceIndicator(data);
        volume = new VolumeIndicator(data, 2);
    }

    @Test
    public void test() {
        PearsonCorrelationIndicator coef = new PearsonCorrelationIndicator(close, volume, 5);

        assertNumEquals(0.94947469058476818628408908843839, coef.getValue(1));
        assertNumEquals(0.9640797490298872, coef.getValue(2));
        assertNumEquals(0.9666189661412724, coef.getValue(3));
        assertNumEquals(0.9219, coef.getValue(4));
        assertNumEquals(0.9205, coef.getValue(5));
        assertNumEquals(0.4565, coef.getValue(6));
        assertNumEquals(-0.4622, coef.getValue(7));
        assertNumEquals(0.05747, coef.getValue(8));
        assertNumEquals(0.1442, coef.getValue(9));
        assertNumEquals(-0.1263, coef.getValue(10));
        assertNumEquals(-0.5345, coef.getValue(11));
        assertNumEquals(-0.7275, coef.getValue(12));
        assertNumEquals(0.1676, coef.getValue(13));
        assertNumEquals(0.2506, coef.getValue(14));
        assertNumEquals(-0.2938, coef.getValue(15));
        assertNumEquals(-0.3586, coef.getValue(16));
        assertNumEquals(0.1713, coef.getValue(17));
        assertNumEquals(0.9841, coef.getValue(18));
        assertNumEquals(0.9799, coef.getValue(19));
    }
}
