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
public class PercentageUnderRule extends AbstractRule {

    /**
     * The first indicator
     */
    private Indicator<Num> first;
    /**
     * The second indicator
     */
    private Indicator<Num> second;

    private Num percentage;

    /**
     * Constructor.
     *
     * @param first the first indicator
     * @param second the second indicator
     * @param percentage
     */
    public PercentageUnderRule(Indicator<Num> first, Indicator<Num> second, Num percentage) {
        this.first = first;
        this.second = second;
        this.percentage = percentage;
    }

    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        final boolean satisfied = (first.getValue(index).minus(second.getValue(index)).dividedBy(first.getValue(index))).isLessThan(percentage);
        return satisfied;
    }

}
