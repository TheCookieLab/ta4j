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
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.pnl.GrossReturnCriterion;
import org.ta4j.core.num.Num;

/**
 * A linear transaction cost criterion.
 *
 * Calculates the transaction cost according to an initial traded amount and a
 * linear function defined by a and b (a * x + b).
 */
public class LinearTransactionCostCriterion extends AbstractAnalysisCriterion {

    private double initialAmount;

    private double a;
    private double b;

    private GrossReturnCriterion grossReturn;

    /**
     * Constructor. (a * x)
     *
     * @param initialAmount the initially traded amount
     * @param a             the a coefficient (e.g. 0.005 for 0.5% per {@link Trade
     *                      trade})
     */
    public LinearTransactionCostCriterion(double initialAmount, double a) {
        this(initialAmount, a, 0);
    }

    /**
     * Constructor. (a * x + b)
     *
     * @param initialAmount the initially traded amount
     * @param a             the a coefficient (e.g. 0.005 for 0.5% per {@link Trade
     *                      trade})
     * @param b             the b constant (e.g. 0.2 for $0.2 per {@link Trade
     *                      trade})
     */
    public LinearTransactionCostCriterion(double initialAmount, double a, double b) {
        this.initialAmount = initialAmount;
        this.a = a;
        this.b = b;
        grossReturn = new GrossReturnCriterion();
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        return getTradeCost(series, position, series.numOf(initialAmount));
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        Num totalCosts = series.numOf(0);
        Num tradedAmount = series.numOf(initialAmount);

        int positionStartIndex = Math.max(0, tradingRecord.getPositionCount() - mostRecentPositions);

        for (Position position : tradingRecord.getPositions()
                .subList(positionStartIndex, tradingRecord.getPositionCount())) {
            Num tradeCost = getTradeCost(series, position, tradedAmount);
            totalCosts = totalCosts.plus(tradeCost);
            // To calculate the new traded amount:
            // - Remove the cost of the *first* trade
            // - Multiply by the profit ratio
            // - Remove the cost of the *second* trade
            tradedAmount = tradedAmount.minus(getTradeCost(position.getEntry(), tradedAmount));
            tradedAmount = tradedAmount.multipliedBy(grossReturn.calculate(series, position));
            tradedAmount = tradedAmount.minus(getTradeCost(position.getExit(), tradedAmount));
        }

        // Special case: if the current position is open
        Position currentPosition = tradingRecord.getCurrentPosition();
        if (currentPosition.isOpened()) {
            totalCosts = totalCosts.plus(getTradeCost(currentPosition.getEntry(), tradedAmount));
        }

        return totalCosts;
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    /** The lower the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isLessThan(criterionValue2);
    }

    /**
     * @param trade        the trade
     * @param tradedAmount the amount of the trade
     * @return the absolute trade cost
     */
    private Num getTradeCost(Trade trade, Num tradedAmount) {
        Num tradeCost = tradedAmount.numOf(0);
        if (trade != null) {
            return tradedAmount.numOf(a).multipliedBy(tradedAmount).plus(tradedAmount.numOf(b));
        }
        return tradeCost;
    }

    /**
     * @param series        the bar series
     * @param position      the position
     * @param initialAmount the initially traded amount for the position
     * @return the absolute total cost of all trades in the position
     */
    private Num getTradeCost(BarSeries series, Position position, Num initialAmount) {
        Num totalTradeCost = series.numOf(0);
        if (position != null) {
            if (position.getEntry() != null) {
                totalTradeCost = getTradeCost(position.getEntry(), initialAmount);
                if (position.getExit() != null) {
                    // To calculate the new traded amount:
                    // - Remove the cost of the first trade
                    // - Multiply by the profit ratio
                    Num newTradedAmount = initialAmount.minus(totalTradeCost)
                            .multipliedBy(grossReturn.calculate(series, position));
                    totalTradeCost = totalTradeCost.plus(getTradeCost(position.getExit(), newTradedAmount));
                }
            }
        }
        return totalTradeCost;
    }
}
