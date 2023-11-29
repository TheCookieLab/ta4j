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

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.helpers.DateTimeIndicator;

/**
 * Time range rule.
 *
 * Satisfied when the local time value of the DateTimeIndicator is within a time
 * range specified in the TimeRange list.
 */
public class TimeRangeRule extends AbstractRule {

    private final List<TimeRange> timeRanges;
    private final DateTimeIndicator timeIndicator;

    public TimeRangeRule(List<TimeRange> timeRanges, DateTimeIndicator beginTimeIndicator) {
        this.timeRanges = timeRanges;
        this.timeIndicator = beginTimeIndicator;
    }

    /** This rule does not use the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        boolean satisfied = false;
        ZonedDateTime dateTime = this.timeIndicator.getValue(index);
        LocalTime localTime = dateTime.toLocalTime();
        satisfied = this.timeRanges.stream()
                .anyMatch(
                        timeRange -> !localTime.isBefore(timeRange.getFrom()) && !localTime.isAfter(timeRange.getTo()));
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }

    public static class TimeRange {

        private final LocalTime from;
        private final LocalTime to;

        public TimeRange(LocalTime from, LocalTime to) {
            this.from = from;
            this.to = to;
        }

        public LocalTime getFrom() {
            return from;
        }

        public LocalTime getTo() {
            return to;
        }
    }
}
