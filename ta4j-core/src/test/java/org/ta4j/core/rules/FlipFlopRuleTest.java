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

public class FlipFlopRuleTest {

    @Test
    public void isSatisfiedStartingWithTrue() {
        FlipFlopRule flipFlopRule = new FlipFlopRule();
        assertTrue(flipFlopRule.isSatisfied(0));
        assertFalse(flipFlopRule.isSatisfied(1));
        assertTrue(flipFlopRule.isSatisfied(2));
        assertFalse(flipFlopRule.isSatisfied(9));
        assertTrue(flipFlopRule.isSatisfied(9));
    }

    @Test
    public void isSatisfiedStartingWithFalse() {
        FlipFlopRule flipFlopRule = new FlipFlopRule(false);
        assertFalse(flipFlopRule.isSatisfied(0));
        assertTrue(flipFlopRule.isSatisfied(1));
        assertFalse(flipFlopRule.isSatisfied(2));
        assertTrue(flipFlopRule.isSatisfied(3));
        assertFalse(flipFlopRule.isSatisfied(4));
        assertTrue(flipFlopRule.isSatisfied(5));
        assertFalse(flipFlopRule.isSatisfied(4));
        assertTrue(flipFlopRule.isSatisfied(3));
        assertFalse(flipFlopRule.isSatisfied(2));
        assertTrue(flipFlopRule.isSatisfied(1));
        assertFalse(flipFlopRule.isSatisfied(0));

    }
}
