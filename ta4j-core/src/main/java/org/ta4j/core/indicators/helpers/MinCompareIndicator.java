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
package org.ta4j.core.indicators.helpers;

import org.ta4j.core.Indicator;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 *
 * @author David
 */
public class MinCompareIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> first;
    private final Indicator<Num> second;

    public MinCompareIndicator(BarSeries series, Indicator<Num> first, Indicator<Num> second) {
        super(series);
        this.first = first;
        this.second = second;
    }

    @Override
    protected Num calculate(int index) {
        Num firstValue = first.getValue(index);
        Num secondValue = second.getValue(index);
        return firstValue.min(secondValue);
    }

}
