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
/**
 * 
 */
package org.ta4j.core.indicators;

import static org.junit.Assert.assertTrue;

import java.time.ZonedDateTime;
import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

/**
 * @author jtomkinson
 *
 */
public class ChopIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    protected BarSeries series;
    protected final BaseBarSeriesBuilder BarSeriesBuilder = new BaseBarSeriesBuilder().withNumTypeOf(numFunction);

    public ChopIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    /**
     * this will assert that choppiness is high if market price is not moving
     */
    @Test
    public void testChoppy() {
        series = BarSeriesBuilder.withName("low volatility series").withNumTypeOf(numFunction).build();
        for (int i = 0; i < 50; i++) {
            ZonedDateTime date = ZonedDateTime.now().minusSeconds(100000 - i);
            series.addBar(date, 21.5, 21.5 + 1, 21.5 - 1, 21.5);
        }
        ChopIndicator ci1 = new ChopIndicator(series, 14, 100);
        int HIGH_CHOPPINESS_VALUE = 85;
        assertTrue(ci1.getValue(series.getEndIndex()).doubleValue() > HIGH_CHOPPINESS_VALUE);
    }

    /**
     * this will assert that choppiness is low if market price is trending
     * significantly
     */
    @Test
    public void testTradeableTrend() {
        series = BarSeriesBuilder.withName("low volatility series").withNumTypeOf(numFunction).build();
        float value = 21.5f;
        for (int i = 0; i < 50; i++) {
            ZonedDateTime date = ZonedDateTime.now().minusSeconds(100000 - i);
            series.addBar(date, value, value + 1, value - 1, value);
            value += 2.0f;
        }
        ChopIndicator ci1 = new ChopIndicator(series, 14, 100);
        int LOW_CHOPPINESS_VALUE = 30;
        assertTrue(ci1.getValue(series.getEndIndex()).doubleValue() < LOW_CHOPPINESS_VALUE);
    }

    // TODO: this test class needs better cases

}
