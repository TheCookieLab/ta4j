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
import org.ta4j.core.num.Num;

/**
 * Exponential moving average indicator.
 *
 * @see <a href=
 *      "https://www.investopedia.com/terms/e/ema.asp">https://www.investopedia.com/terms/e/ema.asp</a>
 */
public class EMAIndicator extends AbstractEMAIndicator {

    /**
     * Constructor.
     *
     * @param indicator an indicator
     * @param barCount  the EMA time frame
     */
    public EMAIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator, barCount, (2.0 / (barCount + 1)));
    }
}
