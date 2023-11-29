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
package org.ta4j.core.indicators.keltner;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.Indicator;
import org.ta4j.core.IndicatorFactory;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.numeric.NumericIndicator;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.ta4j.core.TestUtils.assertNumEquals;

public class KeltnerChannelFacadeTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private MockBarSeries data;

    public KeltnerChannelFacadeTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {

        List<Bar> bars = new ArrayList<>();
        bars.add(new MockBar(11577.43, 11670.75, 11711.47, 11577.35, numFunction));
        bars.add(new MockBar(11670.90, 11691.18, 11698.22, 11635.74, numFunction));
        bars.add(new MockBar(11688.61, 11722.89, 11742.68, 11652.89, numFunction));
        bars.add(new MockBar(11716.93, 11697.31, 11736.74, 11667.46, numFunction));
        bars.add(new MockBar(11696.86, 11674.76, 11726.94, 11599.68, numFunction));
        bars.add(new MockBar(11672.34, 11637.45, 11677.33, 11573.87, numFunction));
        bars.add(new MockBar(11638.51, 11671.88, 11704.12, 11635.48, numFunction));
        bars.add(new MockBar(11673.62, 11755.44, 11782.23, 11673.62, numFunction));
        bars.add(new MockBar(11753.70, 11731.90, 11757.25, 11700.53, numFunction));
        bars.add(new MockBar(11732.13, 11787.38, 11794.15, 11698.83, numFunction));
        bars.add(new MockBar(11783.82, 11837.93, 11858.78, 11777.99, numFunction));
        bars.add(new MockBar(11834.21, 11825.29, 11861.24, 11798.46, numFunction));
        bars.add(new MockBar(11823.70, 11822.80, 11845.16, 11744.77, numFunction));
        bars.add(new MockBar(11822.95, 11871.84, 11905.48, 11822.80, numFunction));
        bars.add(new MockBar(11873.43, 11980.52, 11982.94, 11867.98, numFunction));
        bars.add(new MockBar(11980.52, 11977.19, 11985.97, 11898.74, numFunction));
        bars.add(new MockBar(11978.85, 11985.44, 12020.52, 11961.83, numFunction));
        bars.add(new MockBar(11985.36, 11989.83, 12019.53, 11971.93, numFunction));
        bars.add(new MockBar(11824.39, 11891.93, 11891.93, 11817.88, numFunction));
        bars.add(new MockBar(11892.50, 12040.16, 12050.75, 11892.50, numFunction));
        bars.add(new MockBar(12038.27, 12041.97, 12057.91, 12018.51, numFunction));
        bars.add(new MockBar(12040.68, 12062.26, 12080.54, 11981.05, numFunction));
        bars.add(new MockBar(12061.73, 12092.15, 12092.42, 12025.78, numFunction));
        bars.add(new MockBar(12092.38, 12161.63, 12188.76, 12092.30, numFunction));
        bars.add(new MockBar(12152.70, 12233.15, 12238.79, 12150.05, numFunction));
        bars.add(new MockBar(12229.29, 12239.89, 12254.23, 12188.19, numFunction));
        bars.add(new MockBar(12239.66, 12229.29, 12239.66, 12156.94, numFunction));
        bars.add(new MockBar(12227.78, 12273.26, 12285.94, 12180.48, numFunction));
        bars.add(new MockBar(12266.83, 12268.19, 12276.21, 12235.91, numFunction));
        bars.add(new MockBar(12266.75, 12226.64, 12267.66, 12193.27, numFunction));
        bars.add(new MockBar(12219.79, 12288.17, 12303.16, 12219.79, numFunction));
        bars.add(new MockBar(12287.72, 12318.14, 12331.31, 12253.24, numFunction));
        bars.add(new MockBar(12389.74, 12212.79, 12389.82, 12176.31, numFunction));

        data = new MockBarSeries(bars);
    }

    @Test
    public void testCreation() {
        final KeltnerChannelFacade facade = new KeltnerChannelFacade(data, 14, 14, 2);
        assertEquals(data, facade.middle().getBarSeries());
    }

    @Test
    public void testNumericFacadesSameAsDefaultIndicators() {
        final KeltnerChannelMiddleIndicator km = new KeltnerChannelMiddleIndicator(new ClosePriceIndicator(data), 14);
        final KeltnerChannelLowerIndicator kl = new KeltnerChannelLowerIndicator(km, 2, 14);
        final KeltnerChannelUpperIndicator ku = new KeltnerChannelUpperIndicator(km, 2, 14);

        final KeltnerChannelFacade facade = new KeltnerChannelFacade(data, 14, 14, 2);
        final NumericIndicator middleNumeric = facade.middle();
        final NumericIndicator upperNumeric = facade.upper();
        final NumericIndicator lowerNumeric = facade.lower();

        for (int i = data.getBeginIndex(); i <= data.getEndIndex(); i++) {
            assertNumEquals(kl.getValue(i), lowerNumeric.getValue(i));
            assertNumEquals(ku.getValue(i), upperNumeric.getValue(i));
            assertNumEquals(km.getValue(i), middleNumeric.getValue(i));
        }
    }
}
