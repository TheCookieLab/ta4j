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

import java.util.function.BinaryOperator;
import java.util.function.Function;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Combine indicator.
 * <p>
 * Combines two Num indicators by using common math operations.
 *
 */
public class CombineIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> indicatorLeft;
    private final Indicator<Num> indicatorRight;
    private final BinaryOperator<Num> combineFunction;

    /**
     * Constructor.
     *
     * @param indicatorLeft  the indicator for the left hand side of the calculation
     * @param indicatorRight the indicator for the right hand side of the
     *                       calculation
     * @param combination    a {@link Function} describing the combination function
     *                       to combine the values of the indicators
     */
    public CombineIndicator(Indicator<Num> indicatorLeft, Indicator<Num> indicatorRight,
            BinaryOperator<Num> combination) {
        // TODO check both indicators use the same series/num function
        super(indicatorLeft);
        this.indicatorLeft = indicatorLeft;
        this.indicatorRight = indicatorRight;
        this.combineFunction = combination;
    }

    @Override
    protected Num calculate(int index) {
        return combineFunction.apply(indicatorLeft.getValue(index), indicatorRight.getValue(index));
    }

    /**
     * Combines the two input indicators by indicatorLeft.plus(indicatorRight).
     */
    public static CombineIndicator plus(Indicator<Num> indicatorLeft, Indicator<Num> indicatorRight) {
        return new CombineIndicator(indicatorLeft, indicatorRight, Num::plus);
    }

    /**
     * Combines the two input indicators by indicatorLeft.minus(indicatorRight).
     */
    public static CombineIndicator minus(Indicator<Num> indicatorLeft, Indicator<Num> indicatorRight) {
        return new CombineIndicator(indicatorLeft, indicatorRight, Num::minus);
    }

    /**
     * Combines the two input indicators by indicatorLeft.dividedBy(indicatorRight).
     */
    public static CombineIndicator divide(Indicator<Num> indicatorLeft, Indicator<Num> indicatorRight) {
        return new CombineIndicator(indicatorLeft, indicatorRight, Num::dividedBy);
    }

    /**
     * Combines the two input indicators by
     * indicatorLeft.multipliedBy(indicatorRight).
     */
    public static CombineIndicator multiply(Indicator<Num> indicatorLeft, Indicator<Num> indicatorRight) {
        return new CombineIndicator(indicatorLeft, indicatorRight, Num::multipliedBy);
    }

    /**
     * Combines the two input indicators by indicatorLeft.max(indicatorRight).
     */
    public static CombineIndicator max(Indicator<Num> indicatorLeft, Indicator<Num> indicatorRight) {
        return new CombineIndicator(indicatorLeft, indicatorRight, Num::max);
    }

    /**
     * Combines the two input indicators by indicatorLeft.min(indicatorRight).
     */
    public static CombineIndicator min(Indicator<Num> indicatorLeft, Indicator<Num> indicatorRight) {
        return new CombineIndicator(indicatorLeft, indicatorRight, Num::min);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
