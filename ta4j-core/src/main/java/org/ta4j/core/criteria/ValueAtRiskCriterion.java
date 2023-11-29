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
 * Value at Risk criterion.
 *
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/Value_at_risk">https://en.wikipedia.org/wiki/Value_at_risk</a>
 */
public class ValueAtRiskCriterion extends AbstractAnalysisCriterion {
    /**
     * Confidence level as absolute value (e.g. 0.95)
     */
    private final Double confidence;

    /**
     * Constructor
     *
     * @param confidence the confidence level
     */
    public ValueAtRiskCriterion(Double confidence) {
        this.confidence = confidence;
    }

    @Override
    public Num calculate(BarSeries series, Position position) {
        if (position != null && position.isClosed()) {
            Returns returns = new Returns(series, position, Returns.ReturnType.LOG);
            return calculateVaR(returns, confidence);
        }
        return series.numOf(0);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        Returns returns = new Returns(series, tradingRecord, Returns.ReturnType.LOG, mostRecentPositions);
        return calculateVaR(returns, confidence);
    }

    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return this.calculate(series, tradingRecord, tradingRecord.getPositionCount());
    }

    /**
     * Calculates the VaR on the return series
     * 
     * @param returns    the corresponding returns
     * @param confidence the confidence level
     * @return the relative Value at Risk
     */
    private static Num calculateVaR(Returns returns, double confidence) {
        Num zero = returns.numOf(0);
        // select non-NaN returns
        List<Num> returnRates = returns.getValues().subList(1, returns.getSize() + 1);
        Num valueAtRisk = zero;
        if (!returnRates.isEmpty()) {
            // F(x_var) >= alpha (=1-confidence)
            int nInBody = (int) (returns.getSize() * confidence);
            int nInTail = returns.getSize() - nInBody;

            // The series is not empty, nInTail > 0
            Collections.sort(returnRates);
            valueAtRisk = returnRates.get(nInTail - 1);

            // VaR is non-positive
            if (valueAtRisk.isGreaterThan(zero)) {
                valueAtRisk = zero;
            }
        }
        return valueAtRisk;
    }

    @Override
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        // because it represents a loss, VaR is non-positive
        return criterionValue1.isGreaterThan(criterionValue2);
    }
}
