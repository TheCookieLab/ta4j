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
package org.ta4j.core;

import org.ta4j.core.num.Num;

public interface ExternalIndicatorTest {

    /**
     * Gets the BarSeries used by an external indicator calculator.
     * 
     * @return BarSeries from the external indicator calculator
     * @throws Exception if the external calculator throws an Exception
     */
    BarSeries getSeries() throws Exception;

    /**
     * Sends indicator parameters to an external indicator calculator and returns
     * the externally calculated indicator.
     * 
     * @param params indicator parameters
     * @return Indicator<Num> from the external indicator calculator
     * @throws Exception if the external calculator throws an Exception
     */
    Indicator<Num> getIndicator(Object... params) throws Exception;

}
