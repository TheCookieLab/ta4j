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

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

public class ReturnOverMaxDrawdownCriterionTest extends AbstractCriterionTest {

    private AnalysisCriterion rrc;

    public ReturnOverMaxDrawdownCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> new ReturnOverMaxDrawdownCriterion(), numFunction);
    }

    @Before
    public void setUp() {
        this.rrc = getCriterion();
    }

    @Test
    public void rewardRiskRatioCriterion() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 95, 100, 90, 95, 80, 120);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(4, series), Trade.buyAt(5, series), Trade.sellAt(7, series));

        double totalProfit = (105d / 100) * (90d / 95d) * (120d / 95);
        double peak = (105d / 100) * (100d / 95);
        double low = (105d / 100) * (90d / 95) * (80d / 95);

        assertNumEquals(totalProfit / ((peak - low) / peak), rrc.calculate(series, tradingRecord));
    }

    @Test
    public void rewardRiskRatioCriterionOnlyWithGain() {
        MockBarSeries series = new MockBarSeries(numFunction, 1, 2, 3, 6, 8, 20, 3);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(5, series));
        assertTrue(rrc.calculate(series, tradingRecord).isNaN());
    }

    @Test
    public void rewardRiskRatioCriterionWithNoPositions() {
        MockBarSeries series = new MockBarSeries(numFunction, 1, 2, 3, 6, 8, 20, 3);
        assertTrue(rrc.calculate(series, new BaseTradingRecord()).isNaN());
    }

    @Test
    public void withOnePosition() {
        MockBarSeries series = new MockBarSeries(numFunction, 100, 95, 95, 100, 90, 95, 80, 120);
        Position position = new Position(Trade.buyAt(0, series), Trade.sellAt(1, series));

        AnalysisCriterion ratioCriterion = getCriterion();
        assertNumEquals((95d / 100) / ((1d - 0.95d)), ratioCriterion.calculate(series, position));
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = getCriterion();
        assertTrue(criterion.betterThan(numOf(3.5), numOf(2.2)));
        assertFalse(criterion.betterThan(numOf(1.5), numOf(2.7)));
    }

    @Test
    public void testNoDrawDownForTradingRecord() {
        final MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 95, 100, 90, 95, 80, 120);
        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
                Trade.buyAt(2, series), Trade.sellAt(3, series));

        final Num result = rrc.calculate(series, tradingRecord);

        assertNumEquals(NaN.NaN, result);
    }

    @Test
    public void testNoDrawDownForPosition() {
        final MockBarSeries series = new MockBarSeries(numFunction, 100, 105, 95, 100, 90, 95, 80, 120);
        final Position position = new Position(Trade.buyAt(0, series), Trade.sellAt(1, series));

        final Num result = rrc.calculate(series, position);

        assertNumEquals(NaN.NaN, result);
    }
}
