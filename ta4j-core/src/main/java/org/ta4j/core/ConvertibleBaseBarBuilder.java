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
import java.util.function.Function;

import org.ta4j.core.num.Num;

public class ConvertibleBaseBarBuilder<T> extends BaseBarBuilder {

    private final Function<T, Num> conversionFunction;

    public ConvertibleBaseBarBuilder(Function<T, Num> conversionFunction) {
        this.conversionFunction = conversionFunction;
    }

    @Override
    public ConvertibleBaseBarBuilder<T> timePeriod(Duration timePeriod) {
        super.timePeriod(timePeriod);
        return this;
    }

    @Override
    public ConvertibleBaseBarBuilder<T> endTime(ZonedDateTime endTime) {
        super.endTime(endTime);
        return this;
    }

    @Override
    public ConvertibleBaseBarBuilder<T> trades(long trades) {
        super.trades(trades);
        return this;
    }

    public ConvertibleBaseBarBuilder<T> openPrice(T openPrice) {
        super.openPrice(conversionFunction.apply(openPrice));
        return this;
    }

    public ConvertibleBaseBarBuilder<T> highPrice(T highPrice) {
        super.highPrice(conversionFunction.apply(highPrice));
        return this;
    }

    public ConvertibleBaseBarBuilder<T> lowPrice(T lowPrice) {
        super.lowPrice(conversionFunction.apply(lowPrice));
        return this;
    }

    public ConvertibleBaseBarBuilder<T> closePrice(T closePrice) {
        super.closePrice(conversionFunction.apply(closePrice));
        return this;
    }

    public ConvertibleBaseBarBuilder<T> amount(T amount) {
        super.amount(conversionFunction.apply(amount));
        return this;
    }

    public ConvertibleBaseBarBuilder<T> volume(T volume) {
        super.volume(conversionFunction.apply(volume));
        return this;
    }
}
