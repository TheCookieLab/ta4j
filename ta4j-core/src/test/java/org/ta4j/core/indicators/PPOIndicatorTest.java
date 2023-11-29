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

public class PPOIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private ClosePriceIndicator closePriceIndicator;

    public PPOIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        BarSeries series = new MockBarSeries(numFunction, 22.27, 22.19, 22.08, 22.17, 22.18, 22.13, 22.23, 22.43, 22.24,
                22.29, 22.15, 22.39, 22.38, 22.61, 23.36, 24.05, 23.75, 23.83, 23.95, 23.63, 23.82, 23.87, 23.65, 23.19,
                23.10, 23.33, 22.68, 23.10, 21.40, 20.17);
        closePriceIndicator = new ClosePriceIndicator(series);
    }

    @Test
    public void getValueWithEma10AndEma20() {
        PPOIndicator ppo = new PPOIndicator(closePriceIndicator, 10, 20);

        assertNumEquals(1.6778, ppo.getValue(21));
        assertNumEquals(1.5669, ppo.getValue(22));
        assertNumEquals(1.2884, ppo.getValue(23));

        assertNumEquals(-0.2925, ppo.getValue(28));
        assertNumEquals(-1.3088, ppo.getValue(29));
    }
}
