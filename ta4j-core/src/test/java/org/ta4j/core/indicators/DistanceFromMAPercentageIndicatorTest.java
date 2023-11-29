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

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class DistanceFromMAPercentageIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {
    private BarSeries data;

    public DistanceFromMAPercentageIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        data = new MockBarSeries(numFunction, 10, 15, 20, 18, 17, 18, 15, 12, 10, 8, 5, 2);
    }

    @Test
    public void DistanceFromMovingAverageTest() {
        SMAIndicator sma = new SMAIndicator(new ClosePriceIndicator(data), 3);
        DistanceFromMAPercentageIndicator distanceFromMAIndicator = new DistanceFromMAPercentageIndicator(data, sma);
        assertNumEquals(0.3333, distanceFromMAIndicator.getValue(2));
        assertNumEquals(0.01886792452830182, distanceFromMAIndicator.getValue(5));
        assertNumEquals(-0.1, distanceFromMAIndicator.getValue(6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void DistanceFromIllegalMovingAverage() {
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(data);
        new DistanceFromMAPercentageIndicator(data, closePriceIndicator);
    }
}
