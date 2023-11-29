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
 * Satisfied when the absolute value difference between the first indicator and
 * the second indicator is greater than some fixed value
 * 
 * @author David
 */
public class AbsoluteValueOverRule extends AbstractRule {

    /**
     * The first indicator
     */
    private Indicator<Num> first;
    /**
     * The second indicator
     */
    private Indicator<Num> second;

    private Num value;

    /**
     * Constructor.
     *
     * @param first  the first indicator
     * @param second the second indicator
     * @param value
     */
    public AbsoluteValueOverRule(Indicator<Num> first, Indicator<Num> second, Num value) {
        this.first = first;
        this.second = second;
        this.value = value;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        final boolean satisfied = (first.getValue(index).minus(second.getValue(index))).abs().isGreaterThan(value);
        return satisfied;
    }

}
