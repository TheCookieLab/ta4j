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
package org.ta4j.core.rules;

import org.ta4j.core.TradingRecord;

/**
 *
 * @author David
 */
public class ModulusIndexRule extends AbstractRule {

    private final boolean satisfied;
    private final int modulus;

    /**
     * Constructor.
     *
     * @param modulus   If the current index mod the given modulus == 0, return
     *                  satisfied
     * @param satisfied true for the rule to be always satisfied, false to be never
     *                  satisfied
     */
    public ModulusIndexRule(int modulus, boolean satisfied) {
        this.satisfied = satisfied;
        this.modulus = modulus;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (index % modulus == 0) {
            return satisfied;
        }
        return !satisfied;
    }
}
