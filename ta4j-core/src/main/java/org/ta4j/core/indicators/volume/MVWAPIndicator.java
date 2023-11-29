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
package org.ta4j.core.indicators.volume;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.num.Num;

/**
 * The Moving volume weighted average price (MVWAP) Indicator.
 * 
 * @see <a href=
 *      "http://www.investopedia.com/articles/trading/11/trading-with-vwap-mvwap.asp">
 *      http://www.investopedia.com/articles/trading/11/trading-with-vwap-mvwap.asp</a>
 */
public class MVWAPIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> sma;

    /**
     * Constructor.
     * 
     * @param vwap     the vwap
     * @param barCount the time frame
     */
    public MVWAPIndicator(VWAPIndicator vwap, int barCount) {
        super(vwap);
        this.sma = new SMAIndicator(vwap, barCount);
    }

    @Override
    protected Num calculate(int index) {
        return sma.getValue(index);
    }

}
