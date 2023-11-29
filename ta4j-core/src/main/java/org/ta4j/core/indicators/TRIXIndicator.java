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
 * * https://www.investopedia.com/terms/t/trix.asp
 * https://www.investopedia.com/articles/technical/02/092402.asp
 *
 */
public class TRIXIndicator extends CachedIndicator<Num> {

    private final Num hundred;
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
    public TRIXIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator);
        this.barCount = barCount;
        this.ema = new EMAIndicator(indicator, barCount);
        this.emaEma = new EMAIndicator(ema, barCount);
        this.emaEmaEma = new EMAIndicator(emaEma, barCount);
        this.hundred = indicator.numOf(100);
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return numOf(0);
        }
        Num emaEmaEmaCurrent = emaEmaEma.getValue(index);
        Num emaEmaEmaPrevious = emaEmaEma.getValue(index - 1);

        // Calculate the percentage rate of change of the triple EMA
        return emaEmaEmaCurrent.minus(emaEmaEmaPrevious).dividedBy(emaEmaEmaPrevious).multipliedBy(hundred);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }
}
