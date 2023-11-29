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

import org.junit.Before;
import org.junit.Test;

public class JustOnceRuleTest {

    private JustOnceRule rule;

    @Before
    public void setUp() {
        rule = new JustOnceRule();
    }

    @Test
    public void isSatisfied() {
        assertTrue(rule.isSatisfied(10));
        assertFalse(rule.isSatisfied(11));
        assertFalse(rule.isSatisfied(12));
        assertFalse(rule.isSatisfied(13));
        assertFalse(rule.isSatisfied(14));
    }

    @Test
    public void isSatisfiedInReverseOrder() {
        assertTrue(rule.isSatisfied(5));
        assertFalse(rule.isSatisfied(2));
        assertFalse(rule.isSatisfied(1));
        assertFalse(rule.isSatisfied(0));
    }

    @Test
    public void isSatisfiedWithInnerSatisfiedRule() {
        JustOnceRule rule = new JustOnceRule(new BooleanRule(true));
        assertTrue(rule.isSatisfied(5));
        assertFalse(rule.isSatisfied(2));
        assertFalse(rule.isSatisfied(1));
        assertFalse(rule.isSatisfied(0));
    }

    @Test
    public void isSatisfiedWithInnerNonSatisfiedRule() {
        JustOnceRule rule = new JustOnceRule(new BooleanRule(false));
        assertFalse(rule.isSatisfied(5));
        assertFalse(rule.isSatisfied(2));
        assertFalse(rule.isSatisfied(1));
        assertFalse(rule.isSatisfied(0));
    }

    @Test
    public void isSatisfiedWithInnerRule() {
        JustOnceRule rule = new JustOnceRule(new FixedRule(1, 3, 5));
        assertFalse(rule.isSatisfied(0));
        assertTrue(rule.isSatisfied(1));
        assertFalse(rule.isSatisfied(2));
        assertFalse(rule.isSatisfied(3));
        assertFalse(rule.isSatisfied(4));
        assertFalse(rule.isSatisfied(5));
        assertFalse(rule.isSatisfied(1));
    }
}
