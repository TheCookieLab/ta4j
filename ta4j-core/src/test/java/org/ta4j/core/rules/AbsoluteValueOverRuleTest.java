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

public class AbsoluteValueOverRuleTest {

    @Test
    public void isSatisfied() {
        BarSeries series = new BaseBarSeries();
        Indicator<Num> first = new FixedDecimalIndicator(series, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Indicator<Num> second = new FixedDecimalIndicator(series, 2, 5, 5, 7, 5, 4, 2, 10, 15, 11);
        Num value = DecimalNum.valueOf(2);

        AbsoluteValueOverRule rule = new AbsoluteValueOverRule(first, second, value);

        assertFalse(rule.isSatisfied(0));
        assertTrue(rule.isSatisfied(1));
        assertFalse(rule.isSatisfied(2));
        assertTrue(rule.isSatisfied(3));
        assertFalse(rule.isSatisfied(4));
        assertFalse(rule.isSatisfied(5));
        assertTrue(rule.isSatisfied(6));
        assertFalse(rule.isSatisfied(7));
        assertTrue(rule.isSatisfied(8));
        assertFalse(rule.isSatisfied(9));
    }
}