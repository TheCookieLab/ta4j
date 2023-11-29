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

import java.util.List;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

/**
 * Bar series aggregator based on provided bar aggregator.
 */
public class BaseBarSeriesAggregator implements BarSeriesAggregator {

    private final BarAggregator barAggregator;

    public BaseBarSeriesAggregator(BarAggregator barAggregator) {
        this.barAggregator = barAggregator;
    }

    @Override
    public BarSeries aggregate(BarSeries series, String aggregatedSeriesName) {
        final List<Bar> aggregatedBars = barAggregator.aggregate(series.getBarData());
        return new BaseBarSeries(aggregatedSeriesName, aggregatedBars);
    }
}
