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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class TRIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    public TRIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Test
    public void getValue() {
        List<Bar> bars = new ArrayList<Bar>();
        bars.add(new MockBar(0, 12, 15, 8, numFunction));
        bars.add(new MockBar(0, 8, 11, 6, numFunction));
        bars.add(new MockBar(0, 15, 17, 14, numFunction));
        bars.add(new MockBar(0, 15, 17, 14, numFunction));
        bars.add(new MockBar(0, 0, 0, 2, numFunction));
        TRIndicator tr = new TRIndicator(new MockBarSeries(bars));

        assertNumEquals(7, tr.getValue(0));
        assertNumEquals(6, tr.getValue(1));
        assertNumEquals(9, tr.getValue(2));
        assertNumEquals(3, tr.getValue(3));
        assertNumEquals(15, tr.getValue(4));
    }
}
