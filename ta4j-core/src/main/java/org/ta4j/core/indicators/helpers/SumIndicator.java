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
package org.ta4j.core.indicators.helpers;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Sum indicator.
 *
 * I.e.: operand0 + operand1 + ... + operandN
 */
public class SumIndicator extends CachedIndicator<Num> {

    private final Indicator<Num>[] operands;

    /**
     * Constructor. (operand0 plus operand1 plus ... plus operandN)
     * 
     * @param operands the operand indicators for the sum
     */
    @SafeVarargs
    public SumIndicator(Indicator<Num>... operands) {
        // TODO: check if first series is equal to the other ones
        super(operands[0]);
        this.operands = operands;
    }

    @Override
    protected Num calculate(int index) {
        Num sum = numOf(0);
        for (Indicator<Num> operand : operands) {
            sum = sum.plus(operand.getValue(index));
        }
        return sum;
    }
}
