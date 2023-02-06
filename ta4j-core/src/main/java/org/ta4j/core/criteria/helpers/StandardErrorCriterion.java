/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2022 Ta4j Organization & respective
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
package org.ta4j.core.criteria.helpers;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.criteria.NumberOfPositionsCriterion;
import org.ta4j.core.num.Num;

/**
 * Standard error criterion.
 * 
 * <p>
 * Calculates the standard deviation for a Criterion.
 */
public class StandardErrorCriterion extends AbstractAnalysisCriterion {

    private final StandardDeviationCriterion standardDeviationCriterion;
    private final NumberOfPositionsCriterion numberOfPositionsCriterion = new NumberOfPositionsCriterion();

    /**
     * Constructor.
     * 
     * @param criterion the criterion from which the "standard deviation error" is
     *                  calculated
     */
    public StandardErrorCriterion(AnalysisCriterion criterion) {
        this.standardDeviationCriterion = new StandardDeviationCriterion(criterion);
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        Num numberOfPositions = numberOfPositionsCriterion.calculate(series, position);
        return standardDeviationCriterion.calculate(series, position).dividedBy(numberOfPositions.sqrt());
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
        Num numberOfPositions = numberOfPositionsCriterion.calculate(series, tradingRecord, mostRecentPositions);
        return standardDeviationCriterion.calculate(series, tradingRecord, mostRecentPositions).dividedBy(numberOfPositions.sqrt());
    }

    /** The lower the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isLessThan(criterionValue2);
    }

}
