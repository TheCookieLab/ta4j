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
package org.ta4j.core;

import java.util.List;

import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.num.Num;

/**
 * An analysis criterion.
 *
 * Can be used to:
 * <ul>
 * <li>Analyze the performance of a {@link Strategy strategy}
 * <li>Compare several {@link Strategy strategies} together
 * </ul>
 */
public interface AnalysisCriterion {

    /** Filter to differentiate between winning or losing positions. */
    public enum PositionFilter {
        /** Consider only winning positions. */
        PROFIT,
        /** Consider only losing positions. */
        LOSS;
    }

    /**
     * @param series   a bar series, not null
     * @param position a position, not null
     * @return the criterion value for the position
     */
    Num calculate(BarSeries series, Position position);

    /**
     * @param series        a bar series, not null
     * @param tradingRecord a trading record, not null
     * @return the criterion value for the positions
     */
    Num calculate(BarSeries series, TradingRecord tradingRecord);

    /***
     * Should verify actual implementation as by default it's passthrough to the
     * original function and mostRecentPositions is unused
     * 
     * @param series              a bar series, not null
     * @param tradingRecord       a trading record, not null
     * @param mostRecentPositions only run analysis calculation on most recent N
     *                            positions
     * @return
     */
    Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions);

    /**
     * @param manager    the bar series manager with entry type of BUY
     * @param strategies a list of strategies
     * @return the best strategy (among the provided ones) according to the
     *         criterion
     */
    default Strategy chooseBest(BarSeriesManager manager, List<Strategy> strategies) {
        return chooseBest(manager, TradeType.BUY, strategies);
    }

    /**
     * @param manager    the bar series manager
     * @param tradeType  the entry type (BUY or SELL) of the first trade in the
     *                   trading session
     * @param strategies a list of strategies
     * @return the best strategy (among the provided ones) according to the
     *         criterion
     */
    default Strategy chooseBest(BarSeriesManager manager, TradeType tradeType, List<Strategy> strategies) {

        Strategy bestStrategy = strategies.get(0);
        Num bestCriterionValue = calculate(manager.getBarSeries(), manager.run(bestStrategy));

        for (int i = 1; i < strategies.size(); i++) {
            Strategy currentStrategy = strategies.get(i);
            Num currentCriterionValue = calculate(manager.getBarSeries(), manager.run(currentStrategy, tradeType));

            if (betterThan(currentCriterionValue, bestCriterionValue)) {
                bestStrategy = currentStrategy;
                bestCriterionValue = currentCriterionValue;
            }
        }

        return bestStrategy;
    }

    /**
     * @param criterionValue1 the first value
     * @param criterionValue2
     * 
     * 
     *                        the second value
     * @return true if the first value is better than (according to the criterion)
     *         the second one, false otherwise
     */
    boolean betterThan(Num criterionValue1, Num criterionValue2);
}