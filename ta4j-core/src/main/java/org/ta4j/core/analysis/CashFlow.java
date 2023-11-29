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
package org.ta4j.core.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * The cash flow.
 *
 * This class allows to follow the money cash flow involved by a list of
 * positions over a bar series.
 */
public class CashFlow implements Indicator<Num> {

    /**
     * The bar series
     */
    private final BarSeries barSeries;

    /**
     * The cash flow values
     */
    private List<Num> values;

    /**
     * Constructor for cash flows of a closed position.
     *
     * @param barSeries the bar series
     * @param position  a single position
     */
    public CashFlow(BarSeries barSeries, Position position) {
        this.barSeries = barSeries;
        values = new ArrayList<>(Collections.singletonList(numOf(1)));
        calculate(position);
        fillToTheEnd();
    }

    /**
     * *
     *
     * @param barSeries
     * @param tradingRecord
     */
    public CashFlow(BarSeries barSeries, TradingRecord tradingRecord) {
        this(barSeries, tradingRecord, tradingRecord.getPositionCount());
    }

    /**
     * Constructor for cash flows of closed positions of a trading record.
     *
     * @param barSeries           the bar series
     * @param tradingRecord       the trading record
     * @param mostRecentPositions
     */
    public CashFlow(BarSeries barSeries, TradingRecord tradingRecord, int mostRecentPositions) {
        this.barSeries = barSeries;
        values = new ArrayList<>(Collections.singletonList(numOf(1)));
        calculate(tradingRecord, mostRecentPositions);

        fillToTheEnd();
    }

    /**
     * Constructor.
     *
     * @param barSeries           the bar series
     * @param tradingRecord       the trading record
     * @param mostRecentPositions
     * @param finalIndex          index up until cash flows of open positions are
     *                            considered
     */
    public CashFlow(BarSeries barSeries, TradingRecord tradingRecord, int mostRecentPositions, int finalIndex) {
        this.barSeries = barSeries;
        values = new ArrayList<>(Collections.singletonList(numOf(1)));
        calculate(tradingRecord, mostRecentPositions, finalIndex);

        fillToTheEnd();
    }

    /**
     * @param index the bar index
     * @return the cash flow value at the index-th position
     */
    @Override
    public Num getValue(int index) {
        return values.get(index);
    }

    @Override
    public BarSeries getBarSeries() {
        return barSeries;
    }

    @Override
    public Num numOf(Number number) {
        return barSeries.numOf(number);
    }

    /**
     * @return the size of the bar series
     */
    public int getSize() {
        return barSeries.getBarCount();
    }

    /**
     * Calculates the cash flow for a single closed position.
     *
     * @param position a single position
     */
    private void calculate(Position position) {
        if (position.isOpened()) {
            throw new IllegalArgumentException(
                    "Position is not closed. Final index of observation needs to be provided.");
        }
        calculate(position, position.getExit().getIndex());
    }

    /**
     * Calculates the cash flow for a single position (including accrued cashflow
     * for open positions).
     *
     * @param position   a single position
     * @param finalIndex index up until cash flow of open positions is considered
     */
    private void calculate(Position position, int finalIndex) {
        boolean isLongTrade = position.getEntry().isBuy();
        int endIndex = determineEndIndex(position, finalIndex, barSeries.getEndIndex());
        final int entryIndex = position.getEntry().getIndex();
        int begin = entryIndex + 1;
        if (begin > values.size()) {
            Num lastValue = values.get(values.size() - 1);
            values.addAll(Collections.nCopies(begin - values.size(), lastValue));
        }
        // Trade is not valid if net balance at the entryIndex is negative
        if (values.get(values.size() - 1).isGreaterThan(values.get(0).numOf(0))) {
            int startingIndex = Math.max(begin, 1);

            int nPeriods = endIndex - entryIndex;
            Num holdingCost = position.getHoldingCost(endIndex);
            Num avgCost = holdingCost.dividedBy(holdingCost.numOf(nPeriods));

            // Add intermediate cash flows during position
            Num netEntryPrice = position.getEntry().getNetPrice();
            for (int i = startingIndex; i < endIndex; i++) {
                Num intermediateNetPrice = addCost(barSeries.getBar(i).getClosePrice(), avgCost, isLongTrade);
                Num ratio = getIntermediateRatio(isLongTrade, netEntryPrice, intermediateNetPrice);
                values.add(values.get(entryIndex).multipliedBy(ratio));
            }

            // add net cash flow at exit position
            Num exitPrice;
            if (position.getExit() != null) {
                exitPrice = position.getExit().getNetPrice();
            } else {
                exitPrice = barSeries.getBar(endIndex).getClosePrice();
            }
            Num ratio = getIntermediateRatio(isLongTrade, netEntryPrice, addCost(exitPrice, avgCost, isLongTrade));
            values.add(values.get(entryIndex).multipliedBy(ratio));
        }
    }

    /**
     * Calculates the ratio of intermediate prices.
     *
     * @param isLongTrade true, if the entry trade type is BUY
     * @param entryPrice  price ratio denominator
     * @param exitPrice   price ratio numerator
     */
    private static Num getIntermediateRatio(boolean isLongTrade, Num entryPrice, Num exitPrice) {
        Num ratio;
        if (isLongTrade) {
            ratio = exitPrice.dividedBy(entryPrice);
        } else {
            ratio = entryPrice.numOf(2).minus(exitPrice.dividedBy(entryPrice));
        }
        return ratio;
    }

    /**
     * Calculates the cash flow for the closed positions of a trading record.
     *
     * @param tradingRecord the trading record
     */
    private void calculate(TradingRecord tradingRecord, int mostRecentPositions) {
        // For each position...
        tradingRecord.getPositions(mostRecentPositions).forEach(this::calculate);
    }

    /**
     * Calculates the cash flow for all positions of a trading record, including
     * accrued cash flow of an open position.
     *
     * @param tradingRecord the trading record
     * @param finalIndex    index up until cash flows of open positions are
     *                      considered
     */
    private void calculate(TradingRecord tradingRecord, int mostRecentPositions, int finalIndex) {
        calculate(tradingRecord, mostRecentPositions);

        // Add accrued cash flow of open position
        if (tradingRecord.getCurrentPosition().isOpened()) {
            calculate(tradingRecord.getCurrentPosition(), finalIndex);
        }
    }

    /**
     * Adjusts (intermediate) price to incorporate trading costs.
     *
     * @param rawPrice    the gross asset price
     * @param holdingCost share of the holding cost per period
     * @param isLongTrade true, if the entry trade type is BUY
     */
    static Num addCost(Num rawPrice, Num holdingCost, boolean isLongTrade) {
        Num netPrice;
        if (isLongTrade) {
            netPrice = rawPrice.minus(holdingCost);
        } else {
            netPrice = rawPrice.plus(holdingCost);
        }
        return netPrice;
    }

    /**
     * Fills with last value till the end of the series.
     */
    private void fillToTheEnd() {
        if (barSeries.getEndIndex() >= values.size()) {
            Num lastValue = values.get(values.size() - 1);
            values.addAll(Collections.nCopies(barSeries.getEndIndex() - values.size() + 1, lastValue));
        }
    }

    /**
     * Determines the the valid final index to be considered.
     *
     * @param position   the position
     * @param finalIndex index up until cash flows of open positions are considered
     * @param maxIndex   maximal valid index
     */
    static int determineEndIndex(Position position, int finalIndex, int maxIndex) {
        int idx = finalIndex;
        // After closing of position, no further accrual necessary
        if (position.getExit() != null) {
            idx = Math.min(position.getExit().getIndex(), finalIndex);
        }
        // Accrual at most until maximal index of asset data
        if (idx > maxIndex) {
            idx = maxIndex;
        }
        return idx;
    }
}
