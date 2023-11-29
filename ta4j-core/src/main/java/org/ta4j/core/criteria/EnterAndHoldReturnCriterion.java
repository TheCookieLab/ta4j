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
import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * Enter and hold criterion.
 *
 * Calculates the return if a enter-and-hold strategy was used:
 * 
 * <ul>
 * <li>For {@link #tradeType} = {@link TradeType#BUY}: buying on the first bar
 * and selling on the last bar.
 * <li>For {@link #tradeType} = {@link TradeType#SELL}: selling on the first bar
 * and buying on the last bar.
 * </ul>
 *
 * @see <a href=
 *      "http://en.wikipedia.org/wiki/Buy_and_hold">http://en.wikipedia.org/wiki/Buy_and_hold</a>
 */
public class EnterAndHoldReturnCriterion extends AbstractAnalysisCriterion {

    private final TradeType tradeType;

    /**
     * Constructor
     * 
     * <p>
     * For buy-and-hold strategy.
     */
    public EnterAndHoldReturnCriterion() {
        this(TradeType.BUY);
    }

    /**
     * Constructor.
     * 
     * @param tradeType the {@link TradeType} used to open the position
     */
    public EnterAndHoldReturnCriterion(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        return createEnterAndHoldTrade(series, position.getEntry().getIndex(), position.getExit().getIndex())
                .getGrossReturn(series);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return createEnterAndHoldTrade(series).getGrossReturn(series);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        return this.calculate(series, tradingRecord);
    }

    /** The higher the criterion value the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

    private Position createEnterAndHoldTrade(BarSeries series) {
        return createEnterAndHoldTrade(series, series.getBeginIndex(), series.getEndIndex());
    }

    private Position createEnterAndHoldTrade(BarSeries series, int beginIndex, int endIndex) {
        Position position = new Position(this.tradeType);
        position.operate(beginIndex, series.getBar(beginIndex).getEndTime(), series.getBar(beginIndex).getClosePrice(),
                series.numOf(1));
        position.operate(endIndex, series.getBar(beginIndex).getEndTime(), series.getBar(endIndex).getClosePrice(),
                series.numOf(1));
        return position;
    }
}
