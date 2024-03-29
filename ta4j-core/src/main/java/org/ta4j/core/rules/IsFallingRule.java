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
package org.ta4j.core.rules;

import org.ta4j.core.Indicator;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * Indicator-falling-indicator rule.
 *
 * Satisfied when the values of the {@link Indicator indicator} decrease within
 * the barCount.
 */
public class IsFallingRule extends AbstractRule {

    /** The actual indicator */
    private final Indicator<Num> ref;
    /** The barCount */
    private final int barCount;
    /** The minimum required strength of the falling */
    private double minStrength;

    /**
     * Constructor.
     * 
     * @param ref      the indicator
     * @param barCount the time frame
     */
    public IsFallingRule(Indicator<Num> ref, int barCount) {
        this(ref, barCount, 1.0);
    }

    /**
     * Constructor.
     * 
     * @param ref         the indicator
     * @param barCount    the time frame
     * @param minStrength the minimum required falling strength (between '0' and
     *                    '1', e.g. '1' for strict falling)
     */
    public IsFallingRule(Indicator<Num> ref, int barCount, double minStrength) {
        this.ref = ref;
        this.barCount = barCount;
        this.minStrength = minStrength;
    }

    /** This rule does not use the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (minStrength >= 1) {
            minStrength = 0.99;
        }

        int count = 0;
        for (int i = Math.max(0, index - barCount + 1); i <= index; i++) {
            if (ref.getValue(i).isLessThan(ref.getValue(Math.max(0, i - 1)))) {
                count += 1;
            }
        }

        double ratio = count / (double) barCount;

        final boolean satisfied = ratio >= minStrength;
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }
}
