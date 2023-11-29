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
package org.ta4j.core.indicators.donchian;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.num.Num;

public class DonchianChannelMiddleIndicatorTest extends AbstractIndicatorTest<BarSeries, Num> {

    private BarSeries series;

    public DonchianChannelMiddleIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        ZonedDateTime startDateTime = ZonedDateTime.now();
        List<Bar> bars = new ArrayList<>();

        bars.add(new BaseBar(Duration.ofHours(1), startDateTime, 100d, 105d, 95d, 100d, 0d, 0, 0, this::numOf));
        bars.add(new BaseBar(Duration.ofHours(1), startDateTime.plusHours(1), 105, 110, 100, 105, 0d, 0, 0,
                this::numOf));
        bars.add(new BaseBar(Duration.ofHours(1), startDateTime.plusHours(2), 110, 115, 105, 110, 0d, 0, 0,
                this::numOf));
        bars.add(new BaseBar(Duration.ofHours(1), startDateTime.plusHours(3), 115, 120, 110, 115, 0d, 0, 0,
                this::numOf));
        bars.add(new BaseBar(Duration.ofHours(1), startDateTime.plusHours(4), 120, 125, 115, 120, 0d, 0, 0,
                this::numOf));
        bars.add(new BaseBar(Duration.ofHours(1), startDateTime.plusHours(5), 115, 120, 110, 115, 0d, 0, 0,
                this::numOf));
        bars.add(new BaseBar(Duration.ofHours(1), startDateTime.plusHours(6), 110, 115, 105, 110, 0d, 0, 0,
                this::numOf));
        bars.add(new BaseBar(Duration.ofHours(1), startDateTime.plusHours(7), 105, 110, 100, 105, 0d, 0, 0,
                this::numOf));
        bars.add(
                new BaseBar(Duration.ofHours(1), startDateTime.plusHours(8), 100, 105, 95, 100, 0d, 0, 0, this::numOf));

        this.series = new BaseBarSeries("DonchianChannelMiddleIndicatorTestSeries", bars);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetValue() {
        DonchianChannelMiddleIndicator subject = new DonchianChannelMiddleIndicator(series, 3);

        assertEquals(numOf(100), subject.getValue(0));
        assertEquals(numOf(102.5), subject.getValue(1));
        assertEquals(numOf(105), subject.getValue(2));
        assertEquals(numOf(110), subject.getValue(3));
        assertEquals(numOf(115), subject.getValue(4));
        assertEquals(numOf(117.5), subject.getValue(5));
        assertEquals(numOf(115), subject.getValue(6));
        assertEquals(numOf(110), subject.getValue(7));
        assertEquals(numOf(105), subject.getValue(8));

    }
}
