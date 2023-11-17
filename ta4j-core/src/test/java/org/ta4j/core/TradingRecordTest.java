/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2022 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core;

import java.time.ZonedDateTime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.num.NaN.NaN;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.analysis.cost.FixedTransactionCostModel;
import org.ta4j.core.num.DoubleNum;

public class TradingRecordTest {

    private TradingRecord emptyRecord, openedRecord, closedRecord;

    @Before
    public void setUp() {
        emptyRecord = new BaseTradingRecord();
        openedRecord = new BaseTradingRecord(Trade.buyAt(0, ZonedDateTime.now(), NaN, NaN),
                Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN), Trade.buyAt(7, ZonedDateTime.now(), NaN, NaN));
        closedRecord = new BaseTradingRecord(Trade.buyAt(0, ZonedDateTime.now(), NaN, NaN),
                Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN), Trade.buyAt(7, ZonedDateTime.now(), NaN, NaN),
                Trade.sellAt(8, ZonedDateTime.now(), NaN, NaN));
    }

    @Test
    public void getCurrentPosition() {
        assertTrue(emptyRecord.getCurrentPosition().isNew());
        assertTrue(openedRecord.getCurrentPosition().isOpened());
        assertTrue(closedRecord.getCurrentPosition().isNew());
    }

    @Test
    public void operate() {
        TradingRecord record = new BaseTradingRecord();

        record.operate(1);
        assertTrue(record.getCurrentPosition().isOpened());
        assertEquals(0, record.getPositionCount());
        assertNull(record.getLastPosition());
        assertEquals(Trade.buyAt(1, ZonedDateTime.now(), NaN, NaN), record.getLastTrade());
        assertEquals(Trade.buyAt(1, ZonedDateTime.now(), NaN, NaN), record.getLastTrade(Trade.TradeType.BUY));
        assertNull(record.getLastTrade(Trade.TradeType.SELL));
        assertEquals(Trade.buyAt(1, ZonedDateTime.now(), NaN, NaN), record.getLastEntry());
        assertNull(record.getLastExit());

        record.operate(3);
        assertTrue(record.getCurrentPosition().isNew());
        assertEquals(1, record.getPositionCount());
        assertEquals(new Position(Trade.buyAt(1, ZonedDateTime.now(), NaN, NaN),
                Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN)), record.getLastPosition());
        assertEquals(Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN), record.getLastTrade());
        assertEquals(Trade.buyAt(1, ZonedDateTime.now(), NaN, NaN), record.getLastTrade(Trade.TradeType.BUY));
        assertEquals(Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN), record.getLastTrade(Trade.TradeType.SELL));
        assertEquals(Trade.buyAt(1, ZonedDateTime.now(), NaN, NaN), record.getLastEntry());
        assertEquals(Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN), record.getLastExit());

        record.operate(5);
        assertTrue(record.getCurrentPosition().isOpened());
        assertEquals(1, record.getPositionCount());
        assertEquals(new Position(Trade.buyAt(1, ZonedDateTime.now(), NaN, NaN),
                Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN)), record.getLastPosition());
        assertEquals(Trade.buyAt(5, ZonedDateTime.now(), NaN, NaN), record.getLastTrade());
        assertEquals(Trade.buyAt(5, ZonedDateTime.now(), NaN, NaN), record.getLastTrade(Trade.TradeType.BUY));
        assertEquals(Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN), record.getLastTrade(Trade.TradeType.SELL));
        assertEquals(Trade.buyAt(5, ZonedDateTime.now(), NaN, NaN), record.getLastEntry());
        assertEquals(Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN), record.getLastExit());
    }

    @Test
    public void isClosed() {
        assertTrue(emptyRecord.isClosed());
        assertFalse(openedRecord.isClosed());
        assertTrue(closedRecord.isClosed());
    }

    @Test
    public void getPositionCount() {
        assertEquals(0, emptyRecord.getPositionCount());
        assertEquals(1, openedRecord.getPositionCount());
        assertEquals(2, closedRecord.getPositionCount());
    }

    @Test
    public void getLastPosition() {
        assertNull(emptyRecord.getLastPosition());
        assertEquals(new Position(Trade.buyAt(0, ZonedDateTime.now(), NaN, NaN),
                Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN)), openedRecord.getLastPosition());
        assertEquals(new Position(Trade.buyAt(7, ZonedDateTime.now(), NaN, NaN),
                Trade.sellAt(8, ZonedDateTime.now(), NaN, NaN)), closedRecord.getLastPosition());
    }

    @Test
    public void getLastTrade() {
        // Last trade
        assertNull(emptyRecord.getLastTrade());
        assertEquals(Trade.buyAt(7, ZonedDateTime.now(), NaN, NaN), openedRecord.getLastTrade());
        assertEquals(Trade.sellAt(8, ZonedDateTime.now(), NaN, NaN), closedRecord.getLastTrade());
        // Last BUY trade
        assertNull(emptyRecord.getLastTrade(Trade.TradeType.BUY));
        assertEquals(Trade.buyAt(7, ZonedDateTime.now(), NaN, NaN), openedRecord.getLastTrade(Trade.TradeType.BUY));
        assertEquals(Trade.buyAt(7, ZonedDateTime.now(), NaN, NaN), closedRecord.getLastTrade(Trade.TradeType.BUY));
        // Last SELL trade
        assertNull(emptyRecord.getLastTrade(Trade.TradeType.SELL));
        assertEquals(Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN), openedRecord.getLastTrade(Trade.TradeType.SELL));
        assertEquals(Trade.sellAt(8, ZonedDateTime.now(), NaN, NaN), closedRecord.getLastTrade(Trade.TradeType.SELL));
    }

    @Test
    public void getLastEntryExit() {
        // Last entry
        assertNull(emptyRecord.getLastEntry());
        assertEquals(Trade.buyAt(7, ZonedDateTime.now(), NaN, NaN), openedRecord.getLastEntry());
        assertEquals(Trade.buyAt(7, ZonedDateTime.now(), NaN, NaN), closedRecord.getLastEntry());
        // Last exit
        assertNull(emptyRecord.getLastExit());
        assertEquals(Trade.sellAt(3, ZonedDateTime.now(), NaN, NaN), openedRecord.getLastExit());
        assertEquals(Trade.sellAt(8, ZonedDateTime.now(), NaN, NaN), closedRecord.getLastExit());
    }

    @Test
    public void getNetProfitIsZero() {
        assertEquals(DoubleNum.valueOf(0), emptyRecord.getNetProfit());
        assertEquals(DoubleNum.valueOf(0), openedRecord.getNetProfit());
        assertEquals(DoubleNum.valueOf(0), closedRecord.getNetProfit());
    }

    @Test
    public void getNetProfitIsNonZero() {
        Trade buyOrder = Trade.buyAt(0, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));
        Trade sellOrder = Trade.sellAt(1, ZonedDateTime.now(), DoubleNum.valueOf(2), DoubleNum.valueOf(1));

        TradingRecord tradingRecord = new BaseTradingRecord(buyOrder, sellOrder);

        assertEquals(DoubleNum.valueOf(1), tradingRecord.getNetProfit());
    }

    @Test
    public void getNetProfitForSellEntry() {
        double transactionCost = 0.025;
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.SELL,
                new FixedTransactionCostModel(transactionCost));
        assertTrue(tradingRecord.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(2), DoubleNum.valueOf(1)));
        assertTrue(tradingRecord.exit(1, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1)));
        assertEquals(transactionCost, tradingRecord.getTransactionCostModel().getRawCostValue(), 0.000001);

        TestUtils.assertNumEquals(0.95, tradingRecord.getNetProfit());
    }

    @Test
    public void getNetProfitForNumberOfMostRecentTrades() {
        double transactionCost = 0.025;
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.BUY,
                new FixedTransactionCostModel(transactionCost));
        assertTrue(tradingRecord.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1)));
        assertTrue(tradingRecord.exit(1, ZonedDateTime.now(), DoubleNum.valueOf(2), DoubleNum.valueOf(1)));
        assertTrue(tradingRecord.enter(2, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1)));
        assertTrue(tradingRecord.exit(3, ZonedDateTime.now(), DoubleNum.valueOf(3), DoubleNum.valueOf(1)));
        assertEquals(transactionCost, tradingRecord.getTransactionCostModel().getRawCostValue(), 0.000001);

        TestUtils.assertNumEquals(1.95, tradingRecord.getNetProfit(1));
    }

    @Test
    public void getNetProfitForNumberOfMostRecentTradesIsTheSameAsNotSpecifying() {
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.BUY);
        assertTrue(tradingRecord.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1)));
        assertTrue(tradingRecord.exit(1, ZonedDateTime.now(), DoubleNum.valueOf(2), DoubleNum.valueOf(1)));
        assertTrue(tradingRecord.enter(2, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1)));
        assertTrue(tradingRecord.exit(3, ZonedDateTime.now(), DoubleNum.valueOf(3), DoubleNum.valueOf(1)));
        assertEquals(0, tradingRecord.getTransactionCostModel().getRawCostValue(), 0.000001);

        assertEquals(tradingRecord.getNetProfit(), tradingRecord.getNetProfit(2));
    }

    @Test
    public void getNetProfitForNumberOfMostRecentTradesIsTheSameAsNotSpecifyingWhenNumberIsMoreThanCount() {
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.BUY);
        assertTrue(tradingRecord.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1)));
        assertTrue(tradingRecord.exit(1, ZonedDateTime.now(), DoubleNum.valueOf(2), DoubleNum.valueOf(1)));
        assertTrue(tradingRecord.enter(2, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1)));
        assertTrue(tradingRecord.exit(3, ZonedDateTime.now(), DoubleNum.valueOf(3), DoubleNum.valueOf(1)));
        assertEquals(0, tradingRecord.getTransactionCostModel().getRawCostValue(), 0.000001);
        assertEquals(0, tradingRecord.getHoldingCostModel().getRawCostValue(), 0.000001);

        assertEquals(tradingRecord.getNetProfit(), tradingRecord.getNetProfit(5));
    }

    @Test
    public void getPercentageProfitableTrades() {
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.BUY);
        tradingRecord.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));
        tradingRecord.exit(1, ZonedDateTime.now(), DoubleNum.valueOf(2), DoubleNum.valueOf(1));

        tradingRecord.enter(2, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));
        tradingRecord.exit(3, ZonedDateTime.now(), DoubleNum.valueOf(0), DoubleNum.valueOf(1));

        tradingRecord.enter(4, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));
        tradingRecord.exit(5, ZonedDateTime.now(), DoubleNum.valueOf(3), DoubleNum.valueOf(1));

        tradingRecord.enter(6, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));
        tradingRecord.exit(7, ZonedDateTime.now(), DoubleNum.valueOf(0.5), DoubleNum.valueOf(1));

        TestUtils.assertNumEquals(0.5, tradingRecord.getPercentageProfitableTrades());
    }

    @Test
    public void getPercentageProfitableTradesWhenOnlyTradeIsNotClosed() {
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.BUY);
        tradingRecord.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));

        assertEquals(DoubleNum.valueOf(0), tradingRecord.getPercentageProfitableTrades());
    }

    @Test
    public void getPercentageProfitableTradesWhenNoTradesExist() {
        TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.BUY);

        assertEquals(DoubleNum.valueOf(0), tradingRecord.getPercentageProfitableTrades());
    }

    @Test
    public void testCompareTo() {
        // 25% profitable trades, 1.5 net profit, 4 trades
        TradingRecord tradingRecord1 = new BaseTradingRecord(Trade.TradeType.BUY);
        tradingRecord1.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));
        tradingRecord1.exit(1, ZonedDateTime.now(), DoubleNum.valueOf(4.5), DoubleNum.valueOf(1));

        tradingRecord1.enter(2, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));
        tradingRecord1.exit(3, ZonedDateTime.now(), DoubleNum.valueOf(0), DoubleNum.valueOf(1));

        tradingRecord1.enter(4, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));
        tradingRecord1.exit(5, ZonedDateTime.now(), DoubleNum.valueOf(0.5), DoubleNum.valueOf(1));

        tradingRecord1.enter(6, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));
        tradingRecord1.exit(7, ZonedDateTime.now(), DoubleNum.valueOf(0.5), DoubleNum.valueOf(1));

        TestUtils.assertNumEquals(0.25, tradingRecord1.getPercentageProfitableTrades());
        TestUtils.assertNumEquals(1.5, tradingRecord1.getNetProfit());
        assertEquals(4, tradingRecord1.getPositionCount());

        // 50% profitable trades, 1.5 net profit, 2 trades
        TradingRecord tradingRecord2 = new BaseTradingRecord(Trade.TradeType.BUY);
        tradingRecord2.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));
        tradingRecord2.exit(1, ZonedDateTime.now(), DoubleNum.valueOf(4), DoubleNum.valueOf(1));

        tradingRecord2.enter(2, ZonedDateTime.now(), DoubleNum.valueOf(2), DoubleNum.valueOf(1));
        tradingRecord2.exit(3, ZonedDateTime.now(), DoubleNum.valueOf(0.5), DoubleNum.valueOf(1));

        TestUtils.assertNumEquals(0.5, tradingRecord2.getPercentageProfitableTrades());
        TestUtils.assertNumEquals(1.5, tradingRecord2.getNetProfit());
        assertEquals(2, tradingRecord2.getPositionCount());

        // 100% profitable trades, 3.0 net profit, 2 trades
        TradingRecord tradingRecord3 = new BaseTradingRecord(Trade.TradeType.SELL);
        tradingRecord3.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(3), DoubleNum.valueOf(1));
        tradingRecord3.exit(1, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));

        tradingRecord3.enter(2, ZonedDateTime.now(), DoubleNum.valueOf(2), DoubleNum.valueOf(1));
        tradingRecord3.exit(3, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));

        TestUtils.assertNumEquals(1, tradingRecord3.getPercentageProfitableTrades());
        TestUtils.assertNumEquals(3.0, tradingRecord3.getNetProfit());
        assertEquals(2, tradingRecord3.getPositionCount());

        assertEquals(0, tradingRecord1.compareTo(tradingRecord2));
        assertEquals(-1, tradingRecord1.compareTo(tradingRecord3));
        assertEquals(1, tradingRecord3.compareTo(tradingRecord2));
    }
}
