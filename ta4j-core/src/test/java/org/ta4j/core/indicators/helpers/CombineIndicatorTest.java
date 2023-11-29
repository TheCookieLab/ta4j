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

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

public class CombineIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private CombineIndicator combinePlus;
    private CombineIndicator combineMinus;
    private CombineIndicator combineMultiply;
    private CombineIndicator combineDivide;
    private CombineIndicator combineMax;
    private CombineIndicator combineMin;

    public CombineIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        BarSeries series = new BaseBarSeriesBuilder().withNumTypeOf(numFunction).build();
        ConstantIndicator<Num> constantIndicator = new ConstantIndicator<>(series, numOf(4));
        ConstantIndicator<Num> constantIndicatorTwo = new ConstantIndicator<>(series, numOf(2));

        combinePlus = CombineIndicator.plus(constantIndicator, constantIndicatorTwo);
        combineMinus = CombineIndicator.minus(constantIndicator, constantIndicatorTwo);
        combineMultiply = CombineIndicator.multiply(constantIndicator, constantIndicatorTwo);
        combineDivide = CombineIndicator.divide(constantIndicator, constantIndicatorTwo);
        combineMax = CombineIndicator.max(constantIndicator, constantIndicatorTwo);
        combineMin = CombineIndicator.min(constantIndicator, constantIndicatorTwo);
    }

    @Test
    public void testAllDefaultMathCombineFunctions() {
        assertNumEquals(6, combinePlus.getValue(0));
        assertNumEquals(2, combineMinus.getValue(0));
        assertNumEquals(8, combineMultiply.getValue(0));
        assertNumEquals(2, combineDivide.getValue(0));
        assertNumEquals(4, combineMax.getValue(0));
        assertNumEquals(2, combineMin.getValue(0));
    }

    @Test
    public void testDifferenceIndicator() {

        Function<Number, Num> numFunction = DecimalNum::valueOf;

        BarSeries series = new BaseBarSeries();
        FixedIndicator<Num> mockIndicator = new FixedIndicator<Num>(series, numFunction.apply(-2.0),
                numFunction.apply(0.00), numFunction.apply(1.00), numFunction.apply(2.53), numFunction.apply(5.87),
                numFunction.apply(6.00), numFunction.apply(10.0));
        ConstantIndicator<Num> constantIndicator = new ConstantIndicator<Num>(series, numFunction.apply(6));
        CombineIndicator differenceIndicator = CombineIndicator.minus(constantIndicator, mockIndicator);

        assertNumEquals("8", differenceIndicator.getValue(0));
        assertNumEquals("6", differenceIndicator.getValue(1));
        assertNumEquals("5", differenceIndicator.getValue(2));
        assertNumEquals("3.47", differenceIndicator.getValue(3));
        assertNumEquals("0.13", differenceIndicator.getValue(4));
        assertNumEquals("0", differenceIndicator.getValue(5));
        assertNumEquals("-4", differenceIndicator.getValue(6));
    }
}
