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
package org.ta4j.core.criteria.helpers;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.criteria.NumberOfPositionsCriterion;
import org.ta4j.core.num.Num;

/**
 * Variance criterion.
 * 
 * <p>
 * Calculates the standard deviation for a Criterion.
 */
public class VarianceCriterion extends AbstractAnalysisCriterion {

    private final AnalysisCriterion criterion;
    private final NumberOfPositionsCriterion numberOfPositionsCriterion = new NumberOfPositionsCriterion();

    /**
     * Constructor.
     * 
     * @param criterion the criterion from which the "variance" is calculated
     */
    public VarianceCriterion(AnalysisCriterion criterion) {
        this.criterion = criterion;
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        Num criterionValue = criterion.calculate(series, position);
        Num numberOfPositions = numberOfPositionsCriterion.calculate(series, position);

        Num variance = series.numOf(0);
        Num average = criterionValue.dividedBy(numberOfPositions);
        Num pow = criterion.calculate(series, position).minus(average).pow(2);
        variance = variance.plus(pow);
        variance = variance.dividedBy(numberOfPositions);
        return variance;
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        if (tradingRecord.getPositions().isEmpty()) {
            return series.numOf(0);
        }
        Num criterionValue = criterion.calculate(series, tradingRecord, mostRecentPositions);
        Num numberOfPositions = numberOfPositionsCriterion.calculate(series, tradingRecord, mostRecentPositions);

        Num variance = series.numOf(0);
        Num average = criterionValue.dividedBy(numberOfPositions);
        int startingPositionIndex = Math.max(0, tradingRecord.getPositionCount() - mostRecentPositions);

        for (Position position : tradingRecord.getPositions()
                .subList(startingPositionIndex, tradingRecord.getPositionCount())) {
            Num pow = criterion.calculate(series, position).minus(average).pow(2);
            variance = variance.plus(pow);
        }
        variance = variance.dividedBy(numberOfPositions);
        return variance;
    }

    /** The higher the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

}
