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

import org.ta4j.core.Strategy;

/**
 * This class represents trading statement report which contains position and
 * performance statistics
 */
public class TradingStatement {

    private final Strategy strategy;
    private final PositionStatsReport positionStatsReport;
    private final PerformanceReport performanceReport;

    public TradingStatement(Strategy strategy, PositionStatsReport positionStatsReport,
            PerformanceReport performanceReport) {
        this.strategy = strategy;
        this.positionStatsReport = positionStatsReport;
        this.performanceReport = performanceReport;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public PositionStatsReport getPositionStatsReport() {
        return positionStatsReport;
    }

    public PerformanceReport getPerformanceReport() {
        return performanceReport;
    }
}
