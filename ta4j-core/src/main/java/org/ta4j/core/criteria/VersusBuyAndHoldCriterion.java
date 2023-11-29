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

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * Versus "buy and hold" criterion.
 *
 * Compares the value of a provided {@link AnalysisCriterion criterion} with the
 * value of a "buy and hold".
 */
public class VersusBuyAndHoldCriterion extends AbstractAnalysisCriterion {

    private final AnalysisCriterion criterion;

    /**
     * Constructor.
     * 
     * @param criterion an analysis criterion to be compared
     */
    public VersusBuyAndHoldCriterion(AnalysisCriterion criterion) {
        this.criterion = criterion;
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        TradingRecord fakeRecord = createBuyAndHoldTradingRecord(series);
        return criterion.calculate(series, position).dividedBy(criterion.calculate(series, fakeRecord));
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        TradingRecord fakeRecord;

        if (tradingRecord.getPositionCount() > 0) {
            int startPositionIndex = Math.max(0, tradingRecord.getPositionCount() - mostRecentPositions);
            fakeRecord = createBuyAndHoldTradingRecord(series,
                    tradingRecord.getPositions().get(startPositionIndex).getEntry().getIndex(),
                    tradingRecord.getLastExit().getIndex());
        } else {
            fakeRecord = createBuyAndHoldTradingRecord(series);
        }

        Num tradingRecordReturn = criterion.calculate(series, tradingRecord, mostRecentPositions);
        Num fakeRecordReturn = criterion.calculate(series, fakeRecord);

        return tradingRecordReturn.dividedBy(fakeRecordReturn);
    }

    /** The higher the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

    private TradingRecord createBuyAndHoldTradingRecord(BarSeries series) {
        return createBuyAndHoldTradingRecord(series, series.getBeginIndex(), series.getEndIndex());
    }

    private TradingRecord createBuyAndHoldTradingRecord(BarSeries series, int beginIndex, int endIndex) {
        TradingRecord fakeRecord = new BaseTradingRecord();
        fakeRecord.enter(beginIndex, series.getBar(beginIndex).getEndTime(), series.getBar(beginIndex).getClosePrice(),
                series.numOf(1));
        fakeRecord.exit(endIndex, series.getBar(beginIndex).getEndTime(), series.getBar(endIndex).getClosePrice(),
                series.numOf(1));
        return fakeRecord;
    }
}
