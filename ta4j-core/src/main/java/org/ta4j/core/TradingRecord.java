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

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.analysis.cost.CostModel;
import org.ta4j.core.num.DoubleNum;
import static org.ta4j.core.num.NaN.NaN;
import org.ta4j.core.num.Num;

/**
 * A history/record of a trading session.
 *
 * Holds the full trading record when running a {@link Strategy strategy}. It is
 * used to:
 * <ul>
 * <li>check to satisfaction of some trading rules (when running a strategy)
 * <li>analyze the performance of a trading strategy
 * </ul>
 */
public interface TradingRecord extends Serializable, Comparable<TradingRecord> {

    /**
     * @return the entry type (BUY or SELL) of the first trade in the trading
     *         session
     */
    TradeType getStartingType();

    /**
     * @return the name of the TradingRecord
     */
    String getName();

    /**
     * Places a trade in the trading record.
     *
     * @param index the index to place the trade
     */
    default void operate(int index) {
        operate(index, ZonedDateTime.now(), NaN, NaN);
    }

    /**
     * Places a trade in the trading record.
     *
     * @param index    the index to place the trade
     * @param dateTime
     * @param price    the trade price
     * @param amount   the trade amount
     */
    void operate(int index, ZonedDateTime dateTime, Num price, Num amount);

    /**
     * Places an entry trade in the trading record.
     *
     * @param index the index to place the entry
     * @return true if the entry has been placed, false otherwise
     */
    default boolean enter(int index) {
        return enter(index, ZonedDateTime.now(), NaN, NaN);
    }

    /**
     * Places an entry trade in the trading record.
     *
     * @param index    the index to place the entry
     * @param dateTime
     * @param price    the trade price
     * @param amount   the trade amount
     * @return true if the entry has been placed, false otherwise
     */
    boolean enter(int index, ZonedDateTime dateTime, Num price, Num amount);

    /**
     * Places an exit trade in the trading record.
     *
     * @param index the index to place the exit
     * @return true if the exit has been placed, false otherwise
     */
    default boolean exit(int index) {
        return exit(index, ZonedDateTime.now(), NaN, NaN);
    }

    /**
     * Places an exit trade in the trading record.
     *
     * @param index    the index to place the exit
     * @param dateTime
     * @param price    the trade price
     * @param amount   the trade amount
     * @return true if the exit has been placed, false otherwise
     */
    boolean exit(int index, ZonedDateTime dateTime, Num price, Num amount);

    /**
     * @return true if no position is open, false otherwise
     */
    default boolean isClosed() {
        return !getCurrentPosition().isOpened();
    }

    /**
     * @return the recorded closed positions
     */
    List<Position> getPositions();

    /***
     * 
     * @param mostRecentPositions
     * @return the most recent recorded closed positions
     */
    default List<Position> getPositions(int mostRecentPositions) {
        return this.getPositions()
                .subList(Math.max(0, this.getPositionCount() - mostRecentPositions), this.getPositionCount());
    }

    /**
     * @return the number of recorded closed positions
     */
    default int getPositionCount() {
        return getPositions().size();
    }

    /**
     * @return the current (open) position
     */
    Position getCurrentPosition();

    /**
     * *
     *
     * @return the transaction cost model
     */
    CostModel getTransactionCostModel();

    /**
     * * the holding cost model
     *
     * @return
     */
    CostModel getHoldingCostModel();

    /**
     * @return the last closed position recorded
     */
    default Position getLastPosition() {
        List<Position> positions = getPositions();
        if (!positions.isEmpty()) {
            return positions.get(positions.size() - 1);
        }
        return null;
    }

    /**
     * @return the last trade recorded
     */
    Trade getLastTrade();

    /**
     * @param tradeType the type of the trade to get the last of
     * @return the last trade (of the provided type) recorded
     */
    Trade getLastTrade(TradeType tradeType);

    /**
     * @return the last entry trade recorded
     */
    Trade getLastEntry();

    /**
     * @return the last exit trade recorded
     */
    Trade getLastExit();

    /**
     * * Cumulative profit across all trades
     *
     * @return
     */
    default Num getNetProfit() {
        return this.getNetProfit(this.getPositions());
    }

    /**
     * * Cumulative profit across the most recent n trades
     *
     * @param ofMostRecentNPositions
     * @return
     */
    default Num getNetProfit(int ofMostRecentNPositions) {
        int startingIndex = this.getPositionCount() > ofMostRecentNPositions
                ? this.getPositionCount() - ofMostRecentNPositions
                : 0;
        List<Position> mostRecentPositions = this.getPositions().subList(startingIndex, this.getPositionCount());
        return this.getNetProfit(mostRecentPositions);
    }

    /**
     * * Cumulative profit across the given trades
     *
     * @param positions
     * @return
     */
    default Num getNetProfit(List<Position> positions) {
        Num profit = DoubleNum.valueOf(0);

        for (Position position : positions) {
            profit = profit.plus(position.getProfit());
        }

        return profit != NaN ? profit : DoubleNum.valueOf(0);
    }

    /**
     * *
     *
     * @param ofMostRecentNPositions
     * @return
     */
    default Num getPercentageProfitableTrades(int ofMostRecentNPositions) {
        int startingIndex = this.getPositionCount() > ofMostRecentNPositions
                ? this.getPositionCount() - ofMostRecentNPositions
                : 0;
        List<Position> mostRecentPositions = this.getPositions().subList(startingIndex, this.getPositionCount());
        return this.getPercentageProfitableTrades(mostRecentPositions);
    }

    /**
     * *
     *
     * @param positions
     * @return
     */
    default Num getPercentageProfitableTrades(List<Position> positions) {
        if (positions.isEmpty()) {
            return DoubleNum.valueOf(0);
        }

        int profitableTradeCount = 0;

        for (Position trade : positions) {
            if (trade.getProfit().isPositive()) {
                profitableTradeCount++;
            }
        }

        return DoubleNum.valueOf(profitableTradeCount).dividedBy(DoubleNum.valueOf(positions.size()));
    }

    /**
     * * The percentage of trades that have net profit > 0
     *
     * @return
     */
    default Num getPercentageProfitableTrades() {
        return this.getPercentageProfitableTrades(this.getPositions());
    }

    @Override
    default int compareTo(TradingRecord o) {
        return this.getNetProfit().compareTo(o.getNetProfit());
    }
}
