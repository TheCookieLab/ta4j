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
package org.ta4j.core.mocks;

import java.util.List;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

public class MockIndicator implements Indicator<Num> {

    private BarSeries series;
    private List<Num> values;

    /**
     * Constructor.
     * 
     * @param series BarSeries of the Indicator
     * @param values Indicator values
     */
    public MockIndicator(BarSeries series, List<Num> values) {
        this.series = series;
        this.values = values;
    }

    /**
     * Gets a value from the Indicator
     * 
     * @param index Indicator value to get
     * @return Num Indicator value at index
     */
    public Num getValue(int index) {
        return values.get(index);
    }

    /**
     * Gets the Indicator TimeSeries.
     * 
     * @return TimeSeries of the Indicator
     */
    public BarSeries getBarSeries() {
        return series;
    }

    @Override
    public Num numOf(Number number) {
        return series.numOf(number);
    }

}
