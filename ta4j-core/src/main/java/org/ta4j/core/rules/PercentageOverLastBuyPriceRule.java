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
    private Indicator<Num> first;

    private Num percentage;

    /**
     * Constructor.
     *
     * @param first the first indicator
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

        final boolean satisfied = (first.getValue(index).minus(tradingRecord.getLastEntry().getPricePerAsset())).dividedBy(first.getValue(index)).isGreaterThan(percentage);
        return satisfied;
    }

}
