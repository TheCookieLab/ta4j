package org.ta4j.core.rules;

import org.ta4j.core.Indicator;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;

/**
 * Is satisfied when the first indicator is below the second indicator by the
 * specified percentage i1 = 100 i2 =
 *
 * @author David
 */
public class PercentageUnderLastBuyPriceRule extends AbstractRule {

    /**
     * The first indicator
     */
    public final Indicator<Num> first;

    public final Num percentage;

    /**
     * Constructor. Satisfied when the difference between the indicator value at
     * index i and the last trading record entry's price per asset is less (as a
     * percentage) than the given percentage threshold
     *
     * @param first the first indicator
     * @param percentage 10% is expressed as 0.1
     */
    public PercentageUnderLastBuyPriceRule(Indicator<Num> first, Num percentage) {
        this.first = first;
        this.percentage = percentage;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        if (tradingRecord == null || tradingRecord.getLastEntry() == null) {
            return false;
        }

        final boolean satisfied = (first.getValue(index).minus(tradingRecord.getLastEntry().getPricePerAsset()).dividedBy(first.getValue(index))).isLessThan(percentage);
        return satisfied;
    }

}
