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
package org.ta4j.core.indicators.helpers;

import java.time.ZonedDateTime;
import java.util.function.Function;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;

/**
 * DateTime indicator.
 */
public class DateTimeIndicator extends CachedIndicator<ZonedDateTime> {

    private final Function<Bar, ZonedDateTime> action;

    public DateTimeIndicator(BarSeries barSeries) {
        this(barSeries, Bar::getBeginTime);
    }

    public DateTimeIndicator(BarSeries barSeries, Function<Bar, ZonedDateTime> action) {
        super(barSeries);
        this.action = action;
    }

    @Override
    protected ZonedDateTime calculate(int index) {
        Bar bar = getBarSeries().getBar(index);
        return this.action.apply(bar);
    }
}
