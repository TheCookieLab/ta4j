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

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.helpers.TRIndicator;
import org.ta4j.core.num.Num;

/**
 * Average true range indicator.
 */
public class ATRIndicator extends AbstractIndicator<Num> {

    private final TRIndicator trIndicator;
    private final MMAIndicator averageTrueRangeIndicator;

    public ATRIndicator(BarSeries series, int barCount) {
        this(new TRIndicator(series), barCount);
    }

    public ATRIndicator(TRIndicator tr, int barCount) {
        super(tr.getBarSeries());
        this.trIndicator = tr;
        this.averageTrueRangeIndicator = new MMAIndicator(new TRIndicator(tr.getBarSeries()), barCount);
    }

    @Override
    public Num getValue(int index) {
        return averageTrueRangeIndicator.getValue(index);
    }

    public TRIndicator getTRIndicator() {
        return trIndicator;
    }

    public int getBarCount() {
        return averageTrueRangeIndicator.getBarCount();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + getBarCount();
    }
}
