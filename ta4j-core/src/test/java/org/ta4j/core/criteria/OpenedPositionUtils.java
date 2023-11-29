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
package org.ta4j.core.criteria;

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class OpenedPositionUtils {

    public void testCalculateOneOpenPositionShouldReturnExpectedValue(Function<Number, Num> numFunction,
            AnalysisCriterion criterion, Num expectedValue) {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 110, 100, 95, 105);

        Position trade = new Position(Trade.TradeType.BUY);
        trade.operate(0, series.getBar(0).getEndTime(), series.numOf(2.5), series.numOf(1));

        final Num value = criterion.calculate(series, trade);

        assertNumEquals(expectedValue, value);
    }

    public void testCalculateOneOpenPositionShouldReturnExpectedValue(Function<Number, Num> numFunction,
            AnalysisCriterion criterion, int expectedValue) {
        this.testCalculateOneOpenPositionShouldReturnExpectedValue(numFunction, criterion,
                numFunction.apply(expectedValue));
    }
}
