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
package org.ta4j.core.aggregator;

import org.ta4j.core.BarSeries;

/**
 * Bar aggregator interface to aggregate list of bars into another list of bars.
 */
public interface BarSeriesAggregator {

    /**
     * Aggregates bar series.
     *
     * @param series series to aggregate
     * @return aggregated series
     */
    default BarSeries aggregate(BarSeries series) {
        return aggregate(series, series.getName());
    }

    /**
     * Aggregates bar series.
     *
     * @param series               series to aggregate
     * @param aggregatedSeriesName name for aggregated series
     * @return aggregated series with specified name
     */
    BarSeries aggregate(BarSeries series, String aggregatedSeriesName);
}
