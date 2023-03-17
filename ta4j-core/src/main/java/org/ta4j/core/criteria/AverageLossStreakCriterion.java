/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2022 Ta4j Organization & respective
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

            if (i < tradingRecord.getPositionCount() - 1 && tradingRecord.getPositions().get(i + 1).getProfit().multipliedBy(profit).isNegativeOrZero()) {
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