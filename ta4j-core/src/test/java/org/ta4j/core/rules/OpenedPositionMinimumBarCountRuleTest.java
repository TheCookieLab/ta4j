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
package org.ta4j.core.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.DecimalNum;

public class OpenedPositionMinimumBarCountRuleTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAtLeastBarCountRuleForNegativeNumberShouldThrowException() {
        new OpenedPositionMinimumBarCountRule(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAtLeastBarCountRuleForZeroShouldThrowException() {
        new OpenedPositionMinimumBarCountRule(0);
    }

    @Test
    public void testAtLeastOneBarRuleForOpenedTrade() {
        final OpenedPositionMinimumBarCountRule rule = new OpenedPositionMinimumBarCountRule(1);

        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 1, 2, 3, 4);

        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertTrue(rule.isSatisfied(1, tradingRecord));
        assertTrue(rule.isSatisfied(2, tradingRecord));
        assertTrue(rule.isSatisfied(3, tradingRecord));
    }

    @Test
    public void testAtLeastMoreThanOneBarRuleForOpenedTrade() {
        final OpenedPositionMinimumBarCountRule rule = new OpenedPositionMinimumBarCountRule(2);

        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 1, 2, 3, 4);

        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertTrue(rule.isSatisfied(2, tradingRecord));
        assertTrue(rule.isSatisfied(3, tradingRecord));
    }

    @Test
    public void testAtLeastBarCountRuleForClosedTradeShouldAlwaysReturnsFalse() {
        final OpenedPositionMinimumBarCountRule rule = new OpenedPositionMinimumBarCountRule(1);

        final BarSeries series = new MockBarSeries(DecimalNum::valueOf, 1, 2, 3, 4);

        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertFalse(rule.isSatisfied(3, tradingRecord));
    }

    @Test
    public void testAtLeastBarCountRuleForEmptyTradingRecordShouldAlwaysReturnsFalse() {
        final OpenedPositionMinimumBarCountRule rule = new OpenedPositionMinimumBarCountRule(1);

        final TradingRecord tradingRecord = new BaseTradingRecord();

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertFalse(rule.isSatisfied(3, tradingRecord));
    }
}
