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

import java.util.Collections;
import java.util.List;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.Returns;
import org.ta4j.core.num.Num;

/**
 * Expected Shortfall criterion.
 *
 * Measures the expected shortfall of the strategy log-return time-series.
 *
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/Expected_shortfall">https://en.wikipedia.org/wiki/Expected_shortfall</a>
 *
 */
public class ExpectedShortfallCriterion extends AbstractAnalysisCriterion {
    /**
     * Confidence level as absolute value (e.g. 0.95)
     */
    private final double confidence;

    /**
     * Constructor
     *
     * @param confidence the confidence level
     */
    public ExpectedShortfallCriterion(double confidence) {
        this.confidence = confidence;
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        if (position != null && position.getEntry() != null && position.getExit() != null) {
            Returns returns = new Returns(series, position, Returns.ReturnType.LOG);
            return calculateES(returns, confidence);
        }
        return series.numOf(0);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        Returns returns = new Returns(series, tradingRecord, Returns.ReturnType.LOG, mostRecentPositions);
        return calculateES(returns, confidence);
    }

    /**
     * Calculates the Expected Shortfall on the return series
     * 
     * @param returns    the corresponding returns
     * @param confidence the confidence level
     * @return the relative Expected Shortfall
     */
    private static Num calculateES(Returns returns, double confidence) {
        // select non-NaN returns
        List<Num> returnRates = returns.getValues().subList(1, returns.getSize() + 1);
        Num zero = returns.numOf(0);
        Num expectedShortfall = zero;
        if (!returnRates.isEmpty()) {
            // F(x_var) >= alpha (=1-confidence)
            int nInBody = (int) (returns.getSize() * confidence);
            int nInTail = returns.getSize() - nInBody;

            // calculate average tail loss
            Collections.sort(returnRates);
            List<Num> tailEvents = returnRates.subList(0, nInTail);
            Num sum = zero;
            for (int i = 0; i < nInTail; i++) {
                sum = sum.plus(tailEvents.get(i));
            }
            expectedShortfall = sum.dividedBy(returns.numOf(nInTail));

            // ES is non-positive
            if (expectedShortfall.isGreaterThan(zero)) {
                expectedShortfall = zero;
            }
        }
        return expectedShortfall;
    }

    /** The higher the criterion value, the better. */
    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }
}
