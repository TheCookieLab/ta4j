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

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

public class ExpectedShortfallCriterionTest extends AbstractCriterionTest {
    private MockBarSeries series;

    public ExpectedShortfallCriterionTest(Function<Number, Num> numFunction) {
        // LOG returns requre DoubleNum implementation
        super((params) -> new ExpectedShortfallCriterion(0.95), DoubleNum::valueOf);
    }

    @Test
    public void calculateOnlyWithGainPositions() {
        series = new MockBarSeries(numFunction, 100d, 105d, 106d, 107d, 108d, 115d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series),
                Trade.buyAt(3, series), Trade.sellAt(5, series));
        AnalysisCriterion varCriterion = getCriterion();
        assertNumEquals(numOf(0.0), varCriterion.calculate(series, tradingRecord));
    }

    @Test
    public void calculateWithASimplePosition() {
        // if only one position in tail, VaR = ES
        series = new MockBarSeries(numFunction, 100d, 104d, 90d, 100d, 95d, 105d);
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series));
        AnalysisCriterion esCriterion = getCriterion();
        assertNumEquals(numOf(Math.log(90d / 104)), esCriterion.calculate(series, tradingRecord));
    }

    @Test
    public void calculateOnlyWithLossPosition() {
        // regularly decreasing prices
        List<Double> prices = IntStream.rangeClosed(1, 100)
                .asDoubleStream()
                .boxed()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
        series = new MockBarSeries(numFunction, prices);
        Position position = new Position(Trade.buyAt(series.getBeginIndex(), series),
                Trade.sellAt(series.getEndIndex(), series));
        AnalysisCriterion esCriterion = getCriterion();
        assertNumEquals(numOf(-0.35835189384561106), esCriterion.calculate(series, position));
    }

    @Test
    public void calculateWithNoBarsShouldReturn0() {
        series = new MockBarSeries(numFunction, 100d, 95d, 100d, 80d, 85d, 70d);
        AnalysisCriterion varCriterion = getCriterion();
        assertNumEquals(numOf(0), varCriterion.calculate(series, new BaseTradingRecord()));
    }

    @Test
    public void calculateWithBuyAndHold() {
        series = new MockBarSeries(numFunction, 100d, 99d);
        Position position = new Position(Trade.buyAt(0, series), Trade.sellAt(1, series));
        AnalysisCriterion varCriterion = getCriterion();
        assertNumEquals(numOf(Math.log(99d / 100)), varCriterion.calculate(series, position));
    }

    @Test
    public void betterThan() {
        AnalysisCriterion criterion = getCriterion();
        assertTrue(criterion.betterThan(numOf(-0.1), numOf(-0.2)));
        assertFalse(criterion.betterThan(numOf(-0.1), numOf(0.0)));
    }
}
