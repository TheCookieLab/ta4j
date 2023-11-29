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

@FunctionalInterface
public interface IndicatorFactory<D, I> {

    /**
     * Applies parameters and data to an IndicatorFactory and returns the Indicator.
     * 
     * @param data   source data for building the indicator
     * @param params indicator parameters
     * @return Indicator<I> with the indicator parameters applied
     */
    Indicator<I> getIndicator(D data, Object... params);

}
