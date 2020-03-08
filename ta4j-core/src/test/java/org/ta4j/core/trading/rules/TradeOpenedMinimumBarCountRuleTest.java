/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Marc de Verdelhan, 2017-2020 Ta4j Organization & respective
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
package org.ta4j.core.trading.rules;

import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Order;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.PrecisionNum;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TradeOpenedMinimumBarCountRuleTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAtLeastBarCountRuleForNegativeNumberShouldThrowException() {
        new TradeOpenedMinimumBarCountRule(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAtLeastBarCountRuleForZeroShouldThrowException() {
        new TradeOpenedMinimumBarCountRule(0);
    }

    @Test
    public void testAtLeastOneBarRuleForOpenedTrade() {
        final TradeOpenedMinimumBarCountRule rule = new TradeOpenedMinimumBarCountRule(1);

        final BarSeries series = new MockBarSeries(PrecisionNum::valueOf, 1, 2, 3, 4);

        final TradingRecord tradingRecord = new BaseTradingRecord(Order.buyAt(0, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertTrue(rule.isSatisfied(1, tradingRecord));
        assertTrue(rule.isSatisfied(2, tradingRecord));
        assertTrue(rule.isSatisfied(3, tradingRecord));
    }

    @Test
    public void testAtLeastMoreThanOneBarRuleForOpenedTrade() {
        final TradeOpenedMinimumBarCountRule rule = new TradeOpenedMinimumBarCountRule(2);

        final BarSeries series = new MockBarSeries(PrecisionNum::valueOf, 1, 2, 3, 4);

        final TradingRecord tradingRecord = new BaseTradingRecord(Order.buyAt(0, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertTrue(rule.isSatisfied(2, tradingRecord));
        assertTrue(rule.isSatisfied(3, tradingRecord));
    }

    @Test
    public void testAtLeastBarCountRuleForClosedTradeShouldAlwaysReturnsFalse() {
        final TradeOpenedMinimumBarCountRule rule = new TradeOpenedMinimumBarCountRule(1);

        final BarSeries series = new MockBarSeries(PrecisionNum::valueOf, 1, 2, 3, 4);

        final TradingRecord tradingRecord = new BaseTradingRecord(Order.buyAt(0, series), Order.sellAt(1, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertFalse(rule.isSatisfied(3, tradingRecord));
    }

    @Test
    public void testAtLeastBarCountRuleForEmptyTradingRecordShouldAlwaysReturnsFalse() {
        final TradeOpenedMinimumBarCountRule rule = new TradeOpenedMinimumBarCountRule(1);

        final TradingRecord tradingRecord = new BaseTradingRecord();

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertFalse(rule.isSatisfied(3, tradingRecord));
    }
}