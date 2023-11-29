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
package org.ta4j.core.reports;

import org.ta4j.core.num.Num;

/**
 * This class represents report with statistics for positions.
 */
public class PositionStatsReport {

    private final Num profitCount;
    private final Num lossCount;
    private final Num breakEvenCount;

    /**
     * Constructor.
     *
     * @param profitCount    the number of positions making a profit
     * @param lossCount      the number of positions making a loss
     * @param breakEvenCount the number of positions with a break even
     */
    public PositionStatsReport(Num profitCount, Num lossCount, Num breakEvenCount) {
        this.profitCount = profitCount;
        this.lossCount = lossCount;
        this.breakEvenCount = breakEvenCount;
    }

    public Num getProfitCount() {
        return profitCount;
    }

    public Num getLossCount() {
        return lossCount;
    }

    public Num getBreakEvenCount() {
        return breakEvenCount;
    }
}
