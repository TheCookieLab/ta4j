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
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class MaximumDrawdownCriterionTest extends AbstractCriterionTest {

    public MaximumDrawdownCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> new MaximumDrawdownCriterion(), numFunction);
    }

    @Test
    public void calculateWithNoTrades() {
        MockBarSeries series = new MockBarSeries(numFunction, 1, 2, 3, 6, 5, 20, 3);
        AnalysisCriterion mdd = getCriterion();

        assertNumEquals(0d, mdd.calculate(series, new BaseTradingRecord()));
    }

    @Test
    public void calculateWithOnlyGains() {
        MockBarSeries series = new MockBarSeries(numFunction, 1, 2, 3, 6, 8, 20, 3);
        AnalysisCriterion mdd = getCriterion();
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(5, series));

        assertNumEquals(0d, mdd.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithGainsAndLosses() {
        MockBarSeries series = new MockBarSeries(numFunction, 1, 2, 3, 6, 5, 20, 3);
        AnalysisCriterion mdd = getCriterion();
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(3, series), Trade.sellAt(4, series), Trade.buyAt(5, series), Trade.sellAt(6, series));

        assertNumEquals(.875d, mdd.calculate(series, tradingRecord));

    }

    @Test
    public void calculateWithNullSeriesSizeShouldReturn0() {
        MockBarSeries series = new MockBarSeries(numFunction, new double[] {});
        AnalysisCriterion mdd = getCriterion();
        assertNumEquals(0d, mdd.calculate(series, new BaseTradingRecord()));
    }

    @Test
    public void withTradesThatSellBeforeBuying() {
        MockBarSeries series = new MockBarSeries(numFunction, 2, 1, 3, 5, 6, 3, 20);
        AnalysisCriterion mdd = getCriterion();
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(3, series), Trade.sellAt(4, series), Trade.sellAt(5, series), Trade.buyAt(6, series));
        assertNumEquals(3.8d, mdd.calculate(series, tradingRecord));
    }

    @Test
    public void withSimpleTrades() {
        MockBarSeries series = new MockBarSeries(numFunction, 1, 10, 5, 6, 1);
        AnalysisCriterion mdd = getCriterion();
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(1, series), Trade.sellAt(2, series), Trade.buyAt(2, series), Trade.sellAt(3, series),
                Trade.buyAt(3, series), Trade.sellAt(4, series));
        assertNumEquals(.9d, mdd.calculate(series, tradingRecord));
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = getCriterion();
        assertTrue(criterion.betterThan(numOf(0.9), numOf(1.5)));
        assertFalse(criterion.betterThan(numOf(1.2), numOf(0.4)));
    }
}
