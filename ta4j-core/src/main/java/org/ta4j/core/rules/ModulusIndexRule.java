package org.ta4j.core.rules;

import org.ta4j.core.TradingRecord;

/**
 *
 * @author David
 */
public class ModulusIndexRule extends AbstractRule {

    private int modulus;
    private boolean satisfied;

    /**
     * Constructor.
     *
     * @param modulus If the current index mod the given modulus == 0, return
     * satisfied
     * @param satisfied true for the rule to be always satisfied, false to be
     * never satisfied
     */
    public ModulusIndexRule(int modulus, boolean satisfied) {
        this.modulus = modulus;
        this.satisfied = satisfied;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (index % modulus == 0) {
            return satisfied;
        }
        return !satisfied;
    }
}
