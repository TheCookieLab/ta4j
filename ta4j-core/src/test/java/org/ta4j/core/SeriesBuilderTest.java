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

import static junit.framework.TestCase.assertEquals;
import static org.ta4j.core.TestUtils.assertNumEquals;
import static org.ta4j.core.TestUtils.assertNumNotEquals;

import java.time.ZonedDateTime;
import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

public class SeriesBuilderTest extends AbstractIndicatorTest<BarSeries, Num> {

    public SeriesBuilderTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    private final BaseBarSeriesBuilder seriesBuilder = new BaseBarSeriesBuilder().withNumTypeOf(numFunction);

    @Test
    public void testBuilder() {
        BarSeries defaultSeries = seriesBuilder.build(); // build a new empty unnamed bar series
        BarSeries defaultSeriesName = seriesBuilder.withName("default").build(); // build a new empty bar series using
                                                                                 // BigDecimal as delegate
        BarSeries doubleSeries = seriesBuilder.withMaxBarCount(100)
                .withNumTypeOf(DoubleNum.class)
                .withName("useDoubleNum")
                .build();
        BarSeries precisionSeries = seriesBuilder.withMaxBarCount(100)
                .withNumTypeOf(DecimalNum.class)
                .withName("usePrecisionNum")
                .build();

        for (int i = 1000; i >= 0; i--) {
            defaultSeries.addBar(ZonedDateTime.now().minusSeconds(i), i, i, i, i, i);
            defaultSeriesName.addBar(ZonedDateTime.now().minusSeconds(i), i, i, i, i, i);
            doubleSeries.addBar(ZonedDateTime.now().minusSeconds(i), i, i, i, i, i);
            precisionSeries.addBar(ZonedDateTime.now().minusSeconds(i), i, i, i, i, i);
        }

        assertNumEquals(0, defaultSeries.getBar(1000).getClosePrice());
        assertNumEquals(1000, defaultSeries.getBar(0).getClosePrice());
        assertEquals(defaultSeriesName.getName(), "default");
        assertNumEquals(99, doubleSeries.getBar(0).getClosePrice());
        assertNumEquals(99, precisionSeries.getBar(0).getClosePrice());
    }

    @Test
    public void testNumFunctions() {
        BarSeries series = seriesBuilder.withNumTypeOf(DoubleNum.class).build();
        assertNumEquals(series.numOf(12), DoubleNum.valueOf(12));

        BarSeries seriesB = seriesBuilder.withNumTypeOf(DecimalNum.class).build();
        assertNumEquals(seriesB.numOf(12), DecimalNum.valueOf(12));
    }

    @Test
    public void testWrongNumType() {
        BarSeries series = seriesBuilder.withNumTypeOf(DecimalNum.class).build();
        assertNumNotEquals(series.numOf(12), DoubleNum.valueOf(12));
    }
}
