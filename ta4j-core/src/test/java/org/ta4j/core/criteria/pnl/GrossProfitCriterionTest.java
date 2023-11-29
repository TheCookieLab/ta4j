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
package org.ta4j.core.criteria.pnl;

import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.criteria.AbstractCriterionTest;
import org.ta4j.core.num.Num;

public class GrossProfitCriterionTest extends AbstractCriterionTest {

    public GrossProfitCriterionTest(Function<Number, Num> numFunction) {
        super((params) -> new GrossProfitCriterion(), numFunction);
    }

    @Test
    public void testCalculateOneOpenPositionShouldReturnZero() {
        openedPositionUtils.testCalculateOneOpenPositionShouldReturnExpectedValue(numFunction, getCriterion(), 0);
    }
}
