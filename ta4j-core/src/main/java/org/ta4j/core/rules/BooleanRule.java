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
 * A simple boolean rule.
 *
 * Satisfied when it has been initialized with true.
 */
public class BooleanRule extends AbstractRule {

    /**
     * An always-true rule
     */
    public static final BooleanRule TRUE = new BooleanRule(true);

    /**
     * An always-false rule
     */
    public static final BooleanRule FALSE = new BooleanRule(false);

    private final boolean satisfied;

    /**
     * Constructor.
     *
     * @param satisfied true for the rule to be always satisfied, false to be never
     *                  satisfied
     */
    public BooleanRule(boolean satisfied) {
        this.satisfied = satisfied;
    }

    /** This rule does not use the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }
}
