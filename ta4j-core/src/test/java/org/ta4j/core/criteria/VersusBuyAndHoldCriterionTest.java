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

import java.time.ZonedDateTime;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;
import static org.ta4j.core.num.NaN.NaN;

import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class VersusBuyAndHoldCriterionTest extends AbstractCriterionTest {

    public VersusBuyAndHoldCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> new VersusBuyAndHoldCriterion((AnalysisCriterion) params[0]), numFunction);
    }

    @Test
    public void calculateOnlyWithGainPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 110, 100, 95, 105);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series),
                Trade.buyAt(3, series), Trade.sellAt(5, series));

        AnalysisCriterion buyAndHold = getCriterion(new GrossReturnCriterion());
        assertNumEquals(1.10 * 1.05 / 1.05, buyAndHold.calculate(series, tradingRecord));
    }

    @Test
    public void calculateOnlyWithLossPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(5, series));

        AnalysisCriterion buyAndHold = getCriterion(new GrossReturnCriterion());
        assertNumEquals(0.95 * 0.7 / 0.7, buyAndHold.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithOnlyOnePosition() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        Position position = new Position(Trade.buyAt(0, series), Trade.sellAt(1, series));

        AnalysisCriterion buyAndHold = getCriterion(new GrossReturnCriterion());
        assertNumEquals((100d / 70) / (100d / 95), buyAndHold.calculate(series, position));
    }

    @Test
    public void calculateWithMostRecentOnePosition() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 50, 90, 110);
        TradingRecord tradingRecord = new BaseTradingRecord();
        tradingRecord.enter(0, ZonedDateTime.now(), numOf(100), numOf(1));
        tradingRecord.exit(1, ZonedDateTime.now(), numOf(50), numOf(1));
        tradingRecord.enter(2, ZonedDateTime.now(), numOf(50), numOf(1));
        tradingRecord.exit(3, ZonedDateTime.now(), numOf(110), numOf(1));

        AnalysisCriterion buyAndHold = getCriterion(new GrossReturnCriterion());
        assertNumEquals((110d / 50) / (110d / 90), buyAndHold.calculate(series, tradingRecord, 1));
    }

    @Test
    public void calculateWithNoPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);

        AnalysisCriterion buyAndHold = getCriterion(new GrossReturnCriterion());
        assertNumEquals(1 / 0.7, buyAndHold.calculate(series, new BaseTradingRecord()));
    }

    @Test
    public void calculateWithAverageProfit() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 130);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, ZonedDateTime.now(), NaN, NaN),
                Trade.sellAt(1, ZonedDateTime.now(), NaN, NaN), Trade.buyAt(2, ZonedDateTime.now(), NaN, NaN),
                Trade.sellAt(5, ZonedDateTime.now(), NaN, NaN));

        AnalysisCriterion buyAndHold = getCriterion(new AverageReturnPerBarCriterion());

        assertNumEquals(Math.pow(95d / 100 * 130d / 100, 1d / 6) / Math.pow(130d / 100, 1d / 6),
                buyAndHold.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithNumberOfBars() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 130);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(5, series));

        AnalysisCriterion buyAndHold = getCriterion(new NumberOfBarsCriterion());

        assertNumEquals(6d / 6d, buyAndHold.calculate(series, tradingRecord));
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = getCriterion(new GrossReturnCriterion());
        assertTrue(criterion.betterThan(numOf(2.0), numOf(1.5)));
        assertFalse(criterion.betterThan(numOf(1.5), numOf(2.0)));
    }
}
