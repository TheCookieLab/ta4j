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
import org.ta4j.core.criteria.NumberOfWinningPositionsCriterion;
import org.ta4j.core.num.Num;

/**
 * Average gross profit criterion (includes trading costs).
 */
public class AverageProfitCriterion extends AbstractAnalysisCriterion {

    private final NumberOfWinningPositionsCriterion numberOfWinningPositionsCriterion = new NumberOfWinningPositionsCriterion();
    private final GrossProfitCriterion grossProfitCriterion = new GrossProfitCriterion();

    @Override
    public Num calculate(BarSeries series, Position position) {
        Num numberOfWinningPositions = numberOfWinningPositionsCriterion.calculate(series, position);
        if (numberOfWinningPositions.isZero()) {
            return series.numOf(0);
        }
        Num grossProfit = grossProfitCriterion.calculate(series, position);
        if (grossProfit.isZero()) {
            return series.numOf(0);
        }
        return grossProfit.dividedBy(numberOfWinningPositions);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        Num numberOfWinningPositions = numberOfWinningPositionsCriterion.calculate(series, tradingRecord,
                mostRecentPositions);
        if (numberOfWinningPositions.isZero()) {
            return series.numOf(0);
        }
        Num grossProfit = grossProfitCriterion.calculate(series, tradingRecord, mostRecentPositions);
        if (grossProfit.isZero()) {
            return series.numOf(0);
        }
        return grossProfit.dividedBy(numberOfWinningPositions);
    }

    /** The higher the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

}
