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

import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class WMAIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    public WMAIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Test
    public void calculate() {
        MockBarSeries series = new MockBarSeries(numFunction, 1d, 2d, 3d, 4d, 5d, 6d);
        Indicator<Num> close = new ClosePriceIndicator(series);
        Indicator<Num> wmaIndicator = new WMAIndicator(close, 3);

        assertNumEquals(1, wmaIndicator.getValue(0));
        assertNumEquals(1.6667, wmaIndicator.getValue(1));
        assertNumEquals(2.3333, wmaIndicator.getValue(2));
        assertNumEquals(3.3333, wmaIndicator.getValue(3));
        assertNumEquals(4.3333, wmaIndicator.getValue(4));
        assertNumEquals(5.3333, wmaIndicator.getValue(5));
    }

    @Test
    public void wmaWithBarCountGreaterThanSeriesSize() {
        MockBarSeries series = new MockBarSeries(numFunction, 1d, 2d, 3d, 4d, 5d, 6d);
        Indicator<Num> close = new ClosePriceIndicator(series);
        Indicator<Num> wmaIndicator = new WMAIndicator(close, 55);

        assertNumEquals(1, wmaIndicator.getValue(0));
        assertNumEquals(1.6667, wmaIndicator.getValue(1));
        assertNumEquals(2.3333, wmaIndicator.getValue(2));
        assertNumEquals(3, wmaIndicator.getValue(3));
        assertNumEquals(3.6666, wmaIndicator.getValue(4));
        assertNumEquals(4.3333, wmaIndicator.getValue(5));
    }

    @Test
    public void wmaUsingBarCount9UsingClosePrice() {
        // Example from
        // http://traders.com/Documentation/FEEDbk_docs/2010/12/TradingIndexesWithHullMA.xls
        BarSeries data = new MockBarSeries(numFunction, 84.53, 87.39, 84.55, 82.83, 82.58, 83.74, 83.33, 84.57, 86.98,
                87.10, 83.11, 83.60, 83.66, 82.76, 79.22, 79.03, 78.18, 77.42, 74.65, 77.48, 76.87);

        WMAIndicator wma = new WMAIndicator(new ClosePriceIndicator(data), 9);
        assertNumEquals(84.4958, wma.getValue(8));
        assertNumEquals(85.0158, wma.getValue(9));
        assertNumEquals(84.6807, wma.getValue(10));
        assertNumEquals(84.5387, wma.getValue(11));
        assertNumEquals(84.4298, wma.getValue(12));
        assertNumEquals(84.1224, wma.getValue(13));
        assertNumEquals(83.1031, wma.getValue(14));
        assertNumEquals(82.1462, wma.getValue(15));
        assertNumEquals(81.1149, wma.getValue(16));
        assertNumEquals(80.0736, wma.getValue(17));
        assertNumEquals(78.6907, wma.getValue(18));
        assertNumEquals(78.1504, wma.getValue(19));
        assertNumEquals(77.6133, wma.getValue(20));
    }
}
