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
package org.ta4j.core.indicators;

import org.ta4j.core.Indicator;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

/**
 * The Zig Zag Indicator.
 * <p>
 * For more information, see
 * <a href="https://www.investopedia.com/terms/z/zig_zag_indicator.asp">Zig Zag
 * Indicator</a>.
 */
public class ZigZagIndicator extends CachedIndicator<Num> {

    /**
     * The threshold ratio (positive number).
     */
    private final Num thresholdRatio;

    /**
     * The indicator to provide values.
     * <p>
     * Can be {@link org.ta4j.core.indicators.helpers.ClosePriceIndicator} or
     * something similar.
     */
    private final Indicator<Num> indicator;

    private Num lastExtreme;

    /**
     * Constructs a ZigZagIndicator.
     *
     * @param indicator      the indicator to provide values
     * @param thresholdRatio the threshold ratio, must be positive
     */
    public ZigZagIndicator(Indicator<Num> indicator, Num thresholdRatio) {
        super(indicator);
        this.indicator = indicator;
        validateThreshold(thresholdRatio);
        this.thresholdRatio = thresholdRatio;
    }

    /**
     * Validates the provided threshold.
     *
     * @param threshold the threshold to validate
     * @throws IllegalArgumentException if threshold is null or non-positive
     */
    private void validateThreshold(Num threshold) {
        if (threshold == null) {
            throw new IllegalArgumentException("Threshold ratio is mandatory");
        }
        if (threshold.isNegativeOrZero()) {
            throw new IllegalArgumentException("Threshold ratio value must be positive");
        }
    }

    /**
     * Calculates the Zig Zag indicator's value for the current index.
     *
     * @param index the bar index
     * @return the indicator's value if its ratio from the last extreme is equal to
     *         or greater than the threshold, otherwise {@link NaN}
     */
    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            lastExtreme = indicator.getValue(0);
            return lastExtreme;
        } else {
            if (lastExtreme.isZero()) {
                // Treat this case separately
                // because one cannot divide by zero
                return NaN.NaN;
            } else {
                Num indicatorValue = indicator.getValue(index);
                Num differenceRatio = indicatorValue.minus(lastExtreme).dividedBy(lastExtreme);

                if (differenceRatio.abs().isGreaterThanOrEqual(thresholdRatio)) {
                    lastExtreme = indicatorValue;
                    return lastExtreme;
                } else {
                    return NaN.NaN;
                }
            }
        }
    }
}
