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
package org.ta4j.core;

import static org.ta4j.core.TestUtils.assertIndicatorEquals;
import static org.ta4j.core.TestUtils.assertIndicatorNotEquals;
import static org.ta4j.core.TestUtils.assertNumEquals;
import static org.ta4j.core.TestUtils.assertNumNotEquals;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

public class TestUtilsTest extends AbstractIndicatorTest<BarSeries, Num> {

    private static final String stringDouble = "1234567890.12345";
    private static final String diffStringDouble = "1234567890.12346";
    private static final BigDecimal bigDecimalDouble = new BigDecimal(stringDouble);
    private static final BigDecimal diffBigDecimalDouble = new BigDecimal(diffStringDouble);
    private static final int aInt = 1234567890;
    private static final int diffInt = 1234567891;
    private static final double aDouble = 1234567890.1234;
    private static final double diffDouble = 1234567890.1235;
    private static Num numStringDouble;
    private static Num diffNumStringDouble;
    private static Num numInt;
    private static Num diffNumInt;
    private static Num numDouble;
    private static Num diffNumDouble;
    private static Indicator<Num> indicator;
    private static Indicator<Num> diffIndicator;

    public TestUtilsTest(Function<Number, Num> numFunction) {
        super(numFunction);
        numStringDouble = numOf(bigDecimalDouble);
        diffNumStringDouble = numOf(diffBigDecimalDouble);
        numInt = numOf(aInt);
        diffNumInt = numOf(diffInt);
        numDouble = numOf(aDouble);
        diffNumDouble = numOf(diffDouble);
        BarSeries series = randomSeries();
        BarSeries diffSeries = randomSeries();
        indicator = new ClosePriceIndicator(series);
        diffIndicator = new ClosePriceIndicator(diffSeries);
    }

    private BarSeries randomSeries() {
        BaseBarSeriesBuilder builder = new BaseBarSeriesBuilder();
        BarSeries series = builder.withNumTypeOf(numFunction).build();
        ZonedDateTime time = ZonedDateTime.of(1970, 1, 1, 1, 1, 1, 1, ZoneId.systemDefault());
        double random;
        for (int i = 0; i < 1000; i++) {
            random = Math.random();
            time = time.plusDays(i);
            series.addBar(new BaseBar(Duration.ofDays(1), time, random, random, random, random, random, random, 0,
                    numFunction));
        }
        return series;
    }

    @Test
    public void testStringNum() {
        assertNumEquals(stringDouble, numStringDouble);
        assertNumNotEquals(stringDouble, diffNumStringDouble);
        assertNumNotEquals(diffStringDouble, numStringDouble);
        assertNumEquals(diffStringDouble, diffNumStringDouble);
    }

    @Test
    public void testNumNum() {
        assertNumEquals(numStringDouble, numStringDouble);
        assertNumNotEquals(numStringDouble, diffNumStringDouble);
        assertNumNotEquals(diffNumStringDouble, numStringDouble);
        assertNumEquals(diffNumStringDouble, diffNumStringDouble);
    }

    @Test
    public void testIntNum() {
        assertNumEquals(aInt, numInt);
        assertNumNotEquals(aInt, diffNumInt);
        assertNumNotEquals(diffInt, numInt);
        assertNumEquals(diffInt, diffNumInt);
    }

    @Test
    public void testDoubleNum() {
        assertNumEquals(aDouble, numDouble);
        assertNumNotEquals(aDouble, diffNumDouble);
        assertNumNotEquals(diffDouble, numDouble);
        assertNumEquals(diffDouble, diffNumDouble);
    }

    @Test
    public void testIndicator() {
        assertIndicatorEquals(indicator, indicator);
        assertIndicatorNotEquals(indicator, diffIndicator);
        assertIndicatorNotEquals(diffIndicator, indicator);
        assertIndicatorEquals(diffIndicator, diffIndicator);
    }
}
