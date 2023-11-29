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

import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class TradeCountIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {
    private TradeCountIndicator tradeIndicator;

    BarSeries barSeries;

    public TradeCountIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        barSeries = new MockBarSeries(numFunction);
        tradeIndicator = new TradeCountIndicator(barSeries);
    }

    @Test
    public void indicatorShouldRetrieveBarTrade() {
        for (int i = 0; i < 10; i++) {
            assertEquals((long) tradeIndicator.getValue(i), barSeries.getBar(i).getTrades());
        }
    }
}
