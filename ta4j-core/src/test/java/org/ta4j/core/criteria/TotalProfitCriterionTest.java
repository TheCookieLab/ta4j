/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2022 Ta4j Organization & respective
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
