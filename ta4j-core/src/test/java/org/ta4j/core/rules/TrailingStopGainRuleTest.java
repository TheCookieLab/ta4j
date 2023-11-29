/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
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
/*
  The MIT License (MIT)

  Copyright (c) 2014-2017 Marc de Verdelhan & respective authors (see AUTHORS)

  Permission is hereby granted, free of charge, to any person obtaining a copy of
  this software and associated documentation files (the "Software"), to deal in
  the Software without restriction, including without limitation the rights to
  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
  the Software, and to permit persons to whom the Software is furnished to do so,
  subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.rules;

import java.time.ZonedDateTime;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

public class TrailingStopGainRuleTest extends AbstractIndicatorTest<Object, Object> {

    public TrailingStopGainRuleTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Test
    public void testBuyIsSatisfied() {
        BarSeries series = new BaseBarSeries(100d, 101d, 102d, 103d, 104d, 105d, 106d, 107d, 108d, 109d, 110d, 111d,
                112d, 113d, 114d, 115d, 114d, 113d, 112d, 111d, 110d, 109d);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        TrailingStopGainRule rule = new TrailingStopGainRule(closePrice, DoubleNum.valueOf(10), DoubleNum.valueOf(5));

        TradingRecord tradingRecord = new BaseTradingRecord();
        tradingRecord.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(100), DoubleNum.valueOf(1));

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
        assertFalse(rule.isSatisfied(10, tradingRecord)); // Trailing Stop initiated

        assertFalse(rule.isSatisfied(11, tradingRecord));
        assertFalse(rule.isSatisfied(12, tradingRecord));
        assertFalse(rule.isSatisfied(13, tradingRecord));
        assertFalse(rule.isSatisfied(14, tradingRecord));
        assertFalse(rule.isSatisfied(15, tradingRecord));

        assertFalse(rule.isSatisfied(16, tradingRecord));
        assertFalse(rule.isSatisfied(17, tradingRecord));
        assertFalse(rule.isSatisfied(18, tradingRecord));
        assertFalse(rule.isSatisfied(19, tradingRecord));
        assertFalse(rule.isSatisfied(20, tradingRecord));

        assertTrue(rule.isSatisfied(21, tradingRecord));
    }

    @Test
    public void testSellIsSatisfied() {
        BarSeries series = new BaseBarSeries(100d, 99d, 98d, 97d, 96d, 95d, 94d, 93d, 92d, 91d, 90d, 91d, 92d, 93d, 94d,
                95d);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        TrailingStopGainRule rule = new TrailingStopGainRule(closePrice, DoubleNum.valueOf(10), DoubleNum.valueOf(5));

        TradingRecord tradingRecord = new BaseTradingRecord(Trade.TradeType.SELL);
        tradingRecord.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(100), DoubleNum.valueOf(1));

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
        assertFalse(rule.isSatisfied(10, tradingRecord)); // Trailing Stop initiated

        assertFalse(rule.isSatisfied(11, tradingRecord));
        assertFalse(rule.isSatisfied(12, tradingRecord));
        assertFalse(rule.isSatisfied(13, tradingRecord));
        assertFalse(rule.isSatisfied(14, tradingRecord));
        assertTrue(rule.isSatisfied(15, tradingRecord));
    }
}