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

import org.ta4j.core.indicators.helpers.DateTimeIndicator;
import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class DateTimeIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public DateTimeIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Test
    public void test() {
        ZonedDateTime expectedZonedDateTime = ZonedDateTime.parse("2019-09-17T00:04:00-00:00", DATE_TIME_FORMATTER);
        List<Bar> bars = Arrays.asList(new MockBar(expectedZonedDateTime, 1, numFunction));
        BarSeries series = new MockBarSeries(bars);
        DateTimeIndicator dateTimeIndicator = new DateTimeIndicator(series, Bar::getEndTime);
        assertEquals(expectedZonedDateTime, dateTimeIndicator.getValue(0));
    }
}
