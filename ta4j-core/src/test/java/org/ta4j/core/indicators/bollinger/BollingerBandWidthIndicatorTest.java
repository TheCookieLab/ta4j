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

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class BollingerBandWidthIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private ClosePriceIndicator closePrice;

    public BollingerBandWidthIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        BarSeries data = new MockBarSeries(numFunction, 10, 12, 15, 14, 17, 20, 21, 20, 20, 19, 20, 17, 12, 12, 9, 8, 9,
                10, 9, 10);
        closePrice = new ClosePriceIndicator(data);
    }

    @Test
    public void bollingerBandWidthUsingSMAAndStandardDeviation() {

        SMAIndicator sma = new SMAIndicator(closePrice, 5);
        StandardDeviationIndicator standardDeviation = new StandardDeviationIndicator(closePrice, 5);

        BollingerBandsMiddleIndicator bbmSMA = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsUpperIndicator bbuSMA = new BollingerBandsUpperIndicator(bbmSMA, standardDeviation);
        BollingerBandsLowerIndicator bblSMA = new BollingerBandsLowerIndicator(bbmSMA, standardDeviation);

        BollingerBandWidthIndicator bandwidth = new BollingerBandWidthIndicator(bbuSMA, bbmSMA, bblSMA);

        assertNumEquals(0.0, bandwidth.getValue(0));
        assertNumEquals(36.3636, bandwidth.getValue(1));
        assertNumEquals(66.6423, bandwidth.getValue(2));
        assertNumEquals(60.2443, bandwidth.getValue(3));
        assertNumEquals(71.0767, bandwidth.getValue(4));
        assertNumEquals(69.9394, bandwidth.getValue(5));
        assertNumEquals(62.7043, bandwidth.getValue(6));
        assertNumEquals(56.0178, bandwidth.getValue(7));
        assertNumEquals(27.683, bandwidth.getValue(8));
        assertNumEquals(12.6491, bandwidth.getValue(9));
        assertNumEquals(12.6491, bandwidth.getValue(10));
        assertNumEquals(24.2956, bandwidth.getValue(11));
        assertNumEquals(68.3332, bandwidth.getValue(12));
        assertNumEquals(85.1469, bandwidth.getValue(13));
        assertNumEquals(112.8481, bandwidth.getValue(14));
        assertNumEquals(108.1682, bandwidth.getValue(15));
        assertNumEquals(66.9328, bandwidth.getValue(16));
        assertNumEquals(56.5194, bandwidth.getValue(17));
        assertNumEquals(28.1091, bandwidth.getValue(18));
        assertNumEquals(32.5362, bandwidth.getValue(19));
    }
}
