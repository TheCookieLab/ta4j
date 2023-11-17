/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2022 Ta4j Organization & respective
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
package org.ta4j.core.rules;

import org.ta4j.core.Indicator;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 *
 * @author David
 */
public class PercentageOverRule extends AbstractRule {

    /**
     * The first indicator
     */
    public final Indicator<Num> first;
    /**
     * The second indicator
     */
    public final Indicator<Num> second;

    public final Num percentage;

    /**
     * Constructor. Satisfied when the difference between the first and second
     * indicators is greater (as a percentage) than the provided percentage
     * threshold
     *
     * @param first      the first indicator
     * @param second     the second indicator
     * @param percentage the difference between the first and second indicators as a
     *                   percentage tested if greater than this value
     */
    public PercentageOverRule(Indicator<Num> first, Indicator<Num> second, Num percentage) {
        this.first = first;
        this.second = second;
        this.percentage = percentage;
    }

    /**
     * * Satisfied when the difference between the first and second indicators is
     * greater (as a percentage) than the provided percentage threshold
     *
     * @param index
     * @param tradingRecord
     * @return
     */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        final boolean satisfied = (first.getValue(index).minus(second.getValue(index))).dividedBy(first.getValue(index))
                .isGreaterThan(percentage);
        return satisfied;
    }

    /**
     * * Satisfied when the difference between the first and second indicators is
     * greater (as a percentage) than the provided percentage threshold
     *
     * @param index
     * @param tradingRecord
     * @return
     */
    @Override
    public boolean isSatisfied(int index) {
        return isSatisfied(index, null);
    }

}
