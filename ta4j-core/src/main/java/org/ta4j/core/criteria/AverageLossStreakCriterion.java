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

import java.util.ArrayList;
import java.util.List;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * Calculates the average size of consecutive winning positions
 *
 */
public class AverageLossStreakCriterion extends AbstractAnalysisCriterion {

    @Override
    public Num calculate(BarSeries series, Position position) {
        if (position.hasLoss()) {
            return series.numOf(1);
        }
        return series.numOf(0);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        List<Integer> lossStreaks = new ArrayList<>();
        int lossStreakCount = 0;
        int lossStreak = 0;

        int startPositionIndex = Math.max(0, tradingRecord.getPositionCount() - mostRecentPositions);
        for (int i = startPositionIndex; i < tradingRecord.getPositionCount(); i++) {
            Num profit = tradingRecord.getPositions().get(i).getProfit();

            if (profit.isNegative()) {
                lossStreak++;

                if (i == tradingRecord.getPositionCount() - 1) {
                    lossStreaks.add(lossStreak);
                    lossStreakCount++;
                }

            } else {
                lossStreak = 0;
            }

            if (lossStreak == 0) {
                continue;
            }

            if (i < tradingRecord.getPositionCount() - 1
                    && tradingRecord.getPositions().get(i + 1).getProfit().multipliedBy(profit).isNegativeOrZero()) {
                if (profit.isNegative()) {
                    lossStreaks.add(lossStreak);
                    lossStreakCount++;
                }
            }
        }

        if (lossStreaks.isEmpty()) {
            return series.numOf(0);
        }

        double avgLossStreak = (double) lossStreaks.stream().mapToInt(Integer::intValue).sum() / lossStreakCount;

        return series.numOf(avgLossStreak).round(0);
    }

    /**
     * The higher the criterion value, the better.
     */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }
}
