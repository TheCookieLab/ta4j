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

import java.time.ZonedDateTime;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class DollarStopLossRuleTest extends AbstractIndicatorTest<BarSeries, Num> {

    private ClosePriceIndicator closePrice;

    public DollarStopLossRuleTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        closePrice = new ClosePriceIndicator(new MockBarSeries(numFunction, 110, 105, 110, 120, 100, 150, 110, 146));
    }

    @Test
    public void isSatisfiedWorksForBuy() {
        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.BUY);
        final Num tradedAmount = numOf(1);

        // $5 stop-loss
        DollarStopLossRule rule = new DollarStopLossRule(closePrice, numOf(5));

        assertFalse(rule.isSatisfied(0, null));
        assertFalse(rule.isSatisfied(1, tradingRecord));

        // Enter at 110
        tradingRecord.enter(0, ZonedDateTime.now(), numOf(110), tradedAmount);
        assertTrue(rule.isSatisfied(1, tradingRecord)); // 105 == $5 loss
        assertFalse(rule.isSatisfied(2, tradingRecord)); // 110 == breakeven
        assertFalse(rule.isSatisfied(3, tradingRecord)); // 120 == $10 profit
        // Exit
        tradingRecord.exit(4);

        // Enter at 150
        tradingRecord.enter(5, ZonedDateTime.now(), numOf(150), tradedAmount);
        assertFalse(rule.isSatisfied(5, tradingRecord));
        assertTrue(rule.isSatisfied(6, tradingRecord)); // 110 == $40 loss
        assertFalse(rule.isSatisfied(7, tradingRecord)); // 146 == $4 loss
    }

    @Test
    public void isSatisfiedWorksForSell() {
        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.SELL);
        final Num tradedAmount = numOf(1);

        // $5 stop-loss
        DollarStopLossRule rule = new DollarStopLossRule(closePrice, numOf(5));

        assertFalse(rule.isSatisfied(0, null));
        assertFalse(rule.isSatisfied(1, tradingRecord));

        // Enter at 105
        tradingRecord.enter(1, ZonedDateTime.now(), numOf(105), tradedAmount);
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertTrue(rule.isSatisfied(2, tradingRecord)); // 110 == $5 loss
        assertTrue(rule.isSatisfied(3, tradingRecord)); // 120 == $15 loss
        // Exit
        tradingRecord.exit(4);

        // Enter at 114
        tradingRecord.enter(2, ZonedDateTime.now(), numOf(114), tradedAmount);
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertTrue(rule.isSatisfied(3, tradingRecord));
        assertFalse(rule.isSatisfied(4, tradingRecord));
        assertTrue(rule.isSatisfied(5, tradingRecord));
    }
}
