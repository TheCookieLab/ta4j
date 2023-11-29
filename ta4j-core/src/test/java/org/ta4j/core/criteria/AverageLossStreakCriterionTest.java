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

public class AverageLossStreakCriterionTest extends AbstractCriterionTest {

    private MockBarSeries series;

    public AverageLossStreakCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> new AverageLossStreakCriterion(), numFunction);
    }

    @Test
    public void calculateOnlyWithWinningLongPositions() {
        series = new MockBarSeries(numFunction, 100d, 105d, 110d, 100d, 95d, 105d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series),
                Trade.buyAt(3, series), Trade.sellAt(5, series));
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(0, averageLossStreak.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithASingleWinningLongPosition() {
        series = new MockBarSeries(numFunction, 100d, 105d, 110d, 100d, 95d, 105d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series));
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(numOf(0), averageLossStreak.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithOnlyLosingLongPositions() {
        series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(5, series));
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(numOf(2), averageLossStreak.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithLosingShortPositions() {
        series = new MockBarSeries(numFunction, 100d, 101d, 102d, 103d, 104d, 105d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(1, series),
                Trade.sellAt(2, series), Trade.buyAt(3, series), Trade.sellAt(4, series), Trade.buyAt(5, series));
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(numOf(3), averageLossStreak.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithLosingSingleShortPositions() {
        series = new MockBarSeries(numFunction, 100d, 105d, 110d, 100d, 95d, 105d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(2, series));
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(numOf(1), averageLossStreak.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithOnlyBreakEvenShortPositions() {
        series = new MockBarSeries(numFunction, 100d, 100d, 100d, 100d, 100d, 100d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(1, series),
                Trade.sellAt(2, series), Trade.buyAt(3, series), Trade.sellAt(4, series), Trade.buyAt(5, series));
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(numOf(0), averageLossStreak.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithOnlyBreakEvenLongPositions() {
        series = new MockBarSeries(numFunction, 100d, 100d, 100d, 100d, 100d, 100d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(3, series), Trade.buyAt(4, series), Trade.sellAt(5, series));
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(numOf(0), averageLossStreak.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithMixedPerformanceLongPositions() {
        series = new MockBarSeries(numFunction, 110d, 109d, 107d, 107d, 104d, 100d, 95d, 89d, 82d, 74d, 80d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series), // Loss 1.1
                Trade.buyAt(1, series), Trade.sellAt(2, series), // Loss 1.2
                Trade.buyAt(2, series), Trade.sellAt(3, series), // Breakeven
                Trade.buyAt(3, series), Trade.sellAt(4, series), // Loss 2.1
                Trade.buyAt(4, series), Trade.sellAt(5, series), // Loss 2.2
                Trade.buyAt(5, series), Trade.sellAt(6, series), // Loss 2.3
                Trade.buyAt(6, series), Trade.sellAt(7, series), // Loss 2.4
                Trade.buyAt(7, series), Trade.sellAt(8, series), // Loss 2.5
                Trade.buyAt(8, series), Trade.sellAt(9, series), // Loss 2.6
                Trade.buyAt(9, series), Trade.sellAt(10, series) // Win
        );
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(numOf((2 + 6) / 2), averageLossStreak.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithMixedPerformanceMostRecentLongPositions() {
        series = new MockBarSeries(numFunction, 110d, 109d, 107d, 107d, 104d, 100d, 95d, 89d, 82d, 74d, 80d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series), // Loss 1.1
                Trade.buyAt(1, series), Trade.sellAt(2, series), // Loss 1.2
                Trade.buyAt(2, series), Trade.sellAt(3, series), // Breakeven
                Trade.buyAt(3, series), Trade.sellAt(4, series), // Loss 2.1
                Trade.buyAt(4, series), Trade.sellAt(5, series), // Loss 2.2
                Trade.buyAt(5, series), Trade.sellAt(6, series), // Loss 2.3
                Trade.buyAt(6, series), Trade.sellAt(7, series), // Loss 2.4
                Trade.buyAt(7, series), Trade.sellAt(8, series), // Loss 2.5
                Trade.buyAt(8, series), Trade.sellAt(9, series), // Loss 2.6
                Trade.buyAt(9, series), Trade.sellAt(10, series) // Win
        );
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(numOf(6), averageLossStreak.calculate(series, tradingRecord, 7));
    }

    @Test
    public void calculateWithMixedPerformanceShortPositions() {
        series = new MockBarSeries(numFunction, 100d, 101d, 102d, 102d, 104d, 100d, 99d, 100d, 102d, 105d, 108d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(1, series), // Loss 1.1
                Trade.sellAt(1, series), Trade.buyAt(2, series), // Loss 1.2
                Trade.sellAt(2, series), Trade.buyAt(3, series), // Breakeven
                Trade.sellAt(3, series), Trade.buyAt(4, series), // Loss 2.1
                Trade.sellAt(4, series), Trade.buyAt(5, series), // Win 1.1
                Trade.sellAt(5, series), Trade.buyAt(6, series), // Win 1.2
                Trade.sellAt(6, series), Trade.buyAt(7, series), // Loss 3.1
                Trade.sellAt(7, series), Trade.buyAt(8, series), // Loss 3.2
                Trade.sellAt(8, series), Trade.buyAt(9, series), // Loss 3.3
                Trade.sellAt(9, series), Trade.buyAt(10, series) // Loss 3.4
        );
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(numOf((2 + 1 + 4) / 3), averageLossStreak.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithMixedPerformanceShortMostRecentPositions() {
        series = new MockBarSeries(numFunction, 100d, 101d, 102d, 102d, 104d, 100d, 99d, 100d, 102d, 105d, 108d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.sellAt(0, series), Trade.buyAt(1, series), // Loss 1.1
                Trade.sellAt(1, series), Trade.buyAt(2, series), // Loss 1.2
                Trade.sellAt(2, series), Trade.buyAt(3, series), // Breakeven
                Trade.sellAt(3, series), Trade.buyAt(4, series), // Loss 2.1
                Trade.sellAt(4, series), Trade.buyAt(5, series), // Win 1.1
                Trade.sellAt(5, series), Trade.buyAt(6, series), // Win 1.2
                Trade.sellAt(6, series), Trade.buyAt(7, series), // Loss 3.1
                Trade.sellAt(7, series), Trade.buyAt(8, series), // Loss 3.2
                Trade.sellAt(8, series), Trade.buyAt(9, series), // Loss 3.3
                Trade.sellAt(9, series), Trade.buyAt(10, series) // Loss 3.4
        );
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(numOf((1 + 4) / 2), averageLossStreak.calculate(series, tradingRecord, 7));
    }

    @Test
    public void calculateWithNoBarsShouldReturn1() {
        series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(0, averageLossStreak.calculate(series, new BaseTradingRecord()));
    }

    @Test
    public void calculateWithOneWinningLongPosition() {
        series = new MockBarSeries(numFunction, 100, 105);
        Position position = new Position(Trade.buyAt(0, series), Trade.sellAt(1, series));
        AnalysisCriterion averageLossStreak = getCriterion();
        assertNumEquals(numOf(0), averageLossStreak.calculate(series, position));
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = getCriterion();
        assertTrue(criterion.betterThan(numOf(2.0), numOf(1.5)));
        assertFalse(criterion.betterThan(numOf(1.5), numOf(2.0)));
    }
}
