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
import org.ta4j.core.num.Num;

/**
 * Indicator-rising-indicator rule.
 *
 * Satisfied when the values of the {@link Indicator indicator} increase within
 * the barCount.
 */
public class IsRisingRule extends AbstractRule {

    /** The actual indicator */
    private final Indicator<Num> ref;
    /** The barCount */
    private final int barCount;
    /** The minimum required strength of the rising */
    private double minStrength;

    /**
     * Constructor for strict rising.
     * 
     * @param ref      the indicator
     * @param barCount the time frame
     */
    public IsRisingRule(Indicator<Num> ref, int barCount) {
        this(ref, barCount, 1);
    }

    /**
     * Constructor.
     * 
     * @param ref         the indicator
     * @param barCount    the time frame
     * @param minStrength the minimum required rising strength (between '0' and '1',
     *                    e.g. '1' for strict rising)
     */
    public IsRisingRule(Indicator<Num> ref, int barCount, double minStrength) {
        this.ref = ref;
        this.barCount = barCount;
        this.minStrength = minStrength;
    }

    /** This rule does not use the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (minStrength >= 1) {
            minStrength = 0.99;
        }

        int count = 0;
        for (int i = Math.max(0, index - barCount + 1); i <= index; i++) {
            if (ref.getValue(i).isGreaterThan(ref.getValue(Math.max(0, i - 1)))) {
                count += 1;
            }
        }

        double ratio = count / (double) barCount;

        final boolean satisfied = ratio >= minStrength;
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }
}
