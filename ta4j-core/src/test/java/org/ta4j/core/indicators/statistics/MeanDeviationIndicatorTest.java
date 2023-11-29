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

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class MeanDeviationIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries data;

    public MeanDeviationIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        data = new MockBarSeries(numFunction, 1, 2, 7, 6, 3, 4, 5, 11, 3, 0, 9);
    }

    @Test
    public void meanDeviationUsingBarCount5UsingClosePrice() {
        MeanDeviationIndicator meanDeviation = new MeanDeviationIndicator(new ClosePriceIndicator(data), 5);

        assertNumEquals(2.44444444444444, meanDeviation.getValue(2));
        assertNumEquals(2.5, meanDeviation.getValue(3));
        assertNumEquals(2.16, meanDeviation.getValue(7));
        assertNumEquals(2.32, meanDeviation.getValue(8));
        assertNumEquals(2.72, meanDeviation.getValue(9));
    }

    @Test
    public void firstValueShouldBeZero() {
        MeanDeviationIndicator meanDeviation = new MeanDeviationIndicator(new ClosePriceIndicator(data), 5);
        assertNumEquals(0, meanDeviation.getValue(0));
    }

    @Test
    public void meanDeviationShouldBeZeroWhenBarCountIs1() {
        MeanDeviationIndicator meanDeviation = new MeanDeviationIndicator(new ClosePriceIndicator(data), 1);
        assertNumEquals(0, meanDeviation.getValue(2));
        assertNumEquals(0, meanDeviation.getValue(7));
    }
}
