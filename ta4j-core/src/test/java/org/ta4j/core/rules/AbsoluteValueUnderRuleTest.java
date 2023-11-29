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
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.FixedDecimalIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

public class AbsoluteValueUnderRuleTest {

    @Test
    public void isSatisfied() {
        BarSeries series = new BaseBarSeries();
        Indicator<Num> first = new FixedDecimalIndicator(series, 2, 5, 5, 7, 5, 4, 2, 10, 15, 11);
        Indicator<Num> second = new FixedDecimalIndicator(series, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Num value = DecimalNum.valueOf(2);

        AbsoluteValueUnderRule rule = new AbsoluteValueUnderRule(first, second, value);

        assertTrue(rule.isSatisfied(0));
        assertFalse(rule.isSatisfied(1));
        assertFalse(rule.isSatisfied(2));
        assertFalse(rule.isSatisfied(3));
        assertTrue(rule.isSatisfied(4));
        assertFalse(rule.isSatisfied(5));
        assertFalse(rule.isSatisfied(6));
        assertFalse(rule.isSatisfied(7));
        assertFalse(rule.isSatisfied(8));
        assertTrue(rule.isSatisfied(9));
    }
}
