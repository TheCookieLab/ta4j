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

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.AbstractIndicator;
import org.ta4j.core.num.Num;

/**
 * Low price indicator.
 */
public class LowPriceIndicator extends AbstractIndicator<Num> {

    public LowPriceIndicator(BarSeries series) {
        super(series);
    }

    @Override
    public Num getValue(int index) {
        return getBarSeries().getBar(index).getLowPrice();
    }
}
