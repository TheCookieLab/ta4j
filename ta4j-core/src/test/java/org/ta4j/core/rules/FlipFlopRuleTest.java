/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
