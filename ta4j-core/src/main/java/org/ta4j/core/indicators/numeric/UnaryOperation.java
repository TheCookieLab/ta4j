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
package org.ta4j.core.indicators.numeric;

import java.util.function.UnaryOperator;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

/**
 * Objects of this class defer the evaluation of a unary operator, like sqrt().
 * 
 * There may be other unary operations on Num that could be added here.
 */
class UnaryOperation implements Indicator<Num> {

    public static UnaryOperation sqrt(Indicator<Num> operand) {
        return new UnaryOperation(Num::sqrt, operand);
    }

    public static UnaryOperation abs(Indicator<Num> operand) {
        return new UnaryOperation(Num::abs, operand);
    }

    private final UnaryOperator<Num> operator;
    private final Indicator<Num> operand;

    private UnaryOperation(UnaryOperator<Num> operator, Indicator<Num> operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public Num getValue(int index) {
        Num n = operand.getValue(index);
        return operator.apply(n);
    }

    @Override
    public BarSeries getBarSeries() {
        return operand.getBarSeries();
    }

    // make this a default method in the Indicator interface...
    @Override
    public Num numOf(Number number) {
        return operand.numOf(number);
    }

}
