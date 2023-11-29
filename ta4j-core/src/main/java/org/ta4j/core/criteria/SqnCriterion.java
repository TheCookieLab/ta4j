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
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.helpers.StandardDeviationCriterion;
import org.ta4j.core.criteria.pnl.ProfitLossCriterion;
import org.ta4j.core.num.Num;

/**
 * The SQN ("System Quality Number") Criterion.
 *
 * @see <a href=
 *      "https://indextrader.com.au/van-tharps-sqn/">https://indextrader.com.au/van-tharps-sqn/</a>
 */
public class SqnCriterion extends AbstractAnalysisCriterion {

    private final AnalysisCriterion criterion;
    private final StandardDeviationCriterion standardDeviationCriterion;
    private final NumberOfPositionsCriterion numberOfPositionsCriterion = new NumberOfPositionsCriterion();

    /**
     * The number to be used for the part of <code>√(numberOfPositions)</code>
     * within the SQN-Formula when there are more than 100 trades. If this value is
     * <code>null</code>, then the number of positions calculated by
     * {@link #numberOfPositionsCriterion} is used instead.
     */
    private final Integer nPositions;

    /**
     * Constructor.
     *
     * <p>
     * Uses ProfitLossCriterion for {@link #criterion}.
     */
    public SqnCriterion() {
        this(new ProfitLossCriterion());
    }

    /**
     * Constructor.
     *
     * @param criterion the Criterion (e.g. ProfitLossCriterion or
     *                  ExpectancyCriterion)
     */
    public SqnCriterion(AnalysisCriterion criterion) {
        this(criterion, null);
    }

    /**
     * Constructor.
     *
     * @param criterion  the Criterion (e.g. ProfitLossCriterion or
     *                   ExpectancyCriterion)
     * @param nPositions the {@link #nPositions} (optional)
     */
    public SqnCriterion(AnalysisCriterion criterion, Integer nPositions) {
        this.criterion = criterion;
        this.nPositions = nPositions;
        this.standardDeviationCriterion = new StandardDeviationCriterion(criterion);
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        Num numberOfPositions = numberOfPositionsCriterion.calculate(series, position);
        Num pnl = criterion.calculate(series, position);
        Num avgPnl = pnl.dividedBy(numberOfPositions);
        Num stdDevPnl = standardDeviationCriterion.calculate(series, position);
        if (stdDevPnl.isZero()) {
            return series.numOf(0);
        }
        // SQN = (Average (PnL) / StdDev(PnL)) * SquareRoot(NumberOfTrades)
        return avgPnl.dividedBy(stdDevPnl).multipliedBy(numberOfPositions.sqrt());
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
        Num numberOfPositions = numberOfPositionsCriterion.calculate(series, tradingRecord, mostRecentPositions);
        Num pnl = criterion.calculate(series, tradingRecord);
        Num avgPnl = pnl.dividedBy(numberOfPositions);
        Num stdDevPnl = standardDeviationCriterion.calculate(series, tradingRecord, mostRecentPositions);
        if (stdDevPnl.isZero()) {
            return series.numOf(0);
        }
        if (nPositions != null && numberOfPositions.isGreaterThan(series.numOf(100))) {
            numberOfPositions = series.numOf(nPositions);
        }
        // SQN = (Average (PnL) / StdDev(PnL)) * SquareRoot(NumberOfTrades)
        return avgPnl.dividedBy(stdDevPnl).multipliedBy(numberOfPositions.sqrt());
    }

    /**
     * The higher the criterion value, the better.
     */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

}
