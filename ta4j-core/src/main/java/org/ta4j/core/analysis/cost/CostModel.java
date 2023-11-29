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

import java.io.Serializable;

import org.ta4j.core.Position;
import org.ta4j.core.num.Num;

public interface CostModel extends Serializable {

    /**
     * @param position   the position
     * @param finalIndex final index of consideration for open positions
     * @return Calculates the trading cost of a single position
     */
    Num calculate(Position position, int finalIndex);

    /**
     * @param position the position
     * @return Calculates the trading cost of a single position
     */
    Num calculate(Position position);

    /**
     * @param price  the price per asset
     * @param amount number of traded assets
     * @return Calculates the trading cost for a certain traded amount
     */
    Num calculate(Num price, Num amount);

    boolean equals(CostModel model);

    /***
     * 
     * @return the underlying cost value in raw form
     */
    double getRawCostValue();
}