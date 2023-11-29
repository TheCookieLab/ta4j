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
import org.ta4j.core.indicators.helpers.DistinctConsecutiveValuesOfIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class DistinctConsecutiveValuesOfIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private final double[] closePriceValues = new double[] { 100, 101, 101, 101, 102, 102, 102, 103, 104, 104, 105, 105,
            105, 104, 103, 103, 103, 102, 102, 101, 100, 100 };

    private ClosePriceIndicator closePrice;

    public DistinctConsecutiveValuesOfIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        closePrice = new ClosePriceIndicator(new MockBarSeries(numFunction, closePriceValues));
    }

    @Test
    public void getValue() {
        DistinctConsecutiveValuesOfIndicator subject = new DistinctConsecutiveValuesOfIndicator(closePrice);

        assertNumEquals(100, subject.getValue(0));
        assertNumEquals(101, subject.getValue(1));
        assertNumEquals(102, subject.getValue(2));
        assertNumEquals(103, subject.getValue(3));
        assertNumEquals(104, subject.getValue(4));
        assertNumEquals(105, subject.getValue(5));
        assertNumEquals(104, subject.getValue(6));
        assertNumEquals(103, subject.getValue(7));
        assertNumEquals(102, subject.getValue(8));
        assertNumEquals(101, subject.getValue(9));
        assertNumEquals(100, subject.getValue(10));

        assertNumEquals(100, subject.getValue(11));

    }
}
