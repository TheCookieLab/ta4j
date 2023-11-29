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
package org.ta4j.core.indicators.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.ZonedDateTime;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.EMAIndicator;

public class PreviousValueIndicatorTest {

    private PreviousValueIndicator prevValueIndicator;

    private OpenPriceIndicator openPriceIndicator;
    private LowPriceIndicator lowPriceIndicator;
    private HighPriceIndicator highPriceIndicator;

    private VolumeIndicator volumeIndicator;
    private EMAIndicator emaIndicator;

    private BarSeries series;

    @Before
    public void setUp() {
        Random r = new Random();
        this.series = new BaseBarSeries("test");
        for (int i = 0; i < 1000; i++) {
            double open = r.nextDouble();
            double close = r.nextDouble();
            double max = Math.max(close + r.nextDouble(), open + r.nextDouble());
            double min = Math.min(0, Math.min(close - r.nextDouble(), open - r.nextDouble()));
            ZonedDateTime dateTime = ZonedDateTime.now().minusSeconds(1001 - i);
            series.addBar(dateTime, open, close, max, min, i);
        }

        this.openPriceIndicator = new OpenPriceIndicator(this.series);
        this.lowPriceIndicator = new LowPriceIndicator(this.series);
        this.highPriceIndicator = new HighPriceIndicator(this.series);
        this.volumeIndicator = new VolumeIndicator(this.series);
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(this.series);
        this.emaIndicator = new EMAIndicator(closePriceIndicator, 20);
    }

    @Test
    public void shouldBePreviousValueFromIndicator() {

        // test 1 with openPrice-indicator
        prevValueIndicator = new PreviousValueIndicator(openPriceIndicator);
        assertEquals(prevValueIndicator.getValue(0), openPriceIndicator.getValue(0));
        for (int i = 1; i < this.series.getBarCount(); i++) {
            assertEquals(prevValueIndicator.getValue(i), openPriceIndicator.getValue(i - 1));
        }

        // test 2 with lowPrice-indicator
        prevValueIndicator = new PreviousValueIndicator(lowPriceIndicator);
        assertEquals(prevValueIndicator.getValue(0), lowPriceIndicator.getValue(0));
        for (int i = 1; i < this.series.getBarCount(); i++) {
            assertEquals(prevValueIndicator.getValue(i), lowPriceIndicator.getValue(i - 1));
        }

        // test 3 with highPrice-indicator
        prevValueIndicator = new PreviousValueIndicator(highPriceIndicator);
        assertEquals(prevValueIndicator.getValue(0), highPriceIndicator.getValue(0));
        for (int i = 1; i < this.series.getBarCount(); i++) {
            assertEquals(prevValueIndicator.getValue(i), highPriceIndicator.getValue(i - 1));
        }
    }

    @Test
    public void shouldBeNthPreviousValueFromIndicator() {
        for (int i = 1; i < this.series.getBarCount(); i++) {
            testWithN(i);
        }
    }

    private void testWithN(int n) {

        // test 1 with volume-indicator
        prevValueIndicator = new PreviousValueIndicator(volumeIndicator, n);
        for (int i = 0; i < n; i++) {
            assertEquals(prevValueIndicator.getValue(i), volumeIndicator.getValue(0));
        }
        for (int i = n; i < this.series.getBarCount(); i++) {
            assertEquals(prevValueIndicator.getValue(i), volumeIndicator.getValue(i - n));
        }

        // test 2 with ema-indicator
        prevValueIndicator = new PreviousValueIndicator(emaIndicator, n);
        for (int i = 0; i < n; i++) {
            assertEquals(prevValueIndicator.getValue(i), emaIndicator.getValue(0));
        }
        for (int i = n; i < this.series.getBarCount(); i++) {
            assertEquals(prevValueIndicator.getValue(i), emaIndicator.getValue(i - n));
        }
    }

    @Test
    public void testToStringMethodWithN1() {
        prevValueIndicator = new PreviousValueIndicator(openPriceIndicator);

        final String prevValueIndicatorAsString = prevValueIndicator.toString();

        assertTrue(prevValueIndicatorAsString.startsWith("PreviousValueIndicator["));
        assertTrue(prevValueIndicatorAsString.endsWith("]"));
    }

    @Test
    public void testToStringMethodWithNGreaterThen1() {
        prevValueIndicator = new PreviousValueIndicator(openPriceIndicator, 2);

        final String prevValueIndicatorAsString = prevValueIndicator.toString();

        assertTrue(prevValueIndicatorAsString.startsWith("PreviousValueIndicator(2)["));
        assertTrue(prevValueIndicatorAsString.endsWith("]"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPreviousValueIndicatorWithNonPositiveN() {
        prevValueIndicator = new PreviousValueIndicator(openPriceIndicator, 0);
    }
}