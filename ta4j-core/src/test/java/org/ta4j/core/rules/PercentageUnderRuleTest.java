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
package org.ta4j.core.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.helpers.FixedDecimalIndicator;
import org.ta4j.core.num.DoubleNum;

public class PercentageUnderRuleTest {

    @Test
    public void isSatisfied() {
        BarSeries series = new BaseBarSeries(0d, 1d, 2d, 3d, 4d, 5d);
        FixedDecimalIndicator fd1 = new FixedDecimalIndicator(series, 100, 90, 110, 100, 20, 21);
        FixedDecimalIndicator fd2 = new FixedDecimalIndicator(series, 109, 80, 100, 110, 18, 18);

        PercentageUnderRule rule = new PercentageUnderRule(fd1, fd2, DoubleNum.valueOf(0.1));

        assertTrue(rule.isSatisfied(0));
        assertFalse(rule.isSatisfied(1));
        assertTrue(rule.isSatisfied(2));
        assertTrue(rule.isSatisfied(3));
        assertFalse(rule.isSatisfied(4));
        assertFalse(rule.isSatisfied(5));

    }
}
