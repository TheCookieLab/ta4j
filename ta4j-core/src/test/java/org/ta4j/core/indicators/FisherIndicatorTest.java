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

import java.time.ZonedDateTime;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.Indicator;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.num.Num;

public class FisherIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    protected BarSeries series;

    public FisherIndicatorTest(Function<Number, Num> numFunction) {
        super(null, numFunction);
    }

    @Before
    public void setUp() {

        series = new BaseBarSeriesBuilder().withNumTypeOf(numFunction).withName("NaN test").build();
        int i = 20;
        // open, close, max, min
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 44.98, 45.05, 45.17, 44.96, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.05, 45.10, 45.15, 44.99, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.11, 45.19, 45.32, 45.11, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.19, 45.14, 45.25, 45.04, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.12, 45.15, 45.20, 45.10, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.15, 45.14, 45.20, 45.10, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.13, 45.10, 45.16, 45.07, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.12, 45.15, 45.22, 45.10, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.15, 45.22, 45.27, 45.14, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.24, 45.43, 45.45, 45.20, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.43, 45.44, 45.50, 45.39, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.43, 45.55, 45.60, 45.35, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.58, 45.55, 45.61, 45.39, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.45, 45.01, 45.55, 44.80, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 45.03, 44.23, 45.04, 44.17, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 44.23, 43.95, 44.29, 43.81, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 43.91, 43.08, 43.99, 43.08, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 43.07, 43.55, 43.65, 43.06, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i--), 43.56, 43.95, 43.99, 43.53, 0, 1, 0, numFunction));
        series.addBar(
                new MockBar(ZonedDateTime.now().minusSeconds(i), 43.93, 44.47, 44.58, 43.93, 0, 1, 0, numFunction));
    }

    @Test
    public void fisher() {
        FisherIndicator fisher = new FisherIndicator(series);

        assertNumEquals(0.6448642008177138, fisher.getValue(10));
        assertNumEquals(0.8361770425706673, fisher.getValue(11));
        assertNumEquals(0.9936697984965788, fisher.getValue(12));
        assertNumEquals(0.8324807235379169, fisher.getValue(13));
        assertNumEquals(0.5026313552592737, fisher.getValue(14));
        assertNumEquals(0.06492516204615063, fisher.getValue(15));
    }
}
