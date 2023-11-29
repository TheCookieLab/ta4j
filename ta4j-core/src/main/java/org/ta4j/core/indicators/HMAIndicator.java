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
import org.ta4j.core.indicators.helpers.CombineIndicator;
import org.ta4j.core.indicators.helpers.TransformIndicator;
import org.ta4j.core.num.Num;

/**
 * Hull moving average (HMA) indicator.
 *
 * @see <a href="http://alanhull.com/hull-moving-average">
 *      http://alanhull.com/hull-moving-average</a>
 */
public class HMAIndicator extends CachedIndicator<Num> {

    private final int barCount;
    private final WMAIndicator sqrtWma;

    public HMAIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator);
        this.barCount = barCount;

        WMAIndicator halfWma = new WMAIndicator(indicator, barCount / 2);
        WMAIndicator origWma = new WMAIndicator(indicator, barCount);

        Indicator<Num> indicatorForSqrtWma = CombineIndicator.minus(TransformIndicator.multiply(halfWma, 2), origWma);
        sqrtWma = new WMAIndicator(indicatorForSqrtWma, numOf(barCount).sqrt().intValue());
    }

    @Override
    protected Num calculate(int index) {
        return sqrtWma.getValue(index);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }

}
