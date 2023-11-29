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
package org.ta4j.core.criteria.pnl;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

/**
 * Net profit and loss in percentage criterion (relative PnL, excludes trading
 * costs).
 *
 * <p>
 * Defined as the position profit over the purchase price. The profit or loss in
 * percentage over the provided {@link Position position(s)}.
 * https://www.investopedia.com/ask/answers/how-do-you-calculate-percentage-gain-or-loss-investment/
 */
public class ProfitLossPercentageCriterion extends AbstractAnalysisCriterion {

    @Override
    public Num calculate(BarSeries series, Position position) {
        if (position.isClosed()) {
            Num entryPrice = position.getEntry().getValue();
            Num pnl = position.getProfit().dividedBy(entryPrice).multipliedBy(series.numOf(100));
            return pnl;
        }
        return series.numOf(0);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        return tradingRecord.getPositions()
                .stream()
                .filter(Position::isClosed)
                .skip(Math.max(0,
                        tradingRecord.getPositions().stream().filter(Position::isClosed).count() - mostRecentPositions))
                .map(position -> calculate(series, position))
                .reduce(series.numOf(0), Num::plus);
    }

    /**
     * The higher the criterion value, the better.
     */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

}
