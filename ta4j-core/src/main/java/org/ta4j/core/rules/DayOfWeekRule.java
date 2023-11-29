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

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.helpers.DateTimeIndicator;

/**
 * Day of the week rule.
 *
 * Satisfied when the day of the week value of the DateTimeIndicator is equal to
 * one of the DayOfWeek varargs
 */
public class DayOfWeekRule extends AbstractRule {

    private final Set<DayOfWeek> daysOfWeekSet;
    private final DateTimeIndicator timeIndicator;

    public DayOfWeekRule(DateTimeIndicator timeIndicator, DayOfWeek... daysOfWeek) {
        this.daysOfWeekSet = new HashSet<>(Arrays.asList(daysOfWeek));
        this.timeIndicator = timeIndicator;
    }

    /** This rule does not use the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        ZonedDateTime dateTime = this.timeIndicator.getValue(index);
        boolean satisfied = daysOfWeekSet.contains(dateTime.getDayOfWeek());

        traceIsSatisfied(index, satisfied);
        return satisfied;
    }
}