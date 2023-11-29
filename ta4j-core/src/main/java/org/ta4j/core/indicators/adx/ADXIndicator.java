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
package org.ta4j.core.indicators.adx;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.MMAIndicator;
import org.ta4j.core.num.Num;

/**
 * ADX indicator. Part of the Directional Movement System.
 *
 * @see <a href=
 *      "https://www.investopedia.com/terms/a/adx.asp">https://www.investopedia.com/terms/a/adx.asp</a>
 */
public class ADXIndicator extends CachedIndicator<Num> {

    private final MMAIndicator averageDXIndicator;
    private final int diBarCount;
    private final int adxBarCount;

    public ADXIndicator(BarSeries series, int diBarCount, int adxBarCount) {
        super(series);
        this.diBarCount = diBarCount;
        this.adxBarCount = adxBarCount;
        this.averageDXIndicator = new MMAIndicator(new DXIndicator(series, diBarCount), adxBarCount);
    }

    public ADXIndicator(BarSeries series, int barCount) {
        this(series, barCount, barCount);
    }

    @Override
    protected Num calculate(int index) {
        return averageDXIndicator.getValue(index);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " diBarCount: " + diBarCount + " adxBarCount: " + adxBarCount;
    }
}
