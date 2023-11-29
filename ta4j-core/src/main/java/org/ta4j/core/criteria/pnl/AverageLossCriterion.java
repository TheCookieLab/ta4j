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
import org.ta4j.core.criteria.NumberOfLosingPositionsCriterion;
import org.ta4j.core.num.Num;

/**
 * Average gross loss criterion (includes trading costs).
 */
public class AverageLossCriterion extends AbstractAnalysisCriterion {

    private final NumberOfLosingPositionsCriterion numberOfLosingPositionsCriterion = new NumberOfLosingPositionsCriterion();
    private final GrossLossCriterion grossLossCriterion = new GrossLossCriterion();

    @Override
    public Num calculate(BarSeries series, Position position) {
        Num numberOfLosingPositions = numberOfLosingPositionsCriterion.calculate(series, position);
        if (numberOfLosingPositions.isZero()) {
            return series.numOf(0);
        }
        Num grossLoss = grossLossCriterion.calculate(series, position);
        if (grossLoss.isZero()) {
            return series.numOf(0);
        }
        return grossLoss.dividedBy(numberOfLosingPositions);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        Num numberOfLosingPositions = numberOfLosingPositionsCriterion.calculate(series, tradingRecord,
                mostRecentPositions);
        if (numberOfLosingPositions.isZero()) {
            return series.numOf(0);
        }
        Num grossLoss = grossLossCriterion.calculate(series, tradingRecord, mostRecentPositions);
        if (grossLoss.isZero()) {
            return series.numOf(0);
        }
        return grossLoss.dividedBy(numberOfLosingPositions);
    }

    /** The higher the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

}
