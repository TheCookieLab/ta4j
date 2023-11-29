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
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.helpers.CumulativeSumIndicator;
import org.ta4j.core.indicators.helpers.TRIndicator;
import org.ta4j.core.num.Num;

/**
 * Vortex VI- (Minus) Trend Line Indicator.
 * <p>
 * The Vortex Indicator (VI) is composed of two lines - VI+ (Plus) and VI-
 * (Minus). The VI- trend line is calculated using the negative directional
 * movement over a specified period.
 * <p>
 * For more information:
 * https://www.investopedia.com/terms/v/vortex-indicator-vi.asp
 */
public class MinusVITrendLineIndicator extends CachedIndicator<Num> {

    private final CumulativeSumIndicator sumMinusVM;
    private final CumulativeSumIndicator sumTR;
    private final int barCount;

    /**
     * Constructor.
     *
     * @param series   the BarSeries
     * @param barCount the number of bars to consider for the calculation
     */
    public MinusVITrendLineIndicator(BarSeries series, int barCount) {
        super(series);

        this.sumMinusVM = new CumulativeSumIndicator(new MinusVMIndicator(series), barCount);
        this.sumTR = new CumulativeSumIndicator(new TRIndicator(series), barCount);
        this.barCount = barCount;
    }

    @Override
    protected Num calculate(int index) {
        return this.sumMinusVM.getValue(index).dividedBy(this.sumTR.getValue(index));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + this.barCount;
    }
}
