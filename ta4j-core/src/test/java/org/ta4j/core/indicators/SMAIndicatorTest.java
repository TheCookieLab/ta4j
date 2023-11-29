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

import static org.junit.Assert.assertEquals;
import static org.ta4j.core.TestUtils.assertIndicatorEquals;
import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.ExternalIndicatorTest;
import org.ta4j.core.Indicator;
import org.ta4j.core.TestUtils;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class SMAIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private ExternalIndicatorTest xls;

    public SMAIndicatorTest(Function<Number, Num> numFunction) throws Exception {
        super((data, params) -> new SMAIndicator((Indicator<Num>) data, (int) params[0]), numFunction);
        xls = new XLSIndicatorTest(this.getClass(), "SMA.xls", 6, numFunction);
    }

    private BarSeries data;

    @Before
    public void setUp() {
        data = new MockBarSeries(numFunction, 1, 2, 3, 4, 3, 4, 5, 4, 3, 3, 4, 3, 2);
    }

    @Test
    public void usingBarCount3UsingClosePrice() throws Exception {
        Indicator<Num> indicator = getIndicator(new ClosePriceIndicator(data), 3);

        assertNumEquals(1, indicator.getValue(0));
        assertNumEquals(1.5, indicator.getValue(1));
        assertNumEquals(2, indicator.getValue(2));
        assertNumEquals(3, indicator.getValue(3));
        assertNumEquals(10d / 3, indicator.getValue(4));
        assertNumEquals(11d / 3, indicator.getValue(5));
        assertNumEquals(4, indicator.getValue(6));
        assertNumEquals(13d / 3, indicator.getValue(7));
        assertNumEquals(4, indicator.getValue(8));
        assertNumEquals(10d / 3, indicator.getValue(9));
        assertNumEquals(10d / 3, indicator.getValue(10));
        assertNumEquals(10d / 3, indicator.getValue(11));
        assertNumEquals(3, indicator.getValue(12));
    }

    @Test
    public void whenBarCountIs1ResultShouldBeIndicatorValue() throws Exception {
        Indicator<Num> indicator = getIndicator(new ClosePriceIndicator(data), 1);
        for (int i = 0; i < data.getBarCount(); i++) {
            assertEquals(data.getBar(i).getClosePrice(), indicator.getValue(i));
        }
    }

    @Test
    public void externalData() throws Exception {
        Indicator<Num> xlsClose = new ClosePriceIndicator(xls.getSeries());
        Indicator<Num> actualIndicator;

        actualIndicator = getIndicator(xlsClose, 1);
        assertIndicatorEquals(xls.getIndicator(1), actualIndicator);
        assertEquals(329.0, actualIndicator.getValue(actualIndicator.getBarSeries().getEndIndex()).doubleValue(),
                TestUtils.GENERAL_OFFSET);

        actualIndicator = getIndicator(xlsClose, 3);
        assertIndicatorEquals(xls.getIndicator(3), actualIndicator);
        assertEquals(326.6333, actualIndicator.getValue(actualIndicator.getBarSeries().getEndIndex()).doubleValue(),
                TestUtils.GENERAL_OFFSET);

        actualIndicator = getIndicator(xlsClose, 13);
        assertIndicatorEquals(xls.getIndicator(13), actualIndicator);
        assertEquals(327.7846, actualIndicator.getValue(actualIndicator.getBarSeries().getEndIndex()).doubleValue(),
                TestUtils.GENERAL_OFFSET);
    }

}
