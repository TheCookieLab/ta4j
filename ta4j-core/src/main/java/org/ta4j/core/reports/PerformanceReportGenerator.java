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
import org.ta4j.core.criteria.pnl.NetLossCriterion;
import org.ta4j.core.criteria.pnl.NetProfitCriterion;
import org.ta4j.core.criteria.pnl.ProfitLossCriterion;
import org.ta4j.core.criteria.pnl.ProfitLossPercentageCriterion;
import org.ta4j.core.num.Num;

/**
 * This class generates PerformanceReport basis on provided trading report and
 * bar series
 *
 * @see PerformanceReport
 */
public class PerformanceReportGenerator implements ReportGenerator<PerformanceReport> {

    @Override
    public PerformanceReport generate(Strategy strategy, TradingRecord tradingRecord, BarSeries series) {
        final Num pnl = new ProfitLossCriterion().calculate(series, tradingRecord);
        final Num pnlPercentage = new ProfitLossPercentageCriterion().calculate(series, tradingRecord);
        final Num netProfit = new NetProfitCriterion().calculate(series, tradingRecord);
        final Num netLoss = new NetLossCriterion().calculate(series, tradingRecord);
        return new PerformanceReport(pnl, pnlPercentage, netProfit, netLoss);
    }
}
