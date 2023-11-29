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
package org.ta4j.core.indicators.keltner;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.numeric.NumericIndicator;

/**
 * A facade to create the 3 Keltner Channel indicators. An exponential moving
 * average of close price is used as the middle channel.
 *
 * <p>
 * This class creates lightweight "fluent" numeric indicators. These objects are
 * not cached, although they may be wrapped around cached objects. Overall there
 * is less caching and probably better performance.
 */
public class KeltnerChannelFacade {

    private final NumericIndicator middle;
    private final NumericIndicator upper;
    private final NumericIndicator lower;

    public KeltnerChannelFacade(BarSeries bs, int emaCount, int atrCount, Number k) {
        NumericIndicator price = NumericIndicator.of(new ClosePriceIndicator(bs));
        NumericIndicator atr = NumericIndicator.of(new ATRIndicator(bs, atrCount));
        this.middle = price.ema(emaCount);
        this.upper = middle.plus(atr.multipliedBy(k));
        this.lower = middle.minus(atr.multipliedBy(k));
    }

    public NumericIndicator middle() {
        return middle;
    }

    public NumericIndicator upper() {
        return upper;
    }

    public NumericIndicator lower() {
        return lower;
    }

}
