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

import java.util.Arrays;

import org.ta4j.core.TradingRecord;

/**
 * An indexes-based rule.
 *
 * Satisfied for provided indexes.
 */
public class FixedRule extends AbstractRule {

    private final int[] indexes;

    /**
     * Constructor.
     * 
     * @param indexes a sequence of indexes
     */
    public FixedRule(int... indexes) {
        this.indexes = Arrays.copyOf(indexes, indexes.length);
    }

    /** This rule does not use the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        boolean satisfied = false;
        for (int idx : indexes) {
            if (idx == index) {
                satisfied = true;
                break;
            }
        }
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }
}
