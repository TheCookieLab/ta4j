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

import static org.ta4j.core.TestUtils.assertNumEquals;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.num.Num;

public class ConstantIndicatorTest {
    private ConstantIndicator<Num> constantIndicator;

    @Before
    public void setUp() {
        BarSeries series = new BaseBarSeries();
        constantIndicator = new ConstantIndicator<Num>(series, series.numOf(30.33));
    }

    @Test
    public void constantIndicator() {
        assertNumEquals("30.33", constantIndicator.getValue(0));
        assertNumEquals("30.33", constantIndicator.getValue(1));
        assertNumEquals("30.33", constantIndicator.getValue(10));
        assertNumEquals("30.33", constantIndicator.getValue(30));
    }
}
