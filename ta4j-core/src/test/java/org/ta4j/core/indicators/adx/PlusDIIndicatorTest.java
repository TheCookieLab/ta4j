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
package org.ta4j.core.indicators.adx;

import static org.junit.Assert.assertEquals;
import static org.ta4j.core.TestUtils.assertIndicatorEquals;

import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.ExternalIndicatorTest;
import org.ta4j.core.Indicator;
import org.ta4j.core.TestUtils;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.XLSIndicatorTest;
import org.ta4j.core.num.Num;

public class PlusDIIndicatorTest extends AbstractIndicatorTest<BarSeries, Num> {

    private ExternalIndicatorTest xls;

    public PlusDIIndicatorTest(Function<Number, Num> nf) throws Exception {
        super((data, params) -> new PlusDIIndicator((BarSeries) data, (int) params[0]), nf);
        xls = new XLSIndicatorTest(this.getClass(), "ADX.xls", 12, numFunction);
    }

    @Test
    public void testAgainstExternalData() throws Exception {
        BarSeries xlsSeries = xls.getSeries();
        Indicator<Num> actualIndicator;

        actualIndicator = getIndicator(xlsSeries, 1);
        assertIndicatorEquals(xls.getIndicator(1), actualIndicator);
        assertEquals(12.5, actualIndicator.getValue(actualIndicator.getBarSeries().getEndIndex()).doubleValue(),
                TestUtils.GENERAL_OFFSET);

        actualIndicator = getIndicator(xlsSeries, 3);
        assertIndicatorEquals(xls.getIndicator(3), actualIndicator);
        assertEquals(22.8407, actualIndicator.getValue(actualIndicator.getBarSeries().getEndIndex()).doubleValue(),
                TestUtils.GENERAL_OFFSET);

        actualIndicator = getIndicator(xlsSeries, 13);
        assertIndicatorEquals(xls.getIndicator(13), actualIndicator);
        assertEquals(22.1399, actualIndicator.getValue(actualIndicator.getBarSeries().getEndIndex()).doubleValue(),
                TestUtils.GENERAL_OFFSET);
    }

}
