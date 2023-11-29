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
 * This class represents report which contains performance statistics
 */
public class PerformanceReport {

    private final Num totalProfitLoss;
    private final Num totalProfitLossPercentage;
    private final Num totalProfit;
    private final Num totalLoss;

    public PerformanceReport(Num totalProfitLoss, Num totalProfitLossPercentage, Num totalProfit, Num totalLoss) {
        this.totalProfitLoss = totalProfitLoss;
        this.totalProfitLossPercentage = totalProfitLossPercentage;
        this.totalProfit = totalProfit;
        this.totalLoss = totalLoss;
    }

    public Num getTotalProfitLoss() {
        return totalProfitLoss;
    }

    public Num getTotalProfitLossPercentage() {
        return totalProfitLossPercentage;
    }

    public Num getTotalProfit() {
        return totalProfit;
    }

    public Num getTotalLoss() {
        return totalLoss;
    }
}
