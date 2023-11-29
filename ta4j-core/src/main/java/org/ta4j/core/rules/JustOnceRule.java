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
 * A one-shot rule.
 *
 * Satisfied the first time it's checked then never again.
 */
public class JustOnceRule extends AbstractRule {

    private final Rule rule;
    private boolean satisfied = false;

    /**
     * Constructor.
     *
     * Satisfied the first time the inner rule is satisfied then never again.
     *
     * @param rule the rule that should be satisfied only the first time
     */
    public JustOnceRule(Rule rule) {
        this.rule = rule;
    }

    /**
     * Constructor.
     *
     * Satisfied the first time it's checked then never again.
     */
    public JustOnceRule() {
        this.rule = null;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (satisfied) {
            return false;
        } else if (rule == null) {
            satisfied = true;
            traceIsSatisfied(index, true);
            return true;
        }
        this.satisfied = this.rule.isSatisfied(index, tradingRecord);
        return this.satisfied;
    }
}
