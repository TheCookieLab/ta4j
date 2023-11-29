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

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Transform indicator.
 * <p>
 * Transforms the Num of any indicator by using common math operations.
 *
 * @apiNote Minimal deviations in last decimal places possible. During some
 *          calculations this indicator converts {@link Num DecimalNum} to
 *          {@link Double double}
 */
public class TransformIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> indicator;
    private final UnaryOperator<Num> transformationFunction;

    /**
     * Constructor.
     *
     * @param indicator      the indicator
     * @param transformation a {@link Function} describing the transformation
     */
    public TransformIndicator(Indicator<Num> indicator, UnaryOperator<Num> transformation) {
        super(indicator);
        this.indicator = indicator;
        this.transformationFunction = transformation;
    }

    @Override
    protected Num calculate(int index) {
        return transformationFunction.apply(indicator.getValue(index));
    }

    /**
     * Transforms the input indicator by indicator.plus(coefficient).
     */
    public static TransformIndicator plus(Indicator<Num> indicator, Number coefficient) {
        Num numCoefficient = indicator.numOf(coefficient);
        return new TransformIndicator(indicator, val -> val.plus(numCoefficient));
    }

    /**
     * Transforms the input indicator by indicator.minus(coefficient).
     */
    public static TransformIndicator minus(Indicator<Num> indicator, Number coefficient) {
        Num numCoefficient = indicator.numOf(coefficient);
        return new TransformIndicator(indicator, val -> val.minus(numCoefficient));
    }

    /**
     * Transforms the input indicator by indicator.dividedBy(coefficient).
     */
    public static TransformIndicator divide(Indicator<Num> indicator, Number coefficient) {
        Num numCoefficient = indicator.numOf(coefficient);
        return new TransformIndicator(indicator, val -> val.dividedBy(numCoefficient));
    }

    /**
     * Transforms the input indicator by indicator.multipliedBy(coefficient).
     */
    public static TransformIndicator multiply(Indicator<Num> indicator, Number coefficient) {
        Num numCoefficient = indicator.numOf(coefficient);
        return new TransformIndicator(indicator, val -> val.multipliedBy(numCoefficient));
    }

    /**
     * Transforms the input indicator by indicator.max(coefficient).
     */
    public static TransformIndicator max(Indicator<Num> indicator, Number coefficient) {
        Num numCoefficient = indicator.numOf(coefficient);
        return new TransformIndicator(indicator, val -> val.max(numCoefficient));
    }

    /**
     * Transforms the input indicator by indicator.min(coefficient).
     */
    public static TransformIndicator min(Indicator<Num> indicator, Number coefficient) {
        Num numCoefficient = indicator.numOf(coefficient);
        return new TransformIndicator(indicator, val -> val.min(numCoefficient));
    }

    /**
     * Transforms the input indicator by indicator.abs().
     */
    public static TransformIndicator abs(Indicator<Num> indicator) {
        return new TransformIndicator(indicator, Num::abs);
    }

    /**
     * Transforms the input indicator by indicator.pow(coefficient).
     */
    public static TransformIndicator pow(Indicator<Num> indicator, Number coefficient) {
        Num numCoefficient = indicator.numOf(coefficient);
        return new TransformIndicator(indicator, val -> val.pow(numCoefficient));
    }

    /**
     * Transforms the input indicator by indicator.sqrt().
     */
    public static TransformIndicator sqrt(Indicator<Num> indicator) {
        return new TransformIndicator(indicator, Num::sqrt);
    }

    /**
     * Transforms the input indicator by indicator.log().
     *
     * @apiNote precision may be lost, because this implementation is using the
     *          underlying doubleValue method
     */
    public static TransformIndicator log(Indicator<Num> indicator) {
        return new TransformIndicator(indicator, val -> val.numOf(Math.log(val.doubleValue())));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
