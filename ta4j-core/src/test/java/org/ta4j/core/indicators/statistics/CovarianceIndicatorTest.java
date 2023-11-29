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

public class CovarianceIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private Indicator<Num> close, volume;

    public CovarianceIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
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
    public void usingBarCount5UsingClosePriceAndVolume() {
        CovarianceIndicator covar = new CovarianceIndicator(close, volume, 5);

        assertNumEquals(0, covar.getValue(0));
        assertNumEquals(26.25, covar.getValue(1));
        assertNumEquals(63.3333, covar.getValue(2));
        assertNumEquals(143.75, covar.getValue(3));
        assertNumEquals(156, covar.getValue(4));
        assertNumEquals(60.8, covar.getValue(5));
        assertNumEquals(15.2, covar.getValue(6));
        assertNumEquals(-17.6, covar.getValue(7));
        assertNumEquals(4, covar.getValue(8));
        assertNumEquals(11.6, covar.getValue(9));
        assertNumEquals(-14.4, covar.getValue(10));
        assertNumEquals(-100.2, covar.getValue(11));
        assertNumEquals(-70.0, covar.getValue(12));
        assertNumEquals(24.6, covar.getValue(13));
        assertNumEquals(35.0, covar.getValue(14));
        assertNumEquals(-19.0, covar.getValue(15));
        assertNumEquals(-47.8, covar.getValue(16));
        assertNumEquals(11.4, covar.getValue(17));
        assertNumEquals(55.8, covar.getValue(18));
        assertNumEquals(33.4, covar.getValue(19));
    }

    @Test
    public void firstValueShouldBeZero() {
        CovarianceIndicator covar = new CovarianceIndicator(close, volume, 5);
        assertNumEquals(0, covar.getValue(0));
    }

    @Test
    public void shouldBeZeroWhenBarCountIs1() {
        CovarianceIndicator covar = new CovarianceIndicator(close, volume, 1);
        assertNumEquals(0, covar.getValue(3));
        assertNumEquals(0, covar.getValue(8));
    }
}
