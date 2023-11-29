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
import org.ta4j.core.analysis.CashFlow;
import org.ta4j.core.num.Num;

/**
 * Maximum drawdown criterion.
 *
 * @see <a href=
 *      "http://en.wikipedia.org/wiki/Drawdown_%28economics%29">https://en.wikipedia.org/wiki/Drawdown_(economics)</a>
 */
public class MaximumDrawdownCriterion extends AbstractAnalysisCriterion {

    @Override
    public Num calculate(BarSeries series, Position position) {
        if (position != null && position.getEntry() != null && position.getExit() != null) {
            CashFlow cashFlow = new CashFlow(series, position);
            return calculateMaximumDrawdown(series, cashFlow);
        }
        return series.numOf(0);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        CashFlow cashFlow = new CashFlow(series, tradingRecord, mostRecentPositions);
        return calculateMaximumDrawdown(series, cashFlow);
    }

    /** The lower the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isLessThan(criterionValue2);
    }

    /**
     * Calculates the maximum drawdown from a cash flow over a series.
     *
     * @param series   the bar series
     * @param cashFlow the cash flow
     * @return the maximum drawdown from a cash flow over a series
     */
    private Num calculateMaximumDrawdown(BarSeries series, CashFlow cashFlow) {
        Num maximumDrawdown = series.numOf(0);
        Num maxPeak = series.numOf(0);
        if (!series.isEmpty()) {
            // The series is not empty
            for (int i = series.getBeginIndex(); i <= series.getEndIndex(); i++) {
                Num value = cashFlow.getValue(i);
                if (value.isGreaterThan(maxPeak)) {
                    maxPeak = value;
                }

                Num drawdown = maxPeak.minus(value).dividedBy(maxPeak);
                if (drawdown.isGreaterThan(maximumDrawdown)) {
                    maximumDrawdown = drawdown;
                }
            }
        }
        return maximumDrawdown;
    }
}
