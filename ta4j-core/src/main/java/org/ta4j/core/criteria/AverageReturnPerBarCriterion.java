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
import org.ta4j.core.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.num.Num;

/**
 * Calculates the average return per bar criterion.
 *
 * <p>
 * The {@link GrossReturnCriterion gross return} raised to the power of 1
 * divided by {@link NumberOfBarsCriterion number of bars}.
 */
public class AverageReturnPerBarCriterion extends AbstractAnalysisCriterion {

    private final GrossReturnCriterion grossReturn = new GrossReturnCriterion();
    private final NumberOfBarsCriterion numberOfBars = new NumberOfBarsCriterion();

    @Override
    public Num calculate(BarSeries series, Position position) {
        Num bars = numberOfBars.calculate(series, position);
        if (bars.isEqual(series.numOf(0))) {
            return series.numOf(1);
        }

        return grossReturn.calculate(series, position).pow(series.numOf(1).dividedBy(bars));
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        Num bars = numberOfBars.calculate(series, tradingRecord, mostRecentPositions);
        if (bars.isEqual(series.numOf(0))) {
            return series.numOf(1);
        }

        return grossReturn.calculate(series, tradingRecord, mostRecentPositions).pow(series.numOf(1).dividedBy(bars));
    }

    /** The higher the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }
}
