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

import org.ta4j.core.Indicator;
import org.ta4j.core.TradingRecord;

/**
 * A boolean-indicator-based rule.
 *
 * Satisfied when the value of the {@link Indicator indicator} is true.
 */
public class BooleanIndicatorRule extends AbstractRule {

    private final Indicator<Boolean> indicator;

    /**
     * Constructor.
     *
     * @param indicator a boolean indicator
     */
    public BooleanIndicatorRule(Indicator<Boolean> indicator) {
        this.indicator = indicator;
    }

    /** This rule does not use the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        final boolean satisfied = indicator.getValue(index);
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }
}
