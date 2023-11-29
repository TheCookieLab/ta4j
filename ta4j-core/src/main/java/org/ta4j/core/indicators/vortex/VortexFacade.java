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
package org.ta4j.core.indicators.vortex;

import org.ta4j.core.BarSeries;
import org.ta4j.core.num.Num;

/**
 * Vortex indicator
 *
 * @see <a href=
 *      "https://www.investopedia.com/terms/a/adx.asp">https://www.investopedia.com/terms/a/adx.asp</a>
 */
public class VortexFacade {

    public final MinusVITrendLineIndicator minusVI;
    public final PlusVITrendLineIndicator plusVI;

    private final int barCount;

    public VortexFacade(BarSeries series, int barCount) {
        this.minusVI = new MinusVITrendLineIndicator(series, barCount);
        this.plusVI = new PlusVITrendLineIndicator(series, barCount);
        this.barCount = barCount;
    }

    public Num getMinusVIValue(int index) {
        return this.minusVI.getValue(index);
    }

    public Num getPlusVIValue(int index) {
        return this.plusVI.getValue(index);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + this.barCount;
    }
}
