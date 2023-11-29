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
import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class EnterAndHoldReturnCriterionTest extends AbstractCriterionTest {

    public EnterAndHoldReturnCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> params.length == 0 ? new EnterAndHoldReturnCriterion()
                : new EnterAndHoldReturnCriterion((TradeType) params[0]), numFunction);
    }

    @Test
    public void calculateOnlyWithGainPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 110, 100, 95, 105);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series),
                Trade.buyAt(3, series), Trade.sellAt(5, series));

        AnalysisCriterion buyAndHold = getCriterion();
        assertNumEquals(1.05, buyAndHold.calculate(series, tradingRecord));

        AnalysisCriterion sellAndHold = getCriterion(TradeType.SELL);
        assertNumEquals(0.95, sellAndHold.calculate(series, tradingRecord));
    }

    @Test
    public void calculateOnlyWithLossPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(5, series));

        AnalysisCriterion buyAndHold = getCriterion();
        assertNumEquals(0.7, buyAndHold.calculate(series, tradingRecord));

        AnalysisCriterion sellAndHold = getCriterion(TradeType.SELL);
        assertNumEquals(1.3, sellAndHold.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithNoPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 100, 80, 85, 70);

        AnalysisCriterion buyAndHold = getCriterion();
        assertNumEquals(0.7, buyAndHold.calculate(series, new BaseTradingRecord()));

        AnalysisCriterion sellAndHold = getCriterion(TradeType.SELL);
        assertNumEquals(1.3, sellAndHold.calculate(series, new BaseTradingRecord()));
    }

    @Test
    public void calculateWithOnePositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 105);
        Position position = new Position(Trade.buyAt(0, series), Trade.sellAt(1, series));
        AnalysisCriterion buyAndHold = getCriterion();
        assertNumEquals(105d / 100, buyAndHold.calculate(series, position));

        AnalysisCriterion sellAndHold = getCriterion(TradeType.SELL);
        assertNumEquals(0.95, sellAndHold.calculate(series, position));
    }

    @Test
    public void betterThan() {
        AnalysisCriterion buyAndHold = getCriterion();
        assertTrue(buyAndHold.betterThan(numOf(1.3), numOf(1.1)));
        assertFalse(buyAndHold.betterThan(numOf(0.6), numOf(0.9)));

        AnalysisCriterion sellAndHold = getCriterion(TradeType.SELL);
        assertTrue(sellAndHold.betterThan(numOf(1.3), numOf(1.1)));
        assertFalse(sellAndHold.betterThan(numOf(0.6), numOf(0.9)));
    }
}
