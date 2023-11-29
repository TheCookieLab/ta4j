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

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertFalse;

import java.util.function.Function;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class ATRStopGainRuleTest extends AbstractIndicatorTest<BarSeries, Num> {

    private BarSeries series;

    public ATRStopGainRuleTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        List<Bar> bars = new ArrayList<>();
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 100, 101, 99, 100, 1, 0, 0, this::numOf));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 100, 101, 100, 100, 1, 0, 0, this::numOf));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 105, 110, 100, 102, 1, 0, 0, this::numOf));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 105, 120, 105, 115, 1, 0, 0, this::numOf));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 115, 125, 110, 110, 1, 0, 0, this::numOf));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 110, 110, 90, 90, 1, 0, 0, this::numOf));
        bars.add(new BaseBar(Duration.ZERO, ZonedDateTime.now(), 72, 72, 72, 72, 1, 0, 0, this::numOf));

        series = new MockBarSeries(bars);
    }

    @Test
    public void isSatisfiedWorksForBuy() {
        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.BUY);
        final Num tradedAmount = numOf(1);

        // 2 ATR stop gain
        ATRStopGainRule rule = new ATRStopGainRule(series, 1, 4);

        assertFalse(rule.isSatisfied(0, null));
        assertFalse(rule.isSatisfied(1, tradingRecord));

        // Enter at 102
        tradingRecord.enter(2, ZonedDateTime.now(), numOf(102), tradedAmount);
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertTrue(rule.isSatisfied(3, tradingRecord));
        assertFalse(rule.isSatisfied(4, tradingRecord));
        assertFalse(rule.isSatisfied(5, tradingRecord));

    }

    @Test
    public void isSatisfiedWorksForSell() {
        final TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.SELL);
        final Num tradedAmount = numOf(1);

        // 1 ATR stop-gain
        ATRStopGainRule rule = new ATRStopGainRule(series, 2, 4);

        assertFalse(rule.isSatisfied(0, null));
        assertFalse(rule.isSatisfied(1, tradingRecord));

        // Enter at 99
        tradingRecord.enter(0, ZonedDateTime.now(), numOf(99), tradedAmount);
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertFalse(rule.isSatisfied(2, tradingRecord));
        assertFalse(rule.isSatisfied(3, tradingRecord));
        assertFalse(rule.isSatisfied(4, tradingRecord));
        assertFalse(rule.isSatisfied(5, tradingRecord));
        assertTrue(rule.isSatisfied(6, tradingRecord));
    }
}
