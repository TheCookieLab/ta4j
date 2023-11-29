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

import org.ta4j.core.BarSeries;

/**
 * A fixed boolean indicator.
 */
public class FixedBooleanIndicator extends FixedIndicator<Boolean> {

    /**
     * Constructor.
     * 
     * @param values the values to be returned by this indicator
     */
    public FixedBooleanIndicator(BarSeries series, Boolean... values) {
        super(series, values);
    }
}
