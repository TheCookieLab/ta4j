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

import org.ta4j.core.TradingRecord;

/**
 *
 * @author David
 */
public class ModulusIndexRule extends AbstractRule {

    private final boolean satisfied;
    private final int modulus;

    /**
     * Constructor.
     *
     * @param modulus   If the current index mod the given modulus == 0, return
     *                  satisfied
     * @param satisfied true for the rule to be always satisfied, false to be never
     *                  satisfied
     */
    public ModulusIndexRule(int modulus, boolean satisfied) {
        this.satisfied = satisfied;
        this.modulus = modulus;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (index % modulus == 0) {
            return satisfied;
        }
        return !satisfied;
    }
}
