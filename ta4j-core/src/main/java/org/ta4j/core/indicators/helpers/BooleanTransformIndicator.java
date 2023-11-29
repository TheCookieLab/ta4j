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
 * Simple boolean transform indicator.
 *
 * Transforms any decimal indicator to a boolean indicator by using common
 * logical operators.
 */
public class BooleanTransformIndicator extends CachedIndicator<Boolean> {

    /**
     * Select the type for transformation.
     */
    public enum BooleanTransformType {

        /**
         * Transforms the decimal indicator to a boolean indicator by
         * indicator.equals(coefficient).
         */
        equals,

        /**
         * Transforms the decimal indicator to a boolean indicator by
         * indicator.isGreaterThan(coefficient).
         */
        isGreaterThan,

        /**
         * Transforms the decimal indicator to a boolean indicator by
         * indicator.isGreaterThanOrEqual(coefficient).
         */
        isGreaterThanOrEqual,

        /**
         * Transforms the decimal indicator to a boolean indicator by
         * indicator.isLessThan(coefficient).
         */
        isLessThan,

        /**
         * Transforms the decimal indicator to a boolean indicator by
         * indicator.isLessThanOrEqual(coefficient).
         */
        isLessThanOrEqual
    }

    /**
     * Select the type for transformation.
     */
    public enum BooleanTransformSimpleType {
        /**
         * Transforms the decimal indicator to a boolean indicator by indicator.isNaN().
         */
        isNaN,

        /**
         * Transforms the decimal indicator to a boolean indicator by
         * indicator.isNegative().
         */
        isNegative,

        /**
         * Transforms the decimal indicator to a boolean indicator by
         * indicator.isNegativeOrZero().
         */
        isNegativeOrZero,

        /**
         * Transforms the decimal indicator to a boolean indicator by
         * indicator.isPositive().
         */
        isPositive,

        /**
         * Transforms the decimal indicator to a boolean indicator by
         * indicator.isPositiveOrZero().
         */
        isPositiveOrZero,

        /**
         * Transforms the decimal indicator to a boolean indicator by
         * indicator.isZero().
         */
        isZero
    }

    private Indicator<Num> indicator;
    private Num coefficient;
    private BooleanTransformType type;
    private BooleanTransformSimpleType simpleType;

    /**
     * Constructor.
     * 
     * @param indicator   the indicator
     * @param coefficient the value for transformation
     * @param type        the type of the transformation
     */
    public BooleanTransformIndicator(Indicator<Num> indicator, Num coefficient, BooleanTransformType type) {
        super(indicator);
        this.indicator = indicator;
        this.coefficient = coefficient;
        this.type = type;
    }

    /**
     * Constructor.
     * 
     * @param indicator the indicator
     * @param type      the type of the transformation
     */
    public BooleanTransformIndicator(Indicator<Num> indicator, BooleanTransformSimpleType type) {
        super(indicator);
        this.indicator = indicator;
        this.simpleType = type;
    }

    @Override
    protected Boolean calculate(int index) {

        Num val = indicator.getValue(index);

        if (type != null) {
            switch (type) {
            case equals:
                return val.equals(coefficient);
            case isGreaterThan:
                return val.isGreaterThan(coefficient);
            case isGreaterThanOrEqual:
                return val.isGreaterThanOrEqual(coefficient);
            case isLessThan:
                return val.isLessThan(coefficient);
            case isLessThanOrEqual:
                return val.isLessThanOrEqual(coefficient);
            default:
                break;
            }
        }

        else if (simpleType != null) {
            switch (simpleType) {
            case isNaN:
                return val.isNaN();
            case isNegative:
                return val.isNegative();
            case isNegativeOrZero:
                return val.isNegativeOrZero();
            case isPositive:
                return val.isPositive();
            case isPositiveOrZero:
                return val.isPositiveOrZero();
            case isZero:
                return val.isZero();
            default:
                break;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        if (type != null) {
            return getClass().getSimpleName() + " Coefficient: " + coefficient + " Transform(" + type.name() + ")";
        }
        return getClass().getSimpleName() + "Transform(" + simpleType.name() + ")";
    }
}
