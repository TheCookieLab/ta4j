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

import java.time.ZonedDateTime;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.Random;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

public class FixedTransactionCostModelTest {

    private static final Random RANDOM = new Random();

    private static final Num PRICE = DoubleNum.valueOf(100);

    private static final Num AMOUNT = DoubleNum.valueOf(5);

    @Test
    public void calculatePerPositionWhenPositionIsOpen() {
        double positionTrades = 1;
        double feePerTrade = RANDOM.nextDouble();
        FixedTransactionCostModel model = new FixedTransactionCostModel(feePerTrade);

        Position position = new Position(TradeType.BUY, model, null);
        position.operate(0, ZonedDateTime.now(), PRICE, AMOUNT);
        Num cost = model.calculate(position);

        assertNumEquals(cost, DoubleNum.valueOf(feePerTrade * positionTrades));
        assertEquals(feePerTrade, model.getRawCostValue(), 0.00000000001);
    }

    @Test
    public void calculatePerPositionWhenPositionIsClosed() {
        double positionTrades = 2;
        double feePerTrade = RANDOM.nextDouble();
        FixedTransactionCostModel model = new FixedTransactionCostModel(feePerTrade);

        int holdingPeriod = 2;
        Trade entry = Trade.buyAt(0, PRICE, AMOUNT, model);
        Trade exit = Trade.sellAt(holdingPeriod, PRICE, AMOUNT, model);

        Position position = new Position(entry, exit, model, model);
        Num cost = model.calculate(position, RANDOM.nextInt());

        assertNumEquals(cost, DoubleNum.valueOf(feePerTrade * positionTrades));
    }

    @Test
    public void calculatePerPrice() {
        double feePerTrade = RANDOM.nextDouble();
        FixedTransactionCostModel model = new FixedTransactionCostModel(feePerTrade);
        Num cost = model.calculate(PRICE, AMOUNT);

        assertNumEquals(cost, DoubleNum.valueOf(feePerTrade));
    }

    @Test
    public void testEquality() {
        double randomFee = RANDOM.nextDouble();
        FixedTransactionCostModel model = new FixedTransactionCostModel(randomFee);
        CostModel modelSame = new FixedTransactionCostModel(randomFee);
        CostModel modelOther = new LinearTransactionCostModel(randomFee);
        boolean equality = model.equals(modelSame);
        boolean inequality = model.equals(modelOther);

        assertTrue(equality);
        assertFalse(inequality);
    }
}
