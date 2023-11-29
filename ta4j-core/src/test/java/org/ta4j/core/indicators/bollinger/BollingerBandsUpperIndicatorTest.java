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

public class BollingerBandsUpperIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private int barCount;

    private ClosePriceIndicator closePrice;

    private SMAIndicator sma;

    public BollingerBandsUpperIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        BarSeries data = new MockBarSeries(numFunction, 1, 2, 3, 4, 3, 4, 5, 4, 3, 3, 4, 3, 2);
        barCount = 3;
        closePrice = new ClosePriceIndicator(data);
        sma = new SMAIndicator(closePrice, barCount);
    }

    @Test
    public void bollingerBandsUpperUsingSMAAndStandardDeviation() {

        BollingerBandsMiddleIndicator bbmSMA = new BollingerBandsMiddleIndicator(sma);
        StandardDeviationIndicator standardDeviation = new StandardDeviationIndicator(closePrice, barCount);
        BollingerBandsUpperIndicator bbuSMA = new BollingerBandsUpperIndicator(bbmSMA, standardDeviation);

        assertNumEquals(2, bbuSMA.getK());

        assertNumEquals(1, bbuSMA.getValue(0));
        assertNumEquals(2.5, bbuSMA.getValue(1));
        assertNumEquals(3.633, bbuSMA.getValue(2));
        assertNumEquals(4.633, bbuSMA.getValue(3));
        assertNumEquals(4.2761, bbuSMA.getValue(4));
        assertNumEquals(4.6094, bbuSMA.getValue(5));
        assertNumEquals(5.633, bbuSMA.getValue(6));
        assertNumEquals(5.2761, bbuSMA.getValue(7));
        assertNumEquals(5.633, bbuSMA.getValue(8));
        assertNumEquals(4.2761, bbuSMA.getValue(9));

        BollingerBandsUpperIndicator bbuSMAwithK = new BollingerBandsUpperIndicator(bbmSMA, standardDeviation,
                numFunction.apply(1.5));

        assertNumEquals(1.5, bbuSMAwithK.getK());

        assertNumEquals(1, bbuSMAwithK.getValue(0));
        assertNumEquals(2.25, bbuSMAwithK.getValue(1));
        assertNumEquals(3.2247, bbuSMAwithK.getValue(2));
        assertNumEquals(4.2247, bbuSMAwithK.getValue(3));
        assertNumEquals(4.0404, bbuSMAwithK.getValue(4));
        assertNumEquals(4.3737, bbuSMAwithK.getValue(5));
        assertNumEquals(5.2247, bbuSMAwithK.getValue(6));
        assertNumEquals(5.0404, bbuSMAwithK.getValue(7));
        assertNumEquals(5.2247, bbuSMAwithK.getValue(8));
        assertNumEquals(4.0404, bbuSMAwithK.getValue(9));
    }
}
