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
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class AverageReturnPerBarCriterionTest extends AbstractCriterionTest {
    private MockBarSeries series;

    public AverageReturnPerBarCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> new AverageReturnPerBarCriterion(), numFunction);
    }

    @Test
    public void calculateOnlyWithGainPositions() {
        series = new MockBarSeries(numFunction, 100d, 105d, 110d, 100d, 95d, 105d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series),
                Trade.buyAt(3, series), Trade.sellAt(5, series));
        AnalysisCriterion averageProfit = getCriterion();
        assertNumEquals(1.0243, averageProfit.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithASimplePosition() {
        series = new MockBarSeries(numFunction, 100d, 105d, 110d, 100d, 95d, 105d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series));
        AnalysisCriterion averageProfit = getCriterion();
        assertNumEquals(numOf(110d / 100).pow(numOf(1d / 3)), averageProfit.calculate(series, tradingRecord));
    }

    @Test
    public void calculateOnlyWithLossPositions() {
        series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(5, series));
        AnalysisCriterion averageProfit = getCriterion();
        assertNumEquals(numOf(95d / 100 * 70d / 100).pow(numOf(1d / 6)),
                averageProfit.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithLosingAShortPositions() {
        series = new MockBarSeries(numFunction, 100d, 105d, 110d, 100d, 95d, 105d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(2, series));
        AnalysisCriterion averageProfit = getCriterion();
        assertNumEquals(numOf(90d / 100).pow(numOf(1d / 3)), averageProfit.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithNoBarsShouldReturn1() {
        series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        AnalysisCriterion averageProfit = getCriterion();
        assertNumEquals(1, averageProfit.calculate(series, new BaseTradingRecord()));
    }

    @Test
    public void calculateWithOnePosition() {
        series = new MockBarSeries(numFunction, 100, 105);
        Position position = new Position(Trade.buyAt(0, series), Trade.sellAt(1, series));
        AnalysisCriterion average = getCriterion();
        assertNumEquals(numOf(105d / 100).pow(numOf(0.5)), average.calculate(series, position));
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = getCriterion();
        assertTrue(criterion.betterThan(numOf(2.0), numOf(1.5)));
        assertFalse(criterion.betterThan(numOf(1.5), numOf(2.0)));
    }
}
