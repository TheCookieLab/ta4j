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
package org.ta4j.core.analysis.cost;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;

import org.junit.Test;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

public class ZeroCostModelTest {

    @Test
    public void calculatePerPosition() {
        // calculate costs per position
        ZeroCostModel model = new ZeroCostModel();

        int holdingPeriod = 2;
        Trade entry = Trade.buyAt(0, DoubleNum.valueOf(100), DoubleNum.valueOf(1), model);
        Trade exit = Trade.sellAt(holdingPeriod, DoubleNum.valueOf(110), DoubleNum.valueOf(1), model);

        Position position = new Position(entry, exit, model, model);
        Num cost = model.calculate(position, holdingPeriod);

        assertNumEquals(cost, DoubleNum.valueOf(0));

    }

    @Test
    public void calculatePerPrice() {
        // calculate costs per position
        ZeroCostModel model = new ZeroCostModel();
        Num cost = model.calculate(DoubleNum.valueOf(100), DoubleNum.valueOf(1));

        assertNumEquals(cost, DoubleNum.valueOf(0));
    }

    @Test
    public void testEquality() {
        ZeroCostModel model = new ZeroCostModel();
        CostModel modelSame = new ZeroCostModel();
        CostModel modelOther = new LinearTransactionCostModel(0.1);
        boolean equality = model.equals(modelSame);
        boolean inequality = model.equals(modelOther);

        assertTrue(equality);
        assertFalse(inequality);
    }
}