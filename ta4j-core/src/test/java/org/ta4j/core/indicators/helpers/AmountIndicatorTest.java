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

import static junit.framework.TestCase.assertEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class AmountIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private AmountIndicator amountIndicator;

    BarSeries barSeries;

    public AmountIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        barSeries = new MockBarSeries(numFunction);
        amountIndicator = new AmountIndicator(barSeries);
    }

    @Test
    public void indicatorShouldRetrieveBarAmountPrice() {
        for (int i = 0; i < 10; i++) {
            assertEquals(amountIndicator.getValue(i), barSeries.getBar(i).getAmount());
        }
    }
}
