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
package org.ta4j.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

public class BarTest extends AbstractIndicatorTest<BarSeries, Num> {

    private Bar bar;

    private ZonedDateTime beginTime;

    private ZonedDateTime endTime;

    public BarTest(Function<Number, Num> numFunction) {
        super(null, numFunction);
    }

    @Before
    public void setUp() {
        beginTime = ZonedDateTime.of(2014, 6, 25, 0, 0, 0, 0, ZoneId.systemDefault());
        endTime = ZonedDateTime.of(2014, 6, 25, 1, 0, 0, 0, ZoneId.systemDefault());
        bar = new BaseBar(Duration.ofHours(1), endTime, numFunction);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void addTrades() {

        bar.addTrade(3.0, 200.0, numFunction);
        bar.addTrade(4.0, 201.0, numFunction);
        bar.addTrade(2.0, 198.0, numFunction);

        assertEquals(3, bar.getTrades());
        assertNumEquals(3 * 200 + 4 * 201 + 2 * 198, bar.getAmount());
        assertNumEquals(200, bar.getOpenPrice());
        assertNumEquals(198, bar.getClosePrice());
        assertNumEquals(198, bar.getLowPrice());
        assertNumEquals(201, bar.getHighPrice());
        assertNumEquals(9, bar.getVolume());
    }

    @Test
    public void addTrade() {
        Num openPrice = DoubleNum.valueOf(10);
        Num highPrice = DoubleNum.valueOf(15);
        Num lowPrice = DoubleNum.valueOf(5);
        Num closePrice = DoubleNum.valueOf(12);
        Num volume = DoubleNum.valueOf(50);
        Num amount = DoubleNum.valueOf(2);
        long trades = 0;

        Bar subject = new BaseBar(Duration.ofDays(1), ZonedDateTime.now(), openPrice, highPrice, lowPrice, closePrice,
                volume, amount, trades);

        Num tradePricePerAsset = DoubleNum.valueOf(197);
        Num tradeAmount = DoubleNum.valueOf(1);
        Trade trade = new Trade(0, ZonedDateTime.now(), Trade.TradeType.BUY, tradePricePerAsset, tradeAmount);
        subject.addTrade(trade);

        assertEquals(1, subject.getTrades());
        assertNumEquals(amount.plus(tradePricePerAsset), subject.getAmount());
        assertNumEquals(openPrice, subject.getOpenPrice());
        assertNumEquals(tradePricePerAsset, subject.getClosePrice());
        assertNumEquals(lowPrice, subject.getLowPrice());
        assertNumEquals(tradePricePerAsset, subject.getHighPrice());
        assertNumEquals(volume.plus(tradeAmount), subject.getVolume());
    }

    @Test
    public void getTimePeriod() {
        assertEquals(beginTime, bar.getEndTime().minus(bar.getTimePeriod()));
    }

    @Test
    public void getBeginTime() {
        assertEquals(beginTime, bar.getBeginTime());
    }

    @Test
    public void inPeriod() {
        assertFalse(bar.inPeriod(null));

        assertFalse(bar.inPeriod(beginTime.withDayOfMonth(24)));
        assertFalse(bar.inPeriod(beginTime.withDayOfMonth(26)));
        assertTrue(bar.inPeriod(beginTime.withMinute(30)));

        assertTrue(bar.inPeriod(beginTime));
        assertFalse(bar.inPeriod(endTime));
    }

    @Test
    public void equals() {
        Bar bar1 = new BaseBar(Duration.ofHours(1), endTime, numFunction);
        Bar bar2 = new BaseBar(Duration.ofHours(1), endTime, numFunction);

        assertEquals(bar1, bar2);
    }

    @Test
    public void hashCode2() {
        Bar bar1 = new BaseBar(Duration.ofHours(1), endTime, numFunction);
        Bar bar2 = new BaseBar(Duration.ofHours(1), endTime, numFunction);

        assertEquals(bar1.hashCode(), bar2.hashCode());
    }
}
