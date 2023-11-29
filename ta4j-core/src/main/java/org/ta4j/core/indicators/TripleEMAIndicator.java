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
 * Triple exponential moving average indicator.
 *
 * a.k.a TRIX
 *
 * TEMA needs "3 * period - 2" of data to start producing values in contrast to
 * the period samples needed by a regular EMA.
 *
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/Triple_exponential_moving_average">https://en.wikipedia.org/wiki/Triple_exponential_moving_average</a>
 * @see <a href=
 *      "https://www.investopedia.com/terms/t/triple-exponential-moving-average.asp">https://www.investopedia.com/terms/t/triple-exponential-moving-average.asp</a>
 */
public class TripleEMAIndicator extends CachedIndicator<Num> {

    private final Num three;
    private final int barCount;
    private final EMAIndicator ema;
    private final EMAIndicator emaEma;
    private final EMAIndicator emaEmaEma;

    /**
     * Constructor.
     *
     * @param indicator the indicator
     * @param barCount  the time frame
     */
    public TripleEMAIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator);
        this.three = indicator.numOf(3);
        this.barCount = barCount;
        this.ema = new EMAIndicator(indicator, barCount);
        this.emaEma = new EMAIndicator(ema, barCount);
        this.emaEmaEma = new EMAIndicator(emaEma, barCount);
    }

    @Override
    protected Num calculate(int index) {
        // tema = (3 * ema) - (3 * emaEma) + emaEmaEma
        Num first = three.multipliedBy(ema.getValue(index));
        Num second = three.multipliedBy(emaEma.getValue(index));
        Num third = emaEmaEma.getValue(index);

        return (first).minus(second).plus(third);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }
}
