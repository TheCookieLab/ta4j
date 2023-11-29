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

import static junit.framework.TestCase.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Strategy;
import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;
import org.ta4j.core.rules.BooleanRule;
import org.ta4j.core.rules.FixedRule;

public class AbstractAnalysisCriterionTest extends AbstractCriterionTest {

    private Strategy alwaysStrategy;

    private Strategy buyAndHoldStrategy;

    private List<Strategy> strategies;

    public AbstractAnalysisCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> new GrossReturnCriterion(), numFunction);
    }

    @Before
    public void setUp() {
        alwaysStrategy = new BaseStrategy(BooleanRule.TRUE, BooleanRule.TRUE);
        buyAndHoldStrategy = new BaseStrategy(new FixedRule(0), new FixedRule(4));
        strategies = new ArrayList<Strategy>();
        strategies.add(alwaysStrategy);
        strategies.add(buyAndHoldStrategy);
    }

    @Test
    public void bestShouldBeAlwaysOperateOnProfit() {
        MockBarSeries series = new MockBarSeries(numFunction, 6.0, 9.0, 6.0, 6.0);
        BarSeriesManager manager = new BarSeriesManager(series);
        Strategy bestStrategy = getCriterion().chooseBest(manager, TradeType.BUY, strategies);
        assertEquals(alwaysStrategy, bestStrategy);
    }

    @Test
    public void bestShouldBeBuyAndHoldOnLoss() {
        MockBarSeries series = new MockBarSeries(numFunction, 6.0, 3.0, 6.0, 6.0);
        BarSeriesManager manager = new BarSeriesManager(series);
        Strategy bestStrategy = getCriterion().chooseBest(manager, TradeType.BUY, strategies);
        assertEquals(buyAndHoldStrategy, bestStrategy);
    }

    @Test
    public void toStringMethod() {
        AbstractAnalysisCriterion c1 = new AverageReturnPerBarCriterion();
        assertEquals("Average Return Per Bar", c1.toString());
        AbstractAnalysisCriterion c2 = new EnterAndHoldReturnCriterion();
        assertEquals("Enter And Hold Return", c2.toString());
        AbstractAnalysisCriterion c3 = new ReturnOverMaxDrawdownCriterion();
        assertEquals("Return Over Max Drawdown", c3.toString());
    }

}
