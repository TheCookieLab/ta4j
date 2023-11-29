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
package org.ta4j.core.rules;

import java.time.ZonedDateTime;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DoubleNum;

public class PercentageOverLastBuyPriceRuleTest {

    @Test
    public void isSatisfied() {
        BarSeries series = new BaseBarSeries(0d, 1d, 1.15d, 3d, 3.1d, 3.2d, 3.3d, 3.4d, 8d, 9d, 10d);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        TradingRecord tradingRecord = new BaseTradingRecord();
        tradingRecord.enter(0, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));

        PercentageOverLastBuyPriceRule rule = new PercentageOverLastBuyPriceRule(closePrice, DoubleNum.valueOf(0.1));

        assertFalse(rule.isSatisfied(0, tradingRecord));
        assertFalse(rule.isSatisfied(1, tradingRecord));
        assertTrue(rule.isSatisfied(2, tradingRecord));

        tradingRecord.exit(2, ZonedDateTime.now(), DoubleNum.valueOf(1), DoubleNum.valueOf(1));
        tradingRecord.enter(2, ZonedDateTime.now(), DoubleNum.valueOf(3), DoubleNum.valueOf(1));

        assertFalse(rule.isSatisfied(3, tradingRecord));
        assertFalse(rule.isSatisfied(4, tradingRecord));
        assertFalse(rule.isSatisfied(5, tradingRecord));
        assertFalse(rule.isSatisfied(6, tradingRecord));
        assertTrue(rule.isSatisfied(7, tradingRecord));

        tradingRecord.exit(7, ZonedDateTime.now(), DoubleNum.valueOf(3), DoubleNum.valueOf(1));
        tradingRecord.enter(7, ZonedDateTime.now(), DoubleNum.valueOf(3.4), DoubleNum.valueOf(1));

        assertTrue(rule.isSatisfied(8, tradingRecord));
        assertTrue(rule.isSatisfied(9, tradingRecord));
    }
}
