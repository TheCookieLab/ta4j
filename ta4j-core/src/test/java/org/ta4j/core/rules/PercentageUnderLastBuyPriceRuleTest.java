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
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DoubleNum;

public class PercentageUnderLastBuyPriceRuleTest {

    @Test
    public void isSatisfied() {
        BarSeries series = new BaseBarSeries(10d, 9d, 8d, 3.4d, 3.3d, 3.2d, 3.1d, 3d, 1.15d, 1d, 0d);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        TradingRecord tradingRecord = new BaseTradingRecord();
        tradingRecord.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(8), DoubleNum.valueOf(1));

        PercentageUnderLastBuyPriceRule rule = new PercentageUnderLastBuyPriceRule(closePrice, DoubleNum.valueOf(0.1));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertTrue(rule.isSatisfied(2, tradingRecord));

        tradingRecord.exit(2, ZonedDateTime.now(), DoubleNum.valueOf(8), DoubleNum.valueOf(1));
        tradingRecord.enter(2, ZonedDateTime.now(), DoubleNum.valueOf(2.7), DoubleNum.valueOf(1));

        assertFalse(rule.isSatisfied(3, tradingRecord));
        assertFalse(rule.isSatisfied(4, tradingRecord));
        assertFalse(rule.isSatisfied(5, tradingRecord));
        assertFalse(rule.isSatisfied(6, tradingRecord));
        assertTrue(rule.isSatisfied(7, tradingRecord));

        tradingRecord.exit(7, ZonedDateTime.now(), DoubleNum.valueOf(3), DoubleNum.valueOf(1));
        tradingRecord.enter(7, ZonedDateTime.now(), DoubleNum.valueOf(2), DoubleNum.valueOf(1));

        assertTrue(rule.isSatisfied(8, tradingRecord));
        assertTrue(rule.isSatisfied(9, tradingRecord));
    }
}
