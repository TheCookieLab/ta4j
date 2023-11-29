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
public interface CriterionFactory {

    /**
     * Applies parameters to a CriterionFactory and returns the AnalysisCriterion.
     * 
     * @param params criteria parameters
     * @return AnalysisCriterion with the parameters applied
     */
    AnalysisCriterion getCriterion(Object... params);

}
