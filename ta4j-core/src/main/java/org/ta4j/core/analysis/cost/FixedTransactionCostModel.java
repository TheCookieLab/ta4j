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

public class FixedTransactionCostModel implements CostModel {

    private static final long serialVersionUID = 3486293523619720786L;

    /**
     * Cost per {@link Trade trade}.
     */
    private final double feePerTrade;

    /**
     * Constructor for a fixed fee trading cost model.
     *
     * Open {@link Position position} cost: (fixedFeePerTrade * 1) Closed
     * {@link Position position} cost: (fixedFeePerTrade * 2)
     *
     * @param feePerTrade the fixed fee per {@link Trade trade})
     */
    public FixedTransactionCostModel(double feePerTrade) {
        this.feePerTrade = feePerTrade;
    }

    /**
     * *
     *
     * @return the underlying fee per trade raw value
     */
    @Override
    public double getRawCostValue() {
        return this.feePerTrade;
    }

    /**
     * Calculates the transaction cost of a position.
     *
     * @param position     the position
     * @param currentIndex current bar index (irrelevant for the
     *                     FixedTransactionCostModel)
     * @return the absolute position cost
     */
    @Override
    public Num calculate(Position position, int currentIndex) {
        Num pricePerAsset = position.getEntry().getPricePerAsset();
        Num multiplier = pricePerAsset.numOf(1);
        if (position.isClosed()) {
            multiplier = pricePerAsset.numOf(2);
        }
        return pricePerAsset.numOf(feePerTrade).multipliedBy(multiplier);
    }

    /**
     * Calculates the transaction cost of a position.
     *
     * @param position the position
     * @return the absolute position cost
     */
    @Override
    public Num calculate(Position position) {
        return this.calculate(position, 0);
    }

    /**
     * Calculates the transaction cost based on the price and the amount (both
     * irrelevant for the FixedTransactionCostModel as the fee is always the same).
     *
     * @param price  the price per asset
     * @param amount number of traded assets
     */
    @Override
    public Num calculate(Num price, Num amount) {
        return price.numOf(feePerTrade);
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
            equality = ((FixedTransactionCostModel) otherModel).feePerTrade == this.feePerTrade;
        }
        return equality;
    }
}
