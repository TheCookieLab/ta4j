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
package org.ta4j.core.criteria;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.CriterionFactory;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

@RunWith(Parameterized.class)
public abstract class AbstractCriterionTest {

    protected final Function<Number, Num> numFunction;
    protected final OpenedPositionUtils openedPositionUtils = new OpenedPositionUtils();
    private final CriterionFactory factory;

    /**
     * Constructor.
     *
     * @param factory CriterionFactory for building an AnalysisCriterion given
     *                parameters
     */
    public AbstractCriterionTest(CriterionFactory factory, Function<Number, Num> numFunction) {
        this.factory = factory;
        this.numFunction = numFunction;
    }

    @Parameterized.Parameters(name = "Test Case: {index} (0=DoubleNum, 1=DecimalNum)")
    public static List<Function<Number, Num>> function() {
        return Arrays.asList(DoubleNum::valueOf, DecimalNum::valueOf);
    }

    /**
     * Generates an AnalysisCriterion given criterion parameters.
     *
     * @param params criterion parameters
     * @return AnalysisCriterion given parameters
     */
    public AnalysisCriterion getCriterion(Object... params) {
        return factory.getCriterion(params);
    }

    public Num numOf(Number n) {
        return numFunction.apply(n);
    }

}
