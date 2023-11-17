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
package org.ta4j.core.rules;

import java.time.ZonedDateTime;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.cost.LinearTransactionCostModel;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.DecimalNum;

public class OpenedPositionMinimumProfitableBarCountRuleTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAtLeastBarCountRuleForNegativeNumberShouldThrowException() {
        new OpenedPositionMinimumProfitableBarCountRule(new BaseBarSeries(), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAtLeastBarCountRuleForZeroShouldThrowException() {
        new OpenedPositionMinimumProfitableBarCountRule(new BaseBarSeries(), 0);
    }

    @Test
    public void testAtLeastOneBarRuleForOpenedTrade() {
        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 1, 2, 3, 4);
        final OpenedPositionMinimumProfitableBarCountRule rule = new OpenedPositionMinimumProfitableBarCountRule(series,
                1);

        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertTrue(rule.isSatisfied(1, tradingRecord));
        assertTrue(rule.isSatisfied(2, tradingRecord));
        assertTrue(rule.isSatisfied(3, tradingRecord));
    }

    @Test
    public void testAtLeastMoreThanOneBarRuleForOpenedTrade() {
        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 1, 2, 3, 4);
        final OpenedPositionMinimumProfitableBarCountRule rule = new OpenedPositionMinimumProfitableBarCountRule(series,
                2);

        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertTrue(rule.isSatisfied(2, tradingRecord));
        assertTrue(rule.isSatisfied(3, tradingRecord));
    }

    @Test
    public void testThreeBarRuleForOpenedTrade() {
        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 1, 2, 3, 4);
        final OpenedPositionMinimumProfitableBarCountRule rule = new OpenedPositionMinimumProfitableBarCountRule(series,
                3);

        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertTrue(rule.isSatisfied(3, tradingRecord));
    }

    @Test
    public void testIsSatisfiedOnDecliningPrices() {
        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        final OpenedPositionMinimumProfitableBarCountRule rule = new OpenedPositionMinimumProfitableBarCountRule(series,
                1);

        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertFalse(rule.isSatisfied(3, tradingRecord));
        assertFalse(rule.isSatisfied(4, tradingRecord));
        assertFalse(rule.isSatisfied(5, tradingRecord));
        assertFalse(rule.isSatisfied(6, tradingRecord));
        assertFalse(rule.isSatisfied(7, tradingRecord));
        assertFalse(rule.isSatisfied(8, tradingRecord));
        assertFalse(rule.isSatisfied(9, tradingRecord));
    }

    @Test
    public void testIsSatisfiedOnMixedPrices() {
        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 10, 11, 12, 9, 10, 11, 12, 13, 9, 10);
        final OpenedPositionMinimumProfitableBarCountRule rule = new OpenedPositionMinimumProfitableBarCountRule(series,
                3);

        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertFalse(rule.isSatisfied(3, tradingRecord));
        assertFalse(rule.isSatisfied(4, tradingRecord));
        assertFalse(rule.isSatisfied(5, tradingRecord));
        assertFalse(rule.isSatisfied(6, tradingRecord));
        assertTrue(rule.isSatisfied(7, tradingRecord));
        assertFalse(rule.isSatisfied(8, tradingRecord));
        assertFalse(rule.isSatisfied(9, tradingRecord));
    }

    @Test
    public void testIsSatisfiedOnMixedPrices2() {
        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 10, 11, 10, 9, 10, 10, 10, 9, 11, 11);
        final OpenedPositionMinimumProfitableBarCountRule rule = new OpenedPositionMinimumProfitableBarCountRule(series,
                2);

        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertFalse(rule.isSatisfied(3, tradingRecord));
        assertFalse(rule.isSatisfied(4, tradingRecord));
        assertFalse(rule.isSatisfied(5, tradingRecord));
        assertFalse(rule.isSatisfied(6, tradingRecord));
        assertFalse(rule.isSatisfied(7, tradingRecord));
        assertFalse(rule.isSatisfied(8, tradingRecord));
        assertTrue(rule.isSatisfied(9, tradingRecord));
    }

    @Test
    public void testIsSatisfiedOnSellEntry() {
        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 10, 11, 10, 9, 10, 10, 10, 9, 9, 8);
        final OpenedPositionMinimumProfitableBarCountRule rule = new OpenedPositionMinimumProfitableBarCountRule(series,
                2);

        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.SELL,
                new LinearTransactionCostModel(0.01));
        tradingRecord.enter(0, ZonedDateTime.now(), series.getBar(0).getClosePrice(), series.numOf(1));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertFalse(rule.isSatisfied(3, tradingRecord));
        assertFalse(rule.isSatisfied(4, tradingRecord));
        assertFalse(rule.isSatisfied(5, tradingRecord));
        assertFalse(rule.isSatisfied(6, tradingRecord));
        assertFalse(rule.isSatisfied(7, tradingRecord));
        assertTrue(rule.isSatisfied(8, tradingRecord));
        assertTrue(rule.isSatisfied(9, tradingRecord));
    }

    @Test
    public void testAtLeastBarCountRuleForClosedTradeShouldAlwaysReturnsFalse() {
        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 1, 2, 3, 4);
        final OpenedPositionMinimumProfitableBarCountRule rule = new OpenedPositionMinimumProfitableBarCountRule(series,
                1);

        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertFalse(rule.isSatisfied(3, tradingRecord));
    }

    @Test
    public void testAtLeastBarCountRuleForEmptyTradingRecordShouldAlwaysReturnsFalse() {
        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 1, 2, 3, 4);
        final OpenedPositionMinimumProfitableBarCountRule rule = new OpenedPositionMinimumProfitableBarCountRule(series,
                1);

        final TradingRecord tradingRecord = new BaseTradingRecord();

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertFalse(rule.isSatisfied(3, tradingRecord));
    }
}
