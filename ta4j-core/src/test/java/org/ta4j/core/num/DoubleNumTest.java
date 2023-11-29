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
package org.ta4j.core.num;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DoubleNumTest {

    @Test
    public void testEqualsDoubleNumWithDecimalNum() {
        final DecimalNum decimalNum = DecimalNum.valueOf(3.0);
        final DoubleNum doubleNum = DoubleNum.valueOf(3.0);

        assertFalse(doubleNum.equals(decimalNum));
    }

    @Test
    public void testZeroEquals() {
        final Num num1 = DoubleNum.valueOf(-0.0);
        final Num num2 = DoubleNum.valueOf(0.0);

        assertTrue(num1.isEqual(num2));
    }
}
