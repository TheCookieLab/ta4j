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
package org.ta4j.core.mocks;

import java.util.List;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

public class MockAnalysisCriterion implements AnalysisCriterion {

    private final BarSeries series;
    private final List<Num> values;

    /**
     * Constructor.
     * 
     * @param series BarSeries of the AnalysisCriterion
     * @param values AnalysisCriterion values
     */
    public MockAnalysisCriterion(BarSeries series, List<Num> values) {
        this.series = series;
        this.values = values;
    }

    /**
     * Gets the final criterion value.
     * 
     * @param series   BarSeries is ignored
     * @param position is ignored
     */
    public Num calculate(BarSeries series, Position position) {
        return values.get(values.size() - 1);
    }

    /**
     * Gets the final criterion value.
     * 
     * @param series        BarSeries is ignored
     * @param tradingRecord is ignored
     */
    public Num calculate(BarSeries series, TradingRecord tradingRecord) {
        return values.get(values.size() - 1);
    }

    /***
     * 
     * @param series
     * @param tradingRecord
     * @param mostRecentPositions
     * @return
     */
    @Override
    public Num calculate(BarSeries series, TradingRecord tradingRecord, int mostRecentPositions) {
        return this.calculate(series, tradingRecord);
    }

    /**
     * Compares two criterion values and returns true if first value is greater than
     * second value, false otherwise.
     * 
     * @param criterionValue1 first value
     * @param criterionValue2 second value
     * @return boolean indicating first value is greater than second value
     */
    public boolean betterThan(Num criterionValue1, Num criterionValue2) {
        return criterionValue1.isGreaterThan(criterionValue2);
    }

    public BarSeries getSeries() {
        return series;
    }

}
