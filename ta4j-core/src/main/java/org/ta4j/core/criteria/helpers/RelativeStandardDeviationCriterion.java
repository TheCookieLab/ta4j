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
package org.ta4j.core.criteria.helpers;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.num.Num;

/**
 * Standard deviation criterion in percentage (also known as Coefficient of
 * Variation (CV)).
 * 
 * <p>
 * Calculates the standard deviation in percentage for a Criterion.
 * 
 * @see <a href=
 *      "https://www.investopedia.com/terms/c/coefficientofvariation.asp">https://www.investopedia.com/terms/c/coefficientofvariation.asp</a>
 */
public class RelativeStandardDeviationCriterion extends AbstractAnalysisCriterion {

    private final StandardDeviationCriterion standardDeviationCriterion;
    private final AverageCriterion averageCriterion;

    /**
     * Constructor.
     * 
     * @param criterion the criterion from which the "relative standard deviation"
     *                  is calculated
     */
    public RelativeStandardDeviationCriterion(AnalysisCriterion criterion) {
        this.standardDeviationCriterion = new StandardDeviationCriterion(criterion);
        this.averageCriterion = new AverageCriterion(criterion);
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        Num average = averageCriterion.calculate(series, position);
        return standardDeviationCriterion.calculate(series, position).dividedBy(average);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        if (tradingRecord.getPositions().isEmpty()) {
            return series.numOf(0);
        }
        Num average = averageCriterion.calculate(series, tradingRecord, mostRecentPositions);
        return standardDeviationCriterion.calculate(series, tradingRecord, mostRecentPositions).dividedBy(average);
    }

    /** The lower the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isLessThan(criterionValue2);
    }

}
