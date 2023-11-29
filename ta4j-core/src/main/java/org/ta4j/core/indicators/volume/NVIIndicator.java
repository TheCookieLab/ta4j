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

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.RecursiveCachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Negative Volume Index (NVI) indicator.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:negative_volume_inde">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:negative_volume_inde</a>
 * @see <a href=
 *      "http://www.metastock.com/Customer/Resources/TAAZ/Default.aspx?p=75">
 *      http://www.metastock.com/Customer/Resources/TAAZ/Default.aspx?p=75</a>
 * @see <a href="http://www.investopedia.com/terms/n/nvi.asp">
 *      http://www.investopedia.com/terms/n/nvi.asp</a>
 */
public class NVIIndicator extends RecursiveCachedIndicator<Num> {

    public NVIIndicator(BarSeries series) {
        super(series);
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return numOf(1000);
        }

        Bar currentBar = getBarSeries().getBar(index);
        Bar previousBar = getBarSeries().getBar(index - 1);
        Num previousValue = getValue(index - 1);

        if (currentBar.getVolume().isLessThan(previousBar.getVolume())) {
            Num currentPrice = currentBar.getClosePrice();
            Num previousPrice = previousBar.getClosePrice();
            Num priceChangeRatio = currentPrice.minus(previousPrice).dividedBy(previousPrice);
            return previousValue.plus(priceChangeRatio.multipliedBy(previousValue));
        }
        return previousValue;
    }

}