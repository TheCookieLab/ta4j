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
package org.ta4j.core.indicators.ichimoku;

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

public class IchimokuIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    protected BarSeries data;

    public IchimokuIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        final List<Bar> bars = new ArrayList<>();
        bars.add(new MockBar(44.98, 45.05, 45.17, 44.96, numFunction));
        bars.add(new MockBar(45.05, 45.10, 45.15, 44.99, numFunction));
        bars.add(new MockBar(45.11, 45.19, 45.32, 45.11, numFunction));
        bars.add(new MockBar(45.19, 45.14, 45.25, 45.04, numFunction));
        bars.add(new MockBar(45.12, 45.15, 45.20, 45.10, numFunction));
        bars.add(new MockBar(45.15, 45.14, 45.20, 45.10, numFunction));
        bars.add(new MockBar(45.13, 45.10, 45.16, 45.07, numFunction));
        bars.add(new MockBar(45.12, 45.15, 45.22, 45.10, numFunction));
        bars.add(new MockBar(45.15, 45.22, 45.27, 45.14, numFunction));
        bars.add(new MockBar(45.24, 45.43, 45.45, 45.20, numFunction));
        bars.add(new MockBar(45.43, 45.44, 45.50, 45.39, numFunction));
        bars.add(new MockBar(45.43, 45.55, 45.60, 45.35, numFunction));
        bars.add(new MockBar(45.58, 45.55, 45.61, 45.39, numFunction));
        bars.add(new MockBar(45.45, 45.01, 45.55, 44.80, numFunction));
        bars.add(new MockBar(45.03, 44.23, 45.04, 44.17, numFunction));
        bars.add(new MockBar(44.23, 43.95, 44.29, 43.81, numFunction));
        bars.add(new MockBar(43.91, 43.08, 43.99, 43.08, numFunction));
        bars.add(new MockBar(43.07, 43.55, 43.65, 43.06, numFunction));
        bars.add(new MockBar(43.56, 43.95, 43.99, 43.53, numFunction));
        bars.add(new MockBar(43.93, 44.47, 44.58, 43.93, numFunction));
        data = new MockBarSeries(bars);
    }

    @Test
    public void ichimoku() {
        IchimokuTenkanSenIndicator tenkanSen = new IchimokuTenkanSenIndicator(data, 3);
        IchimokuKijunSenIndicator kijunSen = new IchimokuKijunSenIndicator(data, 5);
        IchimokuSenkouSpanAIndicator senkouSpanA = new IchimokuSenkouSpanAIndicator(data, tenkanSen, kijunSen, 5);
        IchimokuSenkouSpanBIndicator senkouSpanB = new IchimokuSenkouSpanBIndicator(data, 9, 5);
        final int chikouSpanTimeDelay = 5;
        IchimokuChikouSpanIndicator chikouSpan = new IchimokuChikouSpanIndicator(data, chikouSpanTimeDelay);

        assertNumEquals(45.155, tenkanSen.getValue(3));
        assertNumEquals(45.18, tenkanSen.getValue(4));
        assertNumEquals(45.145, tenkanSen.getValue(5));
        assertNumEquals(45.135, tenkanSen.getValue(6));
        assertNumEquals(45.145, tenkanSen.getValue(7));
        assertNumEquals(45.17, tenkanSen.getValue(8));
        assertNumEquals(44.06, tenkanSen.getValue(16));
        assertNumEquals(43.675, tenkanSen.getValue(17));
        assertNumEquals(43.525, tenkanSen.getValue(18));

        assertNumEquals(45.14, kijunSen.getValue(3));
        assertNumEquals(45.14, kijunSen.getValue(4));
        assertNumEquals(45.155, kijunSen.getValue(5));
        assertNumEquals(45.18, kijunSen.getValue(6));
        assertNumEquals(45.145, kijunSen.getValue(7));
        assertNumEquals(45.17, kijunSen.getValue(8));
        assertNumEquals(44.345, kijunSen.getValue(16));
        assertNumEquals(44.305, kijunSen.getValue(17));
        assertNumEquals(44.05, kijunSen.getValue(18));

        assertNumEquals(NaN.NaN, senkouSpanA.getValue(3));
        assertNumEquals(45.065, senkouSpanA.getValue(4));
        assertNumEquals(45.1475, senkouSpanA.getValue(7));
        assertNumEquals(45.16, senkouSpanA.getValue(8));
        assertNumEquals(45.15, senkouSpanA.getValue(9));
        assertNumEquals(45.1575, senkouSpanA.getValue(10));
        assertNumEquals(45.4275, senkouSpanA.getValue(16));
        assertNumEquals(45.205, senkouSpanA.getValue(17));
        assertNumEquals(44.89, senkouSpanA.getValue(18));

        assertNumEquals(NaN.NaN, senkouSpanB.getValue(3));
        assertNumEquals(45.065, senkouSpanB.getValue(4));
        assertNumEquals(45.065, senkouSpanB.getValue(5));
        assertNumEquals(45.14, senkouSpanB.getValue(6));
        assertNumEquals(45.14, senkouSpanB.getValue(7));
        assertNumEquals(45.22, senkouSpanB.getValue(13));
        assertNumEquals(45.34, senkouSpanB.getValue(16));
        assertNumEquals(45.205, senkouSpanB.getValue(17));
        assertNumEquals(44.89, senkouSpanB.getValue(18));

        assertNumEquals(data.getBar(chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(0));
        assertNumEquals(data.getBar(1 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(1));
        assertNumEquals(data.getBar(2 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(2));
        assertNumEquals(data.getBar(3 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(3));
        assertNumEquals(data.getBar(4 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(4));
        assertNumEquals(data.getBar(5 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(5));
        assertNumEquals(data.getBar(6 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(6));
        assertNumEquals(data.getBar(7 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(7));
        assertNumEquals(data.getBar(8 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(8));
        assertNumEquals(data.getBar(9 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(9));
        assertNumEquals(data.getBar(10 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(10));
        assertNumEquals(data.getBar(11 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(11));
        assertNumEquals(data.getBar(12 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(12));
        assertNumEquals(data.getBar(13 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(13));
        assertNumEquals(data.getBar(14 + chikouSpanTimeDelay).getClosePrice(), chikouSpan.getValue(14));
        assertNumEquals(NaN.NaN, chikouSpan.getValue(15));
        assertNumEquals(NaN.NaN, chikouSpan.getValue(16));
        assertNumEquals(NaN.NaN, chikouSpan.getValue(17));
        assertNumEquals(NaN.NaN, chikouSpan.getValue(18));
        assertNumEquals(NaN.NaN, chikouSpan.getValue(19));
    }
}
