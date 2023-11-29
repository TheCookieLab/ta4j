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
 * Modified moving average indicator.
 *
 * It is similar to exponential moving average but smooths more slowly. Used in
 * Welles Wilder's indicators like ADX, RSI.
 */
public class MMAIndicator extends AbstractEMAIndicator {

    /**
     * Constructor.
     *
     * @param indicator an indicator
     * @param barCount  the MMA time frame
     */
    public MMAIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator, barCount, 1.0 / barCount);
    }
}
