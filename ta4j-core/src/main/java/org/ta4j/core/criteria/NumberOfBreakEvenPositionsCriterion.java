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
import org.ta4j.core.num.Num;

/**
 * Number of break even position criterion.
 */
public class NumberOfBreakEvenPositionsCriterion extends AbstractAnalysisCriterion {

    @Override
    public Num calculate(BarSeries series, Position position) {
        return isBreakEvenPosition(position) ? series.numOf(1) : series.numOf(0);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        long numberOfBreakEvenTrades = tradingRecord.getPositions()
                .stream()
                .filter(Position::isClosed)
                .skip(Math.max(0,
                        tradingRecord.getPositions().stream().filter(Position::isClosed).count() - mostRecentPositions))
                .filter(this::isBreakEvenPosition)
                .count();
        return series.numOf(numberOfBreakEvenTrades);
    }

    private boolean isBreakEvenPosition(Position position) {
        if (position.isClosed()) {
            return position.getProfit().isZero();
        }
        return false;
    }

    /**
     * The lower the criterion value, the better.
     */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isLessThan(criterionValue2);
    }
}
