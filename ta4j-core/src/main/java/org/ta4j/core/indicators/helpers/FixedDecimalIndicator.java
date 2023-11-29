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
package org.ta4j.core.indicators.helpers;

import java.math.BigDecimal;

import org.ta4j.core.BarSeries;
import org.ta4j.core.num.Num;

/**
 * A fixed decimal indicator.
 */
public class FixedDecimalIndicator extends FixedIndicator<Num> {

    /**
     * Constructor.
     * 
     * @param series
     * @param values the values to be returned by this indicator
     */
    public FixedDecimalIndicator(BarSeries series, double... values) {
        super(series);
        for (double value : values) {
            addValue(numOf(value));
        }
    }

    /**
     * Constructor.
     * 
     * @param series
     * @param values the values to be returned by this indicator
     */
    public FixedDecimalIndicator(BarSeries series, String... values) {
        super(series);
        for (String value : values) {
            addValue(numOf(new BigDecimal(value)));
        }
    }
}
