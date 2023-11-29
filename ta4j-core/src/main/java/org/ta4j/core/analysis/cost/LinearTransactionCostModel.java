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
package org.ta4j.core.analysis.cost;

import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.num.Num;

public class LinearTransactionCostModel implements CostModel {

    private static final long serialVersionUID = -8808559507754156097L;
    /**
     * Slope of the linear model - fee per position
     */
    private final double feePerPosition;

    /**
     * Constructor. (feePerPosition * x)
     *
     * @param feePerPosition the feePerPosition coefficient (e.g. 0.005 for 0.5% per
     *                       {@link Trade trade})
     */
    public LinearTransactionCostModel(double feePerPosition) {
        this.feePerPosition = feePerPosition;
    }

    /**
     * *
     *
     * @return the underlying fee per position raw value
     */
    @Override
    public double getRawCostValue() {
        return this.feePerPosition;
    }

    /**
     * Calculates the transaction cost of a position.
     *
     * @param position     the position
     * @param currentIndex current bar index (irrelevant for the
     *                     LinearTransactionCostModel)
     * @return the absolute trade cost
     */
    @Override
    public Num calculate(Position position, int currentIndex) {
        return this.calculate(position);
    }

    /**
     * Calculates the transaction cost of a position.
     *
     * @param position the position
     * @return the absolute trade cost
     */
    @Override
    public Num calculate(Position position) {
        Num totalPositionCost = null;
        Trade entryTrade = position.getEntry();
        if (entryTrade != null) {
            // transaction costs of entry trade
            totalPositionCost = entryTrade.getCost();
            if (position.getExit() != null) {
                totalPositionCost = totalPositionCost.plus(position.getExit().getCost());
            }
        }
        return totalPositionCost;
    }

    /**
     * @param price  execution price
     * @param amount trade amount
     * @return the absolute trade transaction cost
     */
    @Override
    public Num calculate(Num price, Num amount) {
        return amount.numOf(feePerPosition).multipliedBy(price).multipliedBy(amount);
    }

    /**
     * Evaluate if two models are equal
     *
     * @param otherModel model to compare with
     * @return
     */
    @Override
    public boolean equals(CostModel otherModel) {
        boolean equality = false;
        if (this.getClass().equals(otherModel.getClass())) {
            equality = ((LinearTransactionCostModel) otherModel).feePerPosition == this.feePerPosition;
        }
        return equality;
    }
}
