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
import org.ta4j.core.indicators.helpers.ConstantIndicator;
import org.ta4j.core.num.Num;

/**
 * Indicator-between-indicators rule.
 *
 * Satisfied when the value of the {@link Indicator indicator} is between the
 * values of the boundary (up/down) indicators.
 */
public class InPipeRule extends AbstractRule {

    /** The upper indicator */
    private Indicator<Num> upper;
    /** The lower indicator */
    private Indicator<Num> lower;
    /** The evaluated indicator */
    private Indicator<Num> ref;

    /**
     * Constructor.
     * 
     * @param ref   the reference indicator
     * @param upper the upper threshold
     * @param lower the lower threshold
     */
    public InPipeRule(Indicator<Num> ref, Number upper, Number lower) {
        this(ref, ref.numOf(upper), ref.numOf(lower));
    }

    /**
     * Constructor.
     * 
     * @param ref   the reference indicator
     * @param upper the upper threshold
     * @param lower the lower threshold
     */
    public InPipeRule(Indicator<Num> ref, Num upper, Num lower) {
        this(ref, new ConstantIndicator<>(ref.getBarSeries(), upper),
                new ConstantIndicator<>(ref.getBarSeries(), lower));
    }

    /**
     * Constructor.
     * 
     * @param ref   the reference indicator
     * @param upper the upper indicator
     * @param lower the lower indicator
     */
    public InPipeRule(Indicator<Num> ref, Indicator<Num> upper, Indicator<Num> lower) {
        this.upper = upper;
        this.lower = lower;
        this.ref = ref;
    }

    /** This rule does not use the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        final boolean satisfied = ref.getValue(index).isLessThanOrEqual(upper.getValue(index))
                && ref.getValue(index).isGreaterThanOrEqual(lower.getValue(index));
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }
}
