/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Marc de Verdelhan & respective authors (see AUTHORS)
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

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.RecursiveCachedIndicator;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

/**
 * Average of
 * {@link DirectionalMovementUpIndicator directional movement up indicator}.
 * <p>
 */
public class AverageDirectionalMovementUpIndicator extends RecursiveCachedIndicator<Num> {

    private final int timeFrame;

    private final DirectionalMovementUpIndicator dmup;

    public AverageDirectionalMovementUpIndicator(BarSeries series, int timeFrame) {
        super(series);
        this.timeFrame = timeFrame;
        dmup = new DirectionalMovementUpIndicator(series);
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return DoubleNum.valueOf(0);
        }
        Num nbPeriods = DoubleNum.valueOf(timeFrame);
        Num nbPeriodsMinusOne = DoubleNum.valueOf(timeFrame - 1);
        return getValue(index - 1).multipliedBy(nbPeriodsMinusOne).dividedBy(nbPeriods).plus(dmup.getValue(index).dividedBy(nbPeriods));
    }
}
