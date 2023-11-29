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
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.num.Num;

public class TransformIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private TransformIndicator transPlus;
    private TransformIndicator transMinus;
    private TransformIndicator transMultiply;
    private TransformIndicator transDivide;
    private TransformIndicator transMax;
    private TransformIndicator transMin;

    private TransformIndicator transAbs;
    private TransformIndicator transPow;
    private TransformIndicator transSqrt;
    private TransformIndicator transLog;

    public TransformIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        BarSeries series = new BaseBarSeriesBuilder().withNumTypeOf(numFunction).build();
        ConstantIndicator<Num> constantIndicator = new ConstantIndicator<>(series, numOf(4));

        transPlus = TransformIndicator.plus(constantIndicator, 10);
        transMinus = TransformIndicator.minus(constantIndicator, 10);
        transMultiply = TransformIndicator.multiply(constantIndicator, 10);
        transDivide = TransformIndicator.divide(constantIndicator, 10);
        transMax = TransformIndicator.max(constantIndicator, 10);
        transMin = TransformIndicator.min(constantIndicator, 10);

        transAbs = TransformIndicator.abs(new ConstantIndicator<Num>(series, numOf(-4)));
        transPow = TransformIndicator.pow(constantIndicator, 2);
        transSqrt = TransformIndicator.sqrt(constantIndicator);
        transLog = TransformIndicator.log(constantIndicator);
    }

    @Test
    public void getValue() {
        assertNumEquals(14, transPlus.getValue(0));
        assertNumEquals(-6, transMinus.getValue(0));
        assertNumEquals(40, transMultiply.getValue(0));
        assertNumEquals(0.4, transDivide.getValue(0));
        assertNumEquals(10, transMax.getValue(0));
        assertNumEquals(4, transMin.getValue(0));

        assertNumEquals(4, transAbs.getValue(0));
        assertNumEquals(16, transPow.getValue(0));
        assertNumEquals(2, transSqrt.getValue(0));
        assertNumEquals(1.3862943611198906, transLog.getValue(0));
    }
}
