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

public class RAVIIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries data;

    public RAVIIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {

        data = new MockBarSeries(numFunction, 110.00, 109.27, 104.69, 107.07, 107.92, 107.95, 108.70, 107.97, 106.09,
                106.03, 108.65, 109.54, 112.26, 114.38, 117.94

        );
    }

    @Test
    public void ravi() {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(data);
        RAVIIndicator ravi = new RAVIIndicator(closePrice, 3, 8);

        assertNumEquals(0, ravi.getValue(0));
        assertNumEquals(0, ravi.getValue(1));
        assertNumEquals(0, ravi.getValue(2));
        assertNumEquals(-0.6937, ravi.getValue(3));
        assertNumEquals(-1.1411, ravi.getValue(4));
        assertNumEquals(-0.1577, ravi.getValue(5));
        assertNumEquals(0.229, ravi.getValue(6));
        assertNumEquals(0.2412, ravi.getValue(7));
        assertNumEquals(0.1202, ravi.getValue(8));
        assertNumEquals(-0.3324, ravi.getValue(9));
        assertNumEquals(-0.5804, ravi.getValue(10));
        assertNumEquals(0.2013, ravi.getValue(11));
        assertNumEquals(1.6156, ravi.getValue(12));
        assertNumEquals(2.6167, ravi.getValue(13));
        assertNumEquals(4.0799, ravi.getValue(14));
    }
}
