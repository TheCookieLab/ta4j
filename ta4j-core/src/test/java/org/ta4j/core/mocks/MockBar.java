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
package org.ta4j.core.mocks;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.function.Function;

import org.ta4j.core.BaseBar;
import org.ta4j.core.num.Num;

/**
 * A mock bar with sample data.
 */
public class MockBar extends BaseBar {

    private static final long serialVersionUID = -4546486893163810212L;

    private long trades = 0;

    public MockBar(double closePrice, Function<Number, Num> numFunction) {
        this(ZonedDateTime.now(), closePrice, numFunction);
    }

    public MockBar(double closePrice, double volume, Function<Number, Num> numFunction) {
        super(Duration.ofDays(1), ZonedDateTime.now(), 0, 0, 0, closePrice, volume, 0, 0, numFunction);
    }

    public MockBar(ZonedDateTime endTime, double closePrice, Function<Number, Num> numFunction) {
        super(Duration.ofDays(1), endTime, 0, 0, 0, closePrice, 0, 0, 0, numFunction);
    }

    public MockBar(ZonedDateTime endTime, double closePrice, double volume, Function<Number, Num> numFunction) {
        super(Duration.ofDays(1), endTime, 0, 0, 0, closePrice, volume, 0, 0, numFunction);
    }

    public MockBar(double openPrice, double closePrice, double highPrice, double lowPrice,
            Function<Number, Num> numFunction) {
        super(Duration.ofDays(1), ZonedDateTime.now(), openPrice, highPrice, lowPrice, closePrice, 1, 0, 0,
                numFunction);
    }

    public MockBar(double openPrice, double closePrice, double highPrice, double lowPrice, double volume,
            Function<Number, Num> numFunction) {
        super(Duration.ofDays(1), ZonedDateTime.now(), openPrice, highPrice, lowPrice, closePrice, volume, 0, 0,
                numFunction);
    }

    public MockBar(ZonedDateTime endTime, double openPrice, double closePrice, double highPrice, double lowPrice,
            double amount, double volume, long trades, Function<Number, Num> numFunction) {
        super(Duration.ofDays(1), endTime, openPrice, highPrice, lowPrice, closePrice, volume, amount, 0, numFunction);
        this.trades = trades;
    }

    @Override
    public long getTrades() {
        return trades;
    }
}
