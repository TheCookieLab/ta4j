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

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

/**
 * Reward risk ratio criterion, defined as the {@link GrossReturnCriterion gross
 * return} over the {@link MaximumDrawdownCriterion maximum drawdown}.
 */
public class ReturnOverMaxDrawdownCriterion extends AbstractAnalysisCriterion {

    private final AnalysisCriterion grossReturnCriterion = new GrossReturnCriterion();
    private final AnalysisCriterion maxDrawdownCriterion = new MaximumDrawdownCriterion();

    @Override
    public Num calculate(BarSeries series, Position position) {
        final Num maxDrawdown = maxDrawdownCriterion.calculate(series, position);
        if (maxDrawdown.isZero()) {
            return NaN.NaN;
        } else {
            final Num totalProfit = grossReturnCriterion.calculate(series, position);
            return totalProfit.dividedBy(maxDrawdown);
        }
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        final Num maxDrawdown = maxDrawdownCriterion.calculate(series, tradingRecord, mostRecentPositions);
        if (maxDrawdown.isZero()) {
            return NaN.NaN;
        } else {
            final Num totalProfit = grossReturnCriterion.calculate(series, tradingRecord, mostRecentPositions);
            return totalProfit.dividedBy(maxDrawdown);
        }
    }

    /**
     * The higher the criterion value, the better.
     */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }
}
