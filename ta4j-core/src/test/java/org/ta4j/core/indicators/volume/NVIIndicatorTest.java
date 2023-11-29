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
package org.ta4j.core.indicators.volume;

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class NVIIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    public NVIIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Test
    public void getValue() {

        List<Bar> bars = new ArrayList<Bar>();
        bars.add(new MockBar(1355.69, 2739.55, numFunction));
        bars.add(new MockBar(1325.51, 3119.46, numFunction));
        bars.add(new MockBar(1335.02, 3466.88, numFunction));
        bars.add(new MockBar(1313.72, 2577.12, numFunction));
        bars.add(new MockBar(1319.99, 2480.45, numFunction));
        bars.add(new MockBar(1331.85, 2329.79, numFunction));
        bars.add(new MockBar(1329.04, 2793.07, numFunction));
        bars.add(new MockBar(1362.16, 3378.78, numFunction));
        bars.add(new MockBar(1365.51, 2417.59, numFunction));
        bars.add(new MockBar(1374.02, 1442.81, numFunction));
        BarSeries series = new MockBarSeries(bars);

        NVIIndicator nvi = new NVIIndicator(series);
        assertNumEquals(1000, nvi.getValue(0));
        assertNumEquals(1000, nvi.getValue(1));
        assertNumEquals(1000, nvi.getValue(2));
        assertNumEquals(984.0452, nvi.getValue(3));
        assertNumEquals(988.7417, nvi.getValue(4));
        assertNumEquals(997.6255, nvi.getValue(5));
        assertNumEquals(997.6255, nvi.getValue(6));
        assertNumEquals(997.6255, nvi.getValue(7));
        assertNumEquals(1000.079, nvi.getValue(8));
        assertNumEquals(1006.3116, nvi.getValue(9));
    }
}
