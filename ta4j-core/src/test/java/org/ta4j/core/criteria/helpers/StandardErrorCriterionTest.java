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
package org.ta4j.core.criteria.helpers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractCriterionTest;
import org.ta4j.core.criteria.pnl.ProfitLossCriterion;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class StandardErrorCriterionTest extends AbstractCriterionTest {

    public StandardErrorCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> new StandardErrorCriterion((AnalysisCriterion) params[0]), numFunction);
    }

    @Test
    public void calculateStandardErrorPnL() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 110, 100, 95, 105);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series, series.numOf(1)),
                Trade.sellAt(2, series, series.numOf(1)), Trade.buyAt(3, series, series.numOf(1)),
                Trade.sellAt(5, series, series.numOf(1)));

        AnalysisCriterion criterion = getCriterion(new ProfitLossCriterion());
        assertNumEquals(1.7677669529663687, criterion.calculate(series, tradingRecord));
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = getCriterion(new ProfitLossCriterion());
        assertFalse(criterion.betterThan(numOf(5000), numOf(4500)));
        assertTrue(criterion.betterThan(numOf(4500), numOf(5000)));
    }

    @Test
    public void testCalculateOneOpenPositionShouldReturnZero() {
        openedPositionUtils.testCalculateOneOpenPositionShouldReturnExpectedValue(numFunction,
                getCriterion(new ProfitLossCriterion()), 0);
    }

}
