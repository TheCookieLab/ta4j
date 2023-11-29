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

import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;

/**
 * This class generates TradingStatement basis on provided trading record and
 * bar series
 *
 * @see TradingStatement
 */
public class TradingStatementGenerator implements ReportGenerator<TradingStatement> {

    private final PerformanceReportGenerator performanceReportGenerator;
    private final PositionStatsReportGenerator positionStatsReportGenerator;

    public TradingStatementGenerator() {
        this(new PerformanceReportGenerator(), new PositionStatsReportGenerator());
    }

    public TradingStatementGenerator(PerformanceReportGenerator performanceReportGenerator,
            PositionStatsReportGenerator positionStatsReportGenerator) {
        super();
        this.performanceReportGenerator = performanceReportGenerator;
        this.positionStatsReportGenerator = positionStatsReportGenerator;
    }

    @Override
    public TradingStatement generate(Strategy strategy, TradingRecord tradingRecord, BarSeries series) {
        final PerformanceReport performanceReport = performanceReportGenerator.generate(strategy, tradingRecord,
                series);
        final PositionStatsReport positionStatsReport = positionStatsReportGenerator.generate(strategy, tradingRecord,
                series);
        return new TradingStatement(strategy, positionStatsReport, performanceReport);
    }
}
