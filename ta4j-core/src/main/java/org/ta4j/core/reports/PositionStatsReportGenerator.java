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
import org.ta4j.core.criteria.NumberOfBreakEvenPositionsCriterion;
import org.ta4j.core.criteria.NumberOfLosingPositionsCriterion;
import org.ta4j.core.criteria.NumberOfWinningPositionsCriterion;
import org.ta4j.core.num.Num;

/**
 * This class generates PositionStatsReport based on provided trading record and
 * bar series.
 *
 * @see PositionStatsReport
 */
public class PositionStatsReportGenerator implements ReportGenerator<PositionStatsReport> {

    @Override
    public PositionStatsReport generate(Strategy strategy, TradingRecord tradingRecord, BarSeries series) {
        final Num winningPositions = new NumberOfWinningPositionsCriterion().calculate(series, tradingRecord);
        final Num losingPositions = new NumberOfLosingPositionsCriterion().calculate(series, tradingRecord);
        final Num breakEvenPositions = new NumberOfBreakEvenPositionsCriterion().calculate(series, tradingRecord);
        return new PositionStatsReport(winningPositions, losingPositions, breakEvenPositions);
    }
}
