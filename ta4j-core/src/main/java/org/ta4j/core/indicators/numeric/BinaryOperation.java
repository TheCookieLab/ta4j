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

import java.util.function.BinaryOperator;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.num.Num;

/**
 * Objects of this class defer evaluation of an arithmetic operation.
 * 
 * This is a lightweight version of the CombineIndicator; it doesn't cache.
 */
class BinaryOperation implements Indicator<Num> {

    public static BinaryOperation sum(Indicator<Num> left, Indicator<Num> right) {
        return new BinaryOperation(Num::plus, left, right);
    }

    public static BinaryOperation difference(Indicator<Num> left, Indicator<Num> right) {
        return new BinaryOperation(Num::minus, left, right);
    }

    public static BinaryOperation product(Indicator<Num> left, Indicator<Num> right) {
        return new BinaryOperation(Num::multipliedBy, left, right);
    }

    public static BinaryOperation quotient(Indicator<Num> left, Indicator<Num> right) {
        return new BinaryOperation(Num::dividedBy, left, right);
    }

    public static BinaryOperation min(Indicator<Num> left, Indicator<Num> right) {
        return new BinaryOperation(Num::min, left, right);
    }

    public static BinaryOperation max(Indicator<Num> left, Indicator<Num> right) {
        return new BinaryOperation(Num::max, left, right);
    }

    private final BinaryOperator<Num> operator;
    private final Indicator<Num> left;
    private final Indicator<Num> right;

    private BinaryOperation(BinaryOperator<Num> operator, Indicator<Num> left, Indicator<Num> right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public Num getValue(int index) {
        Num n1 = left.getValue(index);
        Num n2 = right.getValue(index);
        return operator.apply(n1, n2);
    }

    @Override
    public BarSeries getBarSeries() {
        return left.getBarSeries();
    }

    @Override
    public Num numOf(Number number) {
        return left.numOf(number);
    }

}
