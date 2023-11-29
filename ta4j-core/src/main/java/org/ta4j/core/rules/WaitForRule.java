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

import org.ta4j.core.Bar;
import org.ta4j.core.Trade;
import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.TradingRecord;

/**
 * A {@link org.ta4j.core.Rule} which waits for a number of {@link Bar} after a
 * trade.
 *
 * Satisfied after a fixed number of bars since the last trade.
 */
public class WaitForRule extends AbstractRule {

    /**
     * The type of the trade since we have to wait for
     */
    private final TradeType tradeType;

    /**
     * The number of bars to wait for
     */
    private final int numberOfBars;

    /**
     * Constructor.
     *
     * @param tradeType    the type of the trade since we have to wait for
     * @param numberOfBars the number of bars to wait for
     */
    public WaitForRule(TradeType tradeType, int numberOfBars) {
        this.tradeType = tradeType;
        this.numberOfBars = numberOfBars;
    }

    /** This rule uses the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        boolean satisfied = false;
        // No trading history, no need to wait
        if (tradingRecord != null) {
            Trade lastTrade = tradingRecord.getLastTrade(tradeType);
            if (lastTrade != null) {
                int currentNumberOfBars = index - lastTrade.getIndex();
                satisfied = currentNumberOfBars >= numberOfBars;
            }
        }
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }
}
