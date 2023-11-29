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

public class BollingerBandsLowerIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private int barCount;

    private ClosePriceIndicator closePrice;

    private SMAIndicator sma;

    public BollingerBandsLowerIndicatorTest(Function<Number, Num> numFunction) {
        super(null, numFunction);
    }

    @Before
    public void setUp() {
        BarSeries data = new MockBarSeries(numFunction, 1, 2, 3, 4, 3, 4, 5, 4, 3, 3, 4, 3, 2);
        barCount = 3;
        closePrice = new ClosePriceIndicator(data);
        sma = new SMAIndicator(closePrice, barCount);
    }

    @Test
    public void bollingerBandsLowerUsingSMAAndStandardDeviation() {

        BollingerBandsMiddleIndicator bbmSMA = new BollingerBandsMiddleIndicator(sma);
        StandardDeviationIndicator standardDeviation = new StandardDeviationIndicator(closePrice, barCount);
        BollingerBandsLowerIndicator bblSMA = new BollingerBandsLowerIndicator(bbmSMA, standardDeviation);

        assertNumEquals(2, bblSMA.getK());

        assertNumEquals(1, bblSMA.getValue(0));
        assertNumEquals(0.5, bblSMA.getValue(1));
        assertNumEquals(0.367, bblSMA.getValue(2));
        assertNumEquals(1.367, bblSMA.getValue(3));
        assertNumEquals(2.3905, bblSMA.getValue(4));
        assertNumEquals(2.7239, bblSMA.getValue(5));
        assertNumEquals(2.367, bblSMA.getValue(6));

        BollingerBandsLowerIndicator bblSMAwithK = new BollingerBandsLowerIndicator(bbmSMA, standardDeviation,
                numFunction.apply(1.5));

        assertNumEquals(1.5, bblSMAwithK.getK());

        assertNumEquals(1, bblSMAwithK.getValue(0));
        assertNumEquals(0.75, bblSMAwithK.getValue(1));
        assertNumEquals(0.7752, bblSMAwithK.getValue(2));
        assertNumEquals(1.7752, bblSMAwithK.getValue(3));
        assertNumEquals(2.6262, bblSMAwithK.getValue(4));
        assertNumEquals(2.9595, bblSMAwithK.getValue(5));
        assertNumEquals(2.7752, bblSMAwithK.getValue(6));
    }
}
