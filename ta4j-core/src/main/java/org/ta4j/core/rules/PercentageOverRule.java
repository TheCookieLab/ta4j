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

import org.ta4j.core.Indicator;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 *
 * @author David
 */
public class PercentageOverRule extends AbstractRule {

    /**
     * The first indicator
     */
    public final Indicator<Num> first;
    /**
     * The second indicator
     */
    public final Indicator<Num> second;

    public final Num percentage;

    /**
     * Constructor. Satisfied when the difference between the first and second
     * indicators is greater (as a percentage) than the provided percentage
     * threshold
     *
     * @param first      the first indicator
     * @param second     the second indicator
     * @param percentage the difference between the first and second indicators as a
     *                   percentage tested if greater than this value
     */
    public PercentageOverRule(Indicator<Num> first, Indicator<Num> second, Num percentage) {
        this.first = first;
        this.second = second;
        this.percentage = percentage;
    }

    /**
     * * Satisfied when the difference between the first and second indicators is
     * greater (as a percentage) than the provided percentage threshold
     *
     * @param index
     * @param tradingRecord
     * @return
     */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        final boolean satisfied = (first.getValue(index).minus(second.getValue(index))).dividedBy(first.getValue(index))
                .isGreaterThan(percentage);
        return satisfied;
    }

    /**
     * * Satisfied when the difference between the first and second indicators is
     * greater (as a percentage) than the provided percentage threshold
     *
     * @param index
     * @param tradingRecord
     * @return
     */
    @Override
    public boolean isSatisfied(int index) {
        return isSatisfied(index, null);
    }

}
