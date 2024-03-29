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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.analysis.cost.CostModel;
import org.ta4j.core.analysis.cost.LinearTransactionCostModel;
import org.ta4j.core.analysis.cost.ZeroCostModel;
import org.ta4j.core.num.Num;

/**
 * Base implementation of a {@link TradingRecord}.
 *
 */
public class BaseTradingRecord implements TradingRecord {

    private static final long serialVersionUID = -4436851731855891220L;

    /**
     * The name of the trading record
     */
    private String name;

    /**
     * The recorded trades
     */
    private List<Trade> trades = new ArrayList<>();

    /**
     * The recorded BUY trades
     */
    private List<Trade> buyTrades = new ArrayList<>();

    /**
     * The recorded SELL trades
     */
    private List<Trade> sellTrades = new ArrayList<>();

    /**
     * The recorded entry trades
     */
    private List<Trade> entryTrades = new ArrayList<>();

    /**
     * The recorded exit trades
     */
    private List<Trade> exitTrades = new ArrayList<>();

    /**
     * The entry type (BUY or SELL) in the trading session
     */
    private TradeType startingType;

    /**
     * The recorded positions
     */
    private List<Position> positions = new ArrayList<>();

    /**
     * The current non-closed position (there's always one)
     */
    private Position currentPosition;

    /**
     * Trading cost models
     */
    private CostModel transactionCostModel;
    private CostModel holdingCostModel;

    /**
     * Constructor.
     */
    public BaseTradingRecord() {
        this(TradeType.BUY);
    }

    /**
     * Constructor.
     *
     * @param name the name of the tradingRecord
     */
    public BaseTradingRecord(String name) {
        this(TradeType.BUY);
        this.name = name;
    }

    /**
     * Constructor.
     *
     * @param name      the name of the trading record
     * @param tradeType
     */
    public BaseTradingRecord(String name, TradeType tradeType) {
        this(tradeType, new ZeroCostModel(), new ZeroCostModel());
        this.name = name;
    }

    /**
     * Constructor.
     *
     * @param tradeType
     */
    public BaseTradingRecord(TradeType tradeType) {
        this(tradeType, new ZeroCostModel(), new ZeroCostModel());
    }

    /**
     * * Constructor.
     *
     * @param tradeType
     * @param transactionCostModel
     */
    public BaseTradingRecord(TradeType tradeType, CostModel transactionCostModel) {
        this(tradeType, transactionCostModel, new ZeroCostModel());
    }

    /**
     * * Constructor.
     *
     * @param transactionCostModel
     */
    public BaseTradingRecord(CostModel transactionCostModel) {
        this(TradeType.BUY, transactionCostModel);
    }

    /***
     * Constructor
     * 
     * @param linearTransactionCost
     */
    public BaseTradingRecord(Double linearTransactionCost) {
        this(new LinearTransactionCostModel(linearTransactionCost));
    }

    /**
     * Constructor.
     *
     * @param entryTradeType       the {@link TradeType trade type} of entries in
     *                             the trading session
     * @param transactionCostModel the cost model for transactions of the asset
     * @param holdingCostModel     the cost model for holding asset (e.g. borrowing)
     */
    public BaseTradingRecord(TradeType entryTradeType, CostModel transactionCostModel, CostModel holdingCostModel) {
        if (entryTradeType == null) {
            throw new IllegalArgumentException("Starting type must not be null");
        }
        this.startingType = entryTradeType;
        this.transactionCostModel = transactionCostModel;
        this.holdingCostModel = holdingCostModel;
        currentPosition = new Position(entryTradeType, transactionCostModel, holdingCostModel);
    }

    /**
     * Constructor.
     *
     * @param trades the trades to be recorded (cannot be empty)
     */
    public BaseTradingRecord(Trade... trades) {
        this(new ZeroCostModel(), new ZeroCostModel(), trades);
    }

    /**
     * Constructor.
     *
     * @param transactionCostModel the cost model for transactions of the asset
     * @param holdingCostModel     the cost model for holding asset (e.g. borrowing)
     * @param trades               the trades to be recorded (cannot be empty)
     */
    public BaseTradingRecord(CostModel transactionCostModel, CostModel holdingCostModel, Trade... trades) {
        this(trades[0].getType(), transactionCostModel, holdingCostModel);
        for (Trade o : trades) {
            boolean newTradeWillBeAnEntry = currentPosition.isNew();
            if (newTradeWillBeAnEntry && o.getType() != startingType) {
                // Special case for entry/exit types reversal
                // E.g.: BUY, SELL,
                // BUY, SELL,
                // SELL, BUY,
                // BUY, SELL
                currentPosition = new Position(o.getType(), transactionCostModel, holdingCostModel);
            }
            Trade newTrade = currentPosition.operate(o.getIndex(), o.getDateTime(), o.getPricePerAsset(),
                    o.getAmount());
            recordTrade(newTrade, newTradeWillBeAnEntry);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TradeType getStartingType() {
        return startingType;
    }

    @Override
    public Position getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void operate(int index, ZonedDateTime dateTime, Num price, Num amount) {
        if (currentPosition.isClosed()) {
            // Current position closed, should not occur
            throw new IllegalStateException("Current position should not be closed");
        }
        boolean newTradeWillBeAnEntry = currentPosition.isNew();
        Trade newTrade = currentPosition.operate(index, dateTime, price, amount);
        recordTrade(newTrade, newTradeWillBeAnEntry);
    }

    @Override
    public boolean enter(int index, ZonedDateTime dateTime, Num price, Num amount) {
        if (currentPosition.isNew()) {
            operate(index, dateTime, price, amount);
            return true;
        }
        return false;
    }

    @Override
    public boolean exit(int index, ZonedDateTime dateTime, Num price, Num amount) {
        if (currentPosition.isOpened()) {
            operate(index, dateTime, price, amount);
            return true;
        }
        return false;
    }

    @Override
    public List<Position> getPositions() {
        return positions;
    }

    @Override
    public Trade getLastTrade() {
        if (!trades.isEmpty()) {
            return trades.get(trades.size() - 1);
        }
        return null;
    }

    @Override
    public Trade getLastTrade(TradeType tradeType) {
        if (TradeType.BUY == tradeType && !buyTrades.isEmpty()) {
            return buyTrades.get(buyTrades.size() - 1);
        } else if (TradeType.SELL == tradeType && !sellTrades.isEmpty()) {
            return sellTrades.get(sellTrades.size() - 1);
        }
        return null;
    }

    @Override
    public Trade getLastEntry() {
        if (!entryTrades.isEmpty()) {
            return entryTrades.get(entryTrades.size() - 1);
        }
        return null;
    }

    @Override
    public Trade getLastExit() {
        if (!exitTrades.isEmpty()) {
            return exitTrades.get(exitTrades.size() - 1);
        }
        return null;
    }

    @Override
    public CostModel getTransactionCostModel() {
        return transactionCostModel;
    }

    @Override
    public CostModel getHoldingCostModel() {
        return holdingCostModel;
    }

    /**
     * Records a trade and the corresponding position (if closed).
     *
     * @param trade   the trade to be recorded
     * @param isEntry true if the trade is an entry, false otherwise (exit)
     */
    private void recordTrade(Trade trade, boolean isEntry) {
        if (trade == null) {
            throw new IllegalArgumentException("Trade should not be null");
        }

        // Storing the new trade in entries/exits lists
        if (isEntry) {
            entryTrades.add(trade);
        } else {
            exitTrades.add(trade);
        }

        // Storing the new trade in trades list
        trades.add(trade);
        if (TradeType.BUY == trade.getType()) {
            // Storing the new trade in buy trades list
            buyTrades.add(trade);
        } else if (TradeType.SELL == trade.getType()) {
            // Storing the new trade in sell trades list
            sellTrades.add(trade);
        }

        // Storing the position if closed
        if (currentPosition.isClosed()) {
            positions.add(currentPosition);
            currentPosition = new Position(startingType, transactionCostModel, holdingCostModel);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BaseTradingRecord: ").append(name == null ? "" : name);
        sb.append(System.lineSeparator());
        for (Trade trade : trades) {
            sb.append(trade.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
