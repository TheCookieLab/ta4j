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

public class MinCompareIndicatorTest {

    @Test
    public void calculate() {
        BarSeries series = new BaseBarSeries();

        FixedDecimalIndicator fd1 = new FixedDecimalIndicator(series, 1, 1, 2, 4, 4, 6, 7, 7, 9, 9);
        FixedDecimalIndicator fd2 = new FixedDecimalIndicator(series, 0, 2, 3, 3, 5, 5, 6, 8, 8, 10);

        MinCompareIndicator minCompare = new MinCompareIndicator(series, fd1, fd2);

        assertEquals(DecimalNum.valueOf(0), minCompare.calculate(0));
        assertEquals(DecimalNum.valueOf(1.0), minCompare.calculate(1));
        assertEquals(DecimalNum.valueOf(2.0), minCompare.calculate(2));
        assertEquals(DecimalNum.valueOf(3.0), minCompare.calculate(3));
        assertEquals(DecimalNum.valueOf(4.0), minCompare.calculate(4));
        assertEquals(DecimalNum.valueOf(5.0), minCompare.calculate(5));
        assertEquals(DecimalNum.valueOf(6.0), minCompare.calculate(6));
        assertEquals(DecimalNum.valueOf(7.0), minCompare.calculate(7));
        assertEquals(DecimalNum.valueOf(8.0), minCompare.calculate(8));
        assertEquals(DecimalNum.valueOf(9.0), minCompare.calculate(9));
    }
}
