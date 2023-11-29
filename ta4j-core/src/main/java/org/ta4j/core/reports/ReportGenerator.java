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
 * Generic interface for generating trading reports
 *
 * @param <T> type of report to be generated
 */
public interface ReportGenerator<T> {

    /**
     * Generate report
     *
     * @param strategy
     * @param tradingRecord the trading record which is a source to generate report,
     *                      not null
     * @param series        the bar series, not null
     * @return generated report
     */
    T generate(Strategy strategy, TradingRecord tradingRecord, BarSeries series);
}
