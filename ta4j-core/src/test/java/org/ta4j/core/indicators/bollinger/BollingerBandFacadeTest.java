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

import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.OpenPriceIndicator;
import org.ta4j.core.indicators.numeric.NumericIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.ta4j.core.TestUtils.assertNumEquals;

public class BollingerBandFacadeTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    public BollingerBandFacadeTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Test
    public void testCreation() {
        final BarSeries data = new MockBarSeries(numFunction, 1, 2, 3, 4, 3, 4, 5, 4, 3, 3, 4, 3, 2);
        final ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(data);
        final int barCount = 3;

        final BollingerBandFacade bollingerBandFacade = new BollingerBandFacade(data, barCount, 2);

        assertEquals(data, bollingerBandFacade.bandwidth().getBarSeries());
        assertEquals(data, bollingerBandFacade.middle().getBarSeries());

        final BollingerBandFacade bollingerBandFacadeOfIndicator = new BollingerBandFacade(new OpenPriceIndicator(data),
                barCount, 2);

        assertEquals(data, bollingerBandFacadeOfIndicator.lower().getBarSeries());
        assertEquals(data, bollingerBandFacadeOfIndicator.upper().getBarSeries());
    }

    @Test
    public void testNumericFacadesSameAsDefaultIndicators() {
        final BarSeries data = new MockBarSeries(numFunction, 1, 2, 3, 4, 3, 4, 5, 4, 3, 3, 4, 3, 2);
        final ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(data);
        final int barCount = 3;
        final Indicator<Num> sma = new SMAIndicator(closePriceIndicator, 3);

        final BollingerBandsMiddleIndicator middleBB = new BollingerBandsMiddleIndicator(sma);
        final StandardDeviationIndicator standardDeviation = new StandardDeviationIndicator(closePriceIndicator,
                barCount);
        final BollingerBandsLowerIndicator lowerBB = new BollingerBandsLowerIndicator(middleBB, standardDeviation);
        final BollingerBandsUpperIndicator upperBB = new BollingerBandsUpperIndicator(middleBB, standardDeviation);
        final PercentBIndicator pcb = new PercentBIndicator(new ClosePriceIndicator(data), 5, 2);
        final BollingerBandWidthIndicator widthBB = new BollingerBandWidthIndicator(upperBB, middleBB, lowerBB);

        final BollingerBandFacade bollingerBandFacade = new BollingerBandFacade(data, barCount, 2);
        final NumericIndicator middleBBNumeric = bollingerBandFacade.middle();
        final NumericIndicator lowerBBNumeric = bollingerBandFacade.lower();
        final NumericIndicator upperBBNumeric = bollingerBandFacade.upper();
        final NumericIndicator widthBBNumeric = bollingerBandFacade.bandwidth();

        final NumericIndicator pcbNumeric = new BollingerBandFacade(data, 5, 2).percentB();

        for (int i = data.getBeginIndex(); i <= data.getEndIndex(); i++) {
            assertNumEquals(pcb.getValue(i), pcbNumeric.getValue(i));
            assertNumEquals(lowerBB.getValue(i), lowerBBNumeric.getValue(i));
            assertNumEquals(middleBB.getValue(i), middleBBNumeric.getValue(i));
            assertNumEquals(upperBB.getValue(i), upperBBNumeric.getValue(i));
            assertNumEquals(widthBB.getValue(i), widthBBNumeric.getValue(i));
        }
    }
}
