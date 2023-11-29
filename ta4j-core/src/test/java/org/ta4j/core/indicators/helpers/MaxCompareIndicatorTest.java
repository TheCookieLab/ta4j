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

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.num.DecimalNum;

public class MaxCompareIndicatorTest {

    @Test
    public void calculate() {
        BarSeries series = new BaseBarSeries();

        FixedDecimalIndicator fd1 = new FixedDecimalIndicator(series, 1, 1, 3, 4, 3, 6, 7, 6, 9, 1);
        FixedDecimalIndicator fd2 = new FixedDecimalIndicator(series, 0, 2, 3, 3, 5, 2, 6, 8, 4, 10);

        MaxCompareIndicator maxCompare = new MaxCompareIndicator(series, fd1, fd2);

        assertEquals(DecimalNum.valueOf(1.0), maxCompare.calculate(0));
        assertEquals(DecimalNum.valueOf(2.0), maxCompare.calculate(1));
        assertEquals(DecimalNum.valueOf(3.0), maxCompare.calculate(2));
        assertEquals(DecimalNum.valueOf(4.0), maxCompare.calculate(3));
        assertEquals(DecimalNum.valueOf(5.0), maxCompare.calculate(4));
        assertEquals(DecimalNum.valueOf(6.0), maxCompare.calculate(5));
        assertEquals(DecimalNum.valueOf(7.0), maxCompare.calculate(6));
        assertEquals(DecimalNum.valueOf(8.0), maxCompare.calculate(7));
        assertEquals(DecimalNum.valueOf(9.0), maxCompare.calculate(8));
        assertEquals(DecimalNum.valueOf(10.0), maxCompare.calculate(9));
    }
}
