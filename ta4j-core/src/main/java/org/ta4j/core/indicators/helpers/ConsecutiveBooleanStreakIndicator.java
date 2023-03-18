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
package org.ta4j.core.indicators.helpers;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * An indicator that returns the length of the current consecutive streak of
 * true or false values from another boolean indicator.
 */
public class ConsecutiveBooleanStreakIndicator extends CachedIndicator<Num> {

    private final Indicator<Boolean> indicator;
    private final boolean checkValue;

    /**
     * Constructor.
     *
     * @param indicator the boolean indicator to check for streaks
     * @param checkValue the value (true or false) for which to count
     * consecutive streaks
     */
    public ConsecutiveBooleanStreakIndicator(Indicator<Boolean> indicator, boolean checkValue) {
        super(indicator.getBarSeries());
        this.indicator = indicator;
        this.checkValue = checkValue;
    }

    /**
     * Calculates the length of the current consecutive streak of the
     * checkValue.
     *
     * @param index the bar index
     * @return the length of the streak as a Num
     */
    @Override
    protected Num calculate(int index) {
        int streak = 0;

        for (int i = index; i >= 0; i--) {
            if (this.indicator.getValue(i) != checkValue) {
                break;
            }

            streak++;
        }

        return this.indicator.numOf(streak);
    }
}