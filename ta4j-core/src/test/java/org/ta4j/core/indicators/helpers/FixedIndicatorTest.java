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

import static org.ta4j.core.TestUtils.assertNumEquals;

import org.junit.Assert;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

public class FixedIndicatorTest {

    @Test
    public void getValueOnFixedDecimalIndicator() {
        BarSeries series = new BaseBarSeries();
        FixedDecimalIndicator fixedDecimalIndicator = new FixedDecimalIndicator(series, 13.37, 42, -17);
        assertNumEquals(13.37, fixedDecimalIndicator.getValue(0));
        assertNumEquals(42, fixedDecimalIndicator.getValue(1));
        assertNumEquals(-17, fixedDecimalIndicator.getValue(2));

        fixedDecimalIndicator = new FixedDecimalIndicator(series, "3.0", "-123.456", "0.0");
        assertNumEquals("3.0", fixedDecimalIndicator.getValue(0));
        assertNumEquals("-123.456", fixedDecimalIndicator.getValue(1));
        assertNumEquals("0.0", fixedDecimalIndicator.getValue(2));

    }

    @Test
    public void getValueOnFixedBooleanIndicator() {
        BarSeries series = new BaseBarSeries();
        FixedBooleanIndicator fixedBooleanIndicator = new FixedBooleanIndicator(series, false, false, true, false,
                true);
        Assert.assertFalse(fixedBooleanIndicator.getValue(0));
        Assert.assertFalse(fixedBooleanIndicator.getValue(1));
        Assert.assertTrue(fixedBooleanIndicator.getValue(2));
        Assert.assertFalse(fixedBooleanIndicator.getValue(3));
        Assert.assertTrue(fixedBooleanIndicator.getValue(4));
    }
}
