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

import java.math.RoundingMode;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class TotalProfitCriterionTest extends AbstractCriterionTest {

    public TotalProfitCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> new TotalProfitCriterion(), numFunction);
    }

    @Test
    public void calculateOnlyWithProfitPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 110, 120, 130, 150, 160);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series),
                Trade.buyAt(3, series), Trade.sellAt(5, series));

        AnalysisCriterion totalProfit = getCriterion();
        assertNumEquals(1.48, totalProfit.calculate(series, tradingRecord).round(2, RoundingMode.HALF_EVEN));
    }

    @Test
    public void calculateWithMixedPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 110, 80, 130, 150, 160);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series),
                Trade.buyAt(3, series), Trade.sellAt(5, series));

        AnalysisCriterion totalProfit = getCriterion();
        assertNumEquals(0.98, totalProfit.calculate(series, tradingRecord).round(2, RoundingMode.HALF_EVEN));
    }

    @Test
    public void calculateWith30PercentWinRateFor5REachAnd70PercentLoseRateFor1REach() {
        MockBarSeries series = new MockBarSeries(numFunction, 1, 6, 1, 6, 1, 6, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2,
                1);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(3, series), Trade.buyAt(4, series), Trade.sellAt(5, series),
                Trade.buyAt(6, series), Trade.sellAt(7, series), Trade.buyAt(8, series), Trade.sellAt(9, series),
                Trade.buyAt(10, series), Trade.sellAt(11, series), Trade.buyAt(12, series), Trade.sellAt(13, series),
                Trade.buyAt(14, series), Trade.sellAt(15, series), Trade.buyAt(16, series), Trade.sellAt(17, series),
                Trade.buyAt(18, series), Trade.sellAt(19, series));

        AnalysisCriterion totalProfit = getCriterion();
        assertNumEquals(1.6875, totalProfit.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithMultipleBreakEvenPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 1, 1, 1, 1, 1, 1, 1, 1);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(3, series), Trade.buyAt(4, series), Trade.sellAt(5, series),
                Trade.buyAt(6, series), Trade.sellAt(7, series));

        AnalysisCriterion totalProfit = getCriterion();
        assertNumEquals(1, totalProfit.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithMostRecentBreakEvenPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 1, 1, 1, 1, 1, 1, 1, 5);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(3, series), Trade.buyAt(4, series), Trade.sellAt(5, series),
                Trade.buyAt(6, series), Trade.sellAt(7, series));

        AnalysisCriterion totalProfit = getCriterion();
        assertNumEquals(5, totalProfit.calculate(series, tradingRecord, 2));
    }

    @Test
    public void calculateOnlyWithLossPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 80, 70, 60, 50);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(5, series));

        AnalysisCriterion totalProfit = getCriterion();
        assertNumEquals(0.59375, totalProfit.calculate(series, tradingRecord));
    }

    @Test
    public void calculateOnlyWithMostRecentLossPosition() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 80, 70, 60, 50);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(5, series));

        AnalysisCriterion totalProfit = getCriterion();
        assertNumEquals(0.625, totalProfit.calculate(series, tradingRecord, 1));
    }

    @Test
    public void calculateProfitWithShortPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 160, 140, 120, 100, 80, 60);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(1, series),
                Trade.sellAt(2, series), Trade.buyAt(5, series));

        AnalysisCriterion totalProfit = getCriterion();
        assertNumEquals(2.29, totalProfit.calculate(series, tradingRecord).round(2, RoundingMode.HALF_EVEN));
    }

    @Test
    public void calculateProfitWithMixedShortPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 160, 200, 120, 100, 80, 60);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(1, series),
                Trade.sellAt(2, series), Trade.buyAt(5, series));

        AnalysisCriterion totalProfit = getCriterion();
        assertNumEquals(1.6, totalProfit.calculate(series, tradingRecord));
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
