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
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class ROCIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private double[] closePriceValues = new double[] { 11045.27, 11167.32, 11008.61, 11151.83, 10926.77, 10868.12,
            10520.32, 10380.43, 10785.14, 10748.26, 10896.91, 10782.95, 10620.16, 10625.83, 10510.95, 10444.37,
            10068.01, 10193.39, 10066.57, 10043.75 };

    private ClosePriceIndicator closePrice;

    public ROCIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        closePrice = new ClosePriceIndicator(new MockBarSeries(numFunction, closePriceValues));
    }

    @Test
    public void getValueWhenBarCountIs12() {
        ROCIndicator roc = new ROCIndicator(closePrice, 12);

        // Incomplete time frame
        assertNumEquals(0, roc.getValue(0));
        assertNumEquals(1.105, roc.getValue(1));
        assertNumEquals(-0.3319, roc.getValue(2));
        assertNumEquals(0.9648, roc.getValue(3));

        // Complete time frame
        double[] results13to20 = new double[] { -3.8488, -4.8489, -4.5206, -6.3439, -7.8592, -6.2083, -4.3131,
                -3.2434 };
        for (int i = 0; i < results13to20.length; i++) {
            assertNumEquals(results13to20[i], roc.getValue(i + 12));
        }
    }
}
