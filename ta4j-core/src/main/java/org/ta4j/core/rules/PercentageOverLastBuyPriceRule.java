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
 *
 * @author David
 */
public class PercentageOverLastBuyPriceRule extends AbstractRule {

    /**
     * The first indicator
     */
    public final Indicator<Num> first;

    public final Num percentage;

    /**
     * Constructor. Satisfied when the difference between the indicator value at
     * index i and the last trading record entry's price per asset is greater (as a
     * percentage) than the given percentage threshold
     *
     *
     * @param first      the first indicator
     * @param percentage 10% is expressed as 0.1
     */
    public PercentageOverLastBuyPriceRule(Indicator<Num> first, Num percentage) {
        this.first = first;
        this.percentage = percentage;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (tradingRecord == null || tradingRecord.getLastEntry() == null) {
            return false;
        }

        final boolean satisfied = (first.getValue(index).minus(tradingRecord.getLastEntry().getPricePerAsset()))
                .dividedBy(first.getValue(index))
                .isGreaterThan(percentage);
        return satisfied;
    }

}
