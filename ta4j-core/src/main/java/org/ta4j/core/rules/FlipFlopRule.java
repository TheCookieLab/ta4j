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
 * An alternating rule.
 *
 * Flips between satisfied and not satisfied with starting value specified in
 * constructor
 */
public class FlipFlopRule extends AbstractRule {

    private boolean previousSatisfied;

    /**
     *
     *
     * @param shouldStartSatisfied
     */
    public FlipFlopRule(boolean shouldStartSatisfied) {
        this.previousSatisfied = !shouldStartSatisfied;
    }

    /**
     * Constructor.
     *
     * Satisfied the first time and every alternate time after that
     */
    public FlipFlopRule() {
        this(true);
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        this.previousSatisfied = !this.previousSatisfied;
        return this.previousSatisfied;
    }
}
