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
 * Linearly Weighted Moving Average (LWMA).
 *
 * @see <a href=
 *      "https://www.investopedia.com/terms/l/linearlyweightedmovingaverage.asp">
 *      https://www.investopedia.com/terms/l/linearlyweightedmovingaverage.asp</a>
 */
public class LWMAIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> indicator;
    private final int barCount;
    private final Num zero = numOf(0);

    public LWMAIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator);
        this.indicator = indicator;
        this.barCount = barCount;
    }

    @Override
    protected Num calculate(int index) {
        Num sum = zero;
        Num denominator = zero;
        int count = 0;

        if ((index + 1) < barCount) {
            return zero;
        }

        int startIndex = (index - barCount) + 1;
        for (int i = startIndex; i <= index; i++) {
            count++;
            denominator = denominator.plus(numOf(count));
            sum = sum.plus(indicator.getValue(i).multipliedBy(numOf(count)));
        }
        return sum.dividedBy(denominator);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }
}
