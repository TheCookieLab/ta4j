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

import java.time.Duration;
import java.time.ZonedDateTime;

import org.ta4j.core.num.Num;

public class BaseBarBuilder {

    private Duration timePeriod;
    private ZonedDateTime endTime;
    private Num openPrice;
    private Num closePrice;
    private Num highPrice;
    private Num lowPrice;
    private Num amount;
    private Num volume;
    private long trades;

    BaseBarBuilder() {
    }

    public BaseBarBuilder timePeriod(Duration timePeriod) {
        this.timePeriod = timePeriod;
        return this;
    }

    public BaseBarBuilder endTime(ZonedDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public BaseBarBuilder openPrice(Num openPrice) {
        this.openPrice = openPrice;
        return this;
    }

    public BaseBarBuilder closePrice(Num closePrice) {
        this.closePrice = closePrice;
        return this;
    }

    public BaseBarBuilder highPrice(Num highPrice) {
        this.highPrice = highPrice;
        return this;
    }

    public BaseBarBuilder lowPrice(Num lowPrice) {
        this.lowPrice = lowPrice;
        return this;
    }

    public BaseBarBuilder amount(Num amount) {
        this.amount = amount;
        return this;
    }

    public BaseBarBuilder volume(Num volume) {
        this.volume = volume;
        return this;
    }

    public BaseBarBuilder trades(long trades) {
        this.trades = trades;
        return this;
    }

    public BaseBar build() {
        return new BaseBar(timePeriod, endTime, openPrice, highPrice, lowPrice, closePrice, volume, amount, trades);
    }
}
