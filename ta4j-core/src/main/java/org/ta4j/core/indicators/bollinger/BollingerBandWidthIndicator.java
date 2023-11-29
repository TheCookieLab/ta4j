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
package org.ta4j.core.indicators.bollinger;

import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Bollinger BandWidth indicator.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:bollinger_band_width">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:bollinger_band_width</a>
 */
public class BollingerBandWidthIndicator extends CachedIndicator<Num> {

    private final BollingerBandsUpperIndicator bbu;
    private final BollingerBandsMiddleIndicator bbm;
    private final BollingerBandsLowerIndicator bbl;
    private final Num hundred;

    /**
     * Constructor.
     *
     * @param bbu the upper band Indicator.
     * @param bbm the middle band Indicator. Typically an SMAIndicator is used.
     * @param bbl the lower band Indicator.
     */
    public BollingerBandWidthIndicator(BollingerBandsUpperIndicator bbu, BollingerBandsMiddleIndicator bbm,
            BollingerBandsLowerIndicator bbl) {
        super(bbm.getBarSeries());
        this.bbu = bbu;
        this.bbm = bbm;
        this.bbl = bbl;
        this.hundred = bbm.getBarSeries().numOf(100);
    }

    @Override
    protected Num calculate(int index) {
        return bbu.getValue(index).minus(bbl.getValue(index)).dividedBy(bbm.getValue(index)).multipliedBy(hundred);
    }
}
