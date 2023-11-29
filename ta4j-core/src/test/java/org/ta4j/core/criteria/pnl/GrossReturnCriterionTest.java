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
package org.ta4j.core.criteria.pnl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractCriterionTest;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class GrossReturnCriterionTest extends AbstractCriterionTest {

    public GrossReturnCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> new GrossReturnCriterion(), numFunction);
    }

    @Test
    public void calculateWithWinningLongPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 110, 100, 95, 105);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series),
                Trade.buyAt(3, series), Trade.sellAt(5, series));

        AnalysisCriterion ret = getCriterion();
        assertNumEquals(1.10 * 1.05, ret.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithLosingLongPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(5, series));

        AnalysisCriterion ret = getCriterion();
        assertNumEquals(0.95 * 0.7, ret.calculate(series, tradingRecord));
    }

    @Test
    public void calculateReturnWithWinningShortPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(1, series),
                Trade.sellAt(2, series), Trade.buyAt(5, series));

        AnalysisCriterion ret = getCriterion();
        assertNumEquals(1.05 * 1.30, ret.calculate(series, tradingRecord));
    }

    @Test
    public void calculateReturnWithLosingShortPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 100, 80, 85, 130);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(1, series),
                Trade.sellAt(2, series), Trade.buyAt(5, series));

        AnalysisCriterion ret = getCriterion();
        assertNumEquals(0.95 * 0.70, ret.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithNoPositionsShouldReturn1() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);

        AnalysisCriterion ret = getCriterion();
        assertNumEquals(1d, ret.calculate(series, new BaseTradingRecord()));
    }

    @Test
    public void calculateWithOpenedPositionShouldReturn1() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        AnalysisCriterion ret = getCriterion();
        Position position = new Position();
        assertNumEquals(1d, ret.calculate(series, position));
        position.operate(0);
        assertNumEquals(1d, ret.calculate(series, position));
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = getCriterion();
        assertTrue(criterion.betterThan(numOf(2.0), numOf(1.5)));
        assertFalse(criterion.betterThan(numOf(1.5), numOf(2.0)));
    }

    @Test
    public void testCalculateOneOpenPositionShouldReturnOne() {
        openedPositionUtils.testCalculateOneOpenPositionShouldReturnExpectedValue(numFunction, getCriterion(), 1);
    }
}
