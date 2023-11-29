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
package org.ta4j.core.indicators.candles;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.num.Num;

/**
 * Three white soldiers indicator.
 *
 * @see <a href="http://www.investopedia.com/terms/t/three_white_soldiers.asp">
 *      http://www.investopedia.com/terms/t/three_white_soldiers.asp</a>
 */
public class ThreeWhiteSoldiersIndicator extends CachedIndicator<Boolean> {

    /**
     * Upper shadow
     */
    private final UpperShadowIndicator upperShadowInd;
    /**
     * Average upper shadow
     */
    private final SMAIndicator averageUpperShadowInd;
    /**
     * Factor used when checking if a candle has a very short upper shadow
     */
    private final Num factor;

    private int blackCandleIndex = -1;

    /**
     * Constructor.
     *
     * @param series   the bar series
     * @param barCount the number of bars used to calculate the average upper shadow
     * @param factor   the factor used when checking if a candle has a very short
     *                 upper shadow
     */
    public ThreeWhiteSoldiersIndicator(BarSeries series, int barCount, Num factor) {
        super(series);
        upperShadowInd = new UpperShadowIndicator(series);
        averageUpperShadowInd = new SMAIndicator(upperShadowInd, barCount);
        this.factor = factor;
    }

    @Override
    protected Boolean calculate(int index) {
        if (index < 3) {
            // We need 4 candles: 1 black, 3 white
            return false;
        }
        blackCandleIndex = index - 3;
        return getBarSeries().getBar(blackCandleIndex).isBearish() && isWhiteSoldier(index - 2)
                && isWhiteSoldier(index - 1) && isWhiteSoldier(index);
    }

    /**
     * @param index the bar/candle index
     * @return true if the bar/candle has a very short upper shadow, false otherwise
     */
    private boolean hasVeryShortUpperShadow(int index) {
        Num currentUpperShadow = upperShadowInd.getValue(index);
        // We use the black candle index to remove to bias of the previous soldiers
        Num averageUpperShadow = averageUpperShadowInd.getValue(blackCandleIndex);

        return currentUpperShadow.isLessThan(averageUpperShadow.multipliedBy(factor));
    }

    /**
     * @param index the current bar/candle index
     * @return true if the current bar/candle is growing, false otherwise
     */
    private boolean isGrowing(int index) {
        Bar prevBar = getBarSeries().getBar(index - 1);
        Bar currBar = getBarSeries().getBar(index);
        final Num prevOpenPrice = prevBar.getOpenPrice();
        final Num prevClosePrice = prevBar.getClosePrice();
        final Num currOpenPrice = currBar.getOpenPrice();
        final Num currClosePrice = currBar.getClosePrice();

        // Opens within the body of the previous candle
        return currOpenPrice.isGreaterThan(prevOpenPrice) && currOpenPrice.isLessThan(prevClosePrice)
        // Closes above the previous close price
                && currClosePrice.isGreaterThan(prevClosePrice);
    }

    /**
     * @param index the current bar/candle index
     * @return true if the current bar/candle is a white soldier, false otherwise
     */
    private boolean isWhiteSoldier(int index) {
        Bar prevBar = getBarSeries().getBar(index - 1);
        Bar currBar = getBarSeries().getBar(index);
        if (currBar.isBullish()) {
            if (prevBar.isBearish()) {
                // First soldier case
                return hasVeryShortUpperShadow(index) && currBar.getOpenPrice().isGreaterThan(prevBar.getLowPrice());
            } else {
                return hasVeryShortUpperShadow(index) && isGrowing(index);
            }
        }
        return false;
    }
}
