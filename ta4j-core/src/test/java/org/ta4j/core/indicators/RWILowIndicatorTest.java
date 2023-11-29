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

import static org.ta4j.core.TestUtils.assertIndicatorEquals;

import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.ExternalIndicatorTest;
import org.ta4j.core.num.Num;

/**
 * Testing the RWILowIndicator
 */
public class RWILowIndicatorTest extends AbstractIndicatorTest<BarSeries, Num> {

    /**
     * TODO: Just graphically Excel-Sheet validation with hard coded results. Excel
     * formula needed
     */
    private ExternalIndicatorTest xls;

    public RWILowIndicatorTest(Function<Number, Num> numFunction) {
        super((data, params) -> new RWILowIndicator(data, (int) params[0]), numFunction);
        xls = new XLSIndicatorTest(this.getClass(), "RWIHL.xls", 9, numFunction);
    }

    @Test
    public void randomWalkIndexHigh() throws Exception {
        BarSeries series = xls.getSeries();
        RWILowIndicator rwih = (RWILowIndicator) getIndicator(series, 20);
        assertIndicatorEquals(getIndicator(series, 20), rwih);
    }
}
