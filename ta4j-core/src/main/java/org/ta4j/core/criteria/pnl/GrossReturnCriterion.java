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
 * Gross return (in percentage) criterion (includes trading costs).
 *
 * <p>
 * The gross return of the provided {@link Position position(s)} over the
 * provided {@link BarSeries series}.
 */
public class GrossReturnCriterion extends AbstractAnalysisCriterion {

    @Override
    public Num calculate(BarSeries series, Position position) {
        return calculateProfit(series, position);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        return tradingRecord.getPositions()
                .stream()
                .skip(Math.max(0, tradingRecord.getPositionCount() - mostRecentPositions))
                .map(position -> calculateProfit(series, position))
                .reduce(series.numOf(1), Num::multipliedBy);
    }

    /**
     * The higher the criterion value, the better.
     */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

    /**
     * Calculates the gross return of a position (Buy and sell).
     *
     * @param series   a bar series
     * @param position a position
     * @return the gross return of the position
     */
    private Num calculateProfit(BarSeries series, Position position) {
        if (position.isClosed()) {
            return position.getGrossReturn(series);
        }
        return series.numOf(1);
    }
}
