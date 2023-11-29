/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
package org.ta4j.core.criteria;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * Total profit criterion.
 * </p>
 * The total profit of the provided {@link Trade trade(s)} over the provided
 * {@link BarSeries series}.
 */
public class TotalProfitCriterion extends AbstractAnalysisCriterion {

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        return tradingRecord.getPositions()
                .stream()
                .skip(Math.max(0, tradingRecord.getPositionCount() - mostRecentPositions))
                .map(position -> calculateProfit(series, position))
                .reduce(series.numOf(1), (profit1, profit2) -> profit1.multipliedBy(profit2));
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        return calculateProfit(series, position);
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

    /**
     * Calculates the profit of a trade (Buy and sell).
     *
     * @param series   a time series
     * @param position a trade
     * @return the profit of the trade
     */
    private Num calculateProfit(BarSeries series, Position position) {
        Num profit = series.numOf(1);
        if (position.isClosed()) {
            // use price of entry/exit order, if NaN use close price of underlying time
            // series
            Num exitClosePrice = position.getExit().getNetPrice().isNaN()
                    ? series.getBar(position.getExit().getIndex()).getClosePrice()
                    : position.getExit().getNetPrice();
            Num entryClosePrice = position.getEntry().getNetPrice().isNaN()
                    ? series.getBar(position.getEntry().getIndex()).getClosePrice()
                    : position.getEntry().getNetPrice();

            if (position.getEntry().isBuy()) {
                profit = exitClosePrice.dividedBy(entryClosePrice);
            } else {
                profit = entryClosePrice.dividedBy(exitClosePrice);
            }
        }
        return profit;
    }
}
