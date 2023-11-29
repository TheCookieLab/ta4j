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

import org.ta4j.core.indicators.helpers.UnstableIndicator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

public class UnstableIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private int unstablePeriod;
    private UnstableIndicator unstableIndicator;

    public UnstableIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        unstablePeriod = 5;
        unstableIndicator = new UnstableIndicator(new ClosePriceIndicator(new MockBarSeries(numFunction)),
                unstablePeriod);
    }

    @Test
    public void indicatorReturnsNanBeforeUnstablePeriod() {
        for (int i = 0; i < unstablePeriod; i++) {
            assertEquals(unstableIndicator.getValue(i), NaN.NaN);
        }
    }

    @Test
    public void indicatorNotReturnsNanAfterUnstablePeriod() {
        for (int i = unstablePeriod; i < 10; i++) {
            assertNotEquals(unstableIndicator.getValue(i), NaN.NaN);
        }
    }

}
