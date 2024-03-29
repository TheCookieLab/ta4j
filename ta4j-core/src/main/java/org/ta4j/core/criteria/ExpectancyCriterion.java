/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
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

import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.pnl.AverageLossCriterion;
import org.ta4j.core.criteria.pnl.AverageProfitCriterion;
import org.ta4j.core.num.Num;

/**
 * Expectancy criterion (Kelly Criterion).
 *
 * Measures the positive or negative expectancy. The higher the positive number,
 * the better a winning expectation. A negative number means there is only
 * losing expectations.
 *
 * @see <a href=
 *      "https://www.straightforex.com/advanced-forex-course/money-management/two-important-things-to-be-considered/">https://www.straightforex.com/advanced-forex-course/money-management/two-important-things-to-be-considered/</a>
 *
 */
public class ExpectancyCriterion extends AbstractAnalysisCriterion {

    private final AverageProfitCriterion averageProfitCriterion = new AverageProfitCriterion();
    private final AverageLossCriterion averageLossCriterion = new AverageLossCriterion();
    private final NumberOfPositionsCriterion numberOfPositionsCriterion = new NumberOfPositionsCriterion();
    private final NumberOfWinningPositionsCriterion numberOfWinningPositionsCriterion = new NumberOfWinningPositionsCriterion();

    @Override
    public Num calculate(BarSeries series, Position position) {
        Num averageProfit = averageProfitCriterion.calculate(series, position);
        Num averageLoss = averageLossCriterion.calculate(series, position);
        Num numberOfPositions = numberOfPositionsCriterion.calculate(series, position);
        Num numberOfWinningPositions = numberOfWinningPositionsCriterion.calculate(series, position);
        return calculate(series, averageProfit, averageLoss, numberOfWinningPositions, numberOfPositions);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        Num averageProfit = averageProfitCriterion.calculate(series, tradingRecord, mostRecentPositions);
        Num averageLoss = averageLossCriterion.calculate(series, tradingRecord, mostRecentPositions);
        Num numberOfPositions = numberOfPositionsCriterion.calculate(series, tradingRecord, mostRecentPositions);
        Num numberOfWinningPositions = numberOfWinningPositionsCriterion.calculate(series, tradingRecord,
                mostRecentPositions);
        return calculate(series, averageProfit, averageLoss, numberOfWinningPositions, numberOfPositions);
    }

    /**
     * The higher the criterion value, the better.
     */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

    private Num calculate(BarSeries series, Num averageProfit, Num averageLoss, Num numberOfWinningPositions,
            Num numberOfAllPositions) {
        Num one = series.numOf(1);
        if (numberOfAllPositions.isZero() || averageProfit.isZero()) {
            return series.numOf(0);
        }

        Num probabiltyToWinOnePosition = numberOfWinningPositions.dividedBy(numberOfAllPositions);

        if (averageLoss.isZero()) {
            return one.min(probabiltyToWinOnePosition.multipliedBy(averageProfit));
        }
        // Expectancy = ((1 + AW/AL) * ProbabilityToWinOnePosition) - 1
        Num profitLossRatio = averageProfit.dividedBy(averageLoss).abs();
        return (one.plus(profitLossRatio)).multipliedBy(probabiltyToWinOnePosition).minus(one);
    }

}
