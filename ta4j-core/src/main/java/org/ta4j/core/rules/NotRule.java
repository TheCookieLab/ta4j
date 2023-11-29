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

import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;

/**
 * An opposite (logical operator: NOT) rule.
 *
 * Satisfied when provided rule is not satisfied.<br>
 * Not satisfied when provided rule is satisfied.
 */
public class NotRule extends AbstractRule {

    private final Rule ruleToNegate;

    /**
     * Constructor.
     *
     * @param ruleToNegate a trading rule to negate
     */
    public NotRule(Rule ruleToNegate) {
        this.ruleToNegate = ruleToNegate;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        final boolean satisfied = !ruleToNegate.isSatisfied(index, tradingRecord);
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }

    public Rule getRuleToNegate() {
        return ruleToNegate;
    }
}
