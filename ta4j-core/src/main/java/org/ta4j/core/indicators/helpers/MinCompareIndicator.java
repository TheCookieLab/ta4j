package org.ta4j.core.indicators.helpers;

import org.ta4j.core.Indicator;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 *
 * @author David
 */
public class MinCompareIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> first;
    private final Indicator<Num> second;

    public MinCompareIndicator(BarSeries series, Indicator<Num> first, Indicator<Num> second) {
        super(series);
        this.first = first;
        this.second = second;
    }

    @Override
    protected Num calculate(int index) {
        Num firstValue = first.getValue(index);
        Num secondValue = second.getValue(index);
        return firstValue.min(secondValue);
    }

}
