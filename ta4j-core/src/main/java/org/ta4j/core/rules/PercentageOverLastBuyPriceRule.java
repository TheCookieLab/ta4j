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
public class PercentageOverLastBuyPriceRule extends AbstractRule {

    /**
     * The first indicator
     */
    public final Indicator<Num> first;

    public final Num percentage;

    /**
     * Constructor. Satisfied when the difference between the indicator value at
     * index i and the last trading record entry's price per asset is greater (as a
     * percentage) than the given percentage threshold
     *
     *
     * @param first      the first indicator
     * @param percentage 10% is expressed as 0.1
     */
    public PercentageOverLastBuyPriceRule(Indicator<Num> first, Num percentage) {
        this.first = first;
        this.percentage = percentage;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (tradingRecord == null || tradingRecord.getLastEntry() == null) {
            return false;
        }

        final boolean satisfied = (first.getValue(index).minus(tradingRecord.getLastEntry().getPricePerAsset()))
                .dividedBy(first.getValue(index))
                .isGreaterThan(percentage);
        return satisfied;
    }

}
