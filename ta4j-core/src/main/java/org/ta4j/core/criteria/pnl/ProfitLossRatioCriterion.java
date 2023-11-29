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
package org.ta4j.core.criteria.pnl;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

/**
 * Ratio gross profit and loss criterion = Average gross profit (includes
 * trading costs) / Average gross loss (includes trading costs).
 */
public class ProfitLossRatioCriterion extends AbstractAnalysisCriterion {

    private final AverageProfitCriterion averageProfitCriterion = new AverageProfitCriterion();
    private final AverageLossCriterion averageLossCriterion = new AverageLossCriterion();

    @Override
    public Num calculate(BarSeries series, Position position) {
        Num averageProfit = averageProfitCriterion.calculate(series, position);
        if (averageProfit.isZero()) {
            // only losing positions means a ratio of 0
            return series.numOf(0);
        }
        Num averageLoss = averageLossCriterion.calculate(series, position);
        if (averageLoss.isZero()) {
            // only winning positions means a ratio of 1 (infinite)
            return series.numOf(1);
        }
        return averageProfit.dividedBy(averageLoss).abs();
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        Num averageProfit = averageProfitCriterion.calculate(series, tradingRecord, mostRecentPositions);
        if (averageProfit.isZero()) {
            // only loosing positions means a ratio of 0
            return series.numOf(0);
        }
        Num averageLoss = averageLossCriterion.calculate(series, tradingRecord, mostRecentPositions);
        if (averageLoss.isZero()) {
            // only winning positions means a ratio of 1
            return series.numOf(1);
        }
        return averageProfit.dividedBy(averageLoss).abs();
    }

    /** The higher the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

}
