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
 * Number of maximum consecutive winning or losing positions criterion.
 */
public class NumberOfConsecutivePositionsCriterion extends AbstractAnalysisCriterion {

    private final PositionFilter positionFilter;

    /**
     * Constructor.
     *
     * @param positionFilter consider either the winning or losing positions
     */
    public NumberOfConsecutivePositionsCriterion(PositionFilter positionFilter) {
        this.positionFilter = positionFilter;
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        return isConsecutive(position) ? series.numOf(1) : series.numOf(0);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        int maxConsecutive = 0;
        int consecutives = 0;
        int startingPositionIndex = Math.max(0, tradingRecord.getPositionCount() - mostRecentPositions);

        for (Position position : tradingRecord.getPositions()
                .subList(startingPositionIndex, tradingRecord.getPositionCount())) {
            if (isConsecutive(position)) {
                consecutives = consecutives + 1;
            } else {
                if (maxConsecutive < consecutives) {
                    maxConsecutive = consecutives;
                }
                consecutives = 0; // reset
            }
        }

        // in case all positions are consecutive positions
        if (maxConsecutive < consecutives) {
            maxConsecutive = consecutives;
        }

        return series.numOf(maxConsecutive);
    }

    /**
     * <ul>
     * <li>For {@link PositionFilter#PROFIT}: The higher the criterion value, the
     * better.
     * <li>For {@link PositionFilter#LOSS}: The lower the criterion value, the
     * better.
     * </ul>
     */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return positionFilter == PositionFilter.PROFIT ? criterionValue1.isGreaterThan(criterionValue2)
                : criterionValue1.isLessThan(criterionValue2);
    }

    private boolean isConsecutive(Position position) {
        if (position.isClosed()) {
            Num pnl = position.getProfit();
            return positionFilter == PositionFilter.PROFIT ? pnl.isPositive() : pnl.isNegative();
        }
        return false;
    }

}
