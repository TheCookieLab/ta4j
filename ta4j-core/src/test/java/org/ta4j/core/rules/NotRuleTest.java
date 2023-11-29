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
import org.ta4j.core.Rule;

public class NotRuleTest {

    private Rule satisfiedRule;
    private Rule unsatisfiedRule;

    @Before
    public void setUp() {
        satisfiedRule = new BooleanRule(true);
        unsatisfiedRule = new BooleanRule(false);
    }

    @Test
    public void isSatisfied() {
        assertFalse(satisfiedRule.negation().isSatisfied(0));
        assertTrue(unsatisfiedRule.negation().isSatisfied(0));

        assertFalse(satisfiedRule.negation().isSatisfied(10));
        assertTrue(unsatisfiedRule.negation().isSatisfied(10));
    }
}
