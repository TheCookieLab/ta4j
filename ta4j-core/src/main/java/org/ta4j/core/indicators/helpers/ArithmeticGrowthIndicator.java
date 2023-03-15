package org.ta4j.core.indicators.helpers;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 *
 * @author David
 */
public class ArithmeticGrowthIndicator extends CachedIndicator<Num> {

    private final Num growthConstant;
    private final Num initialValue;

    public ArithmeticGrowthIndicator(BarSeries series, Num initialValue, Num growthConstant) {
        super(series);
        this.growthConstant = growthConstant;
        this.initialValue = initialValue;
    }

    public ArithmeticGrowthIndicator(BarSeries series) {
        this(series, series.numOf(0), series.numOf(1));
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return this.initialValue;
        }

        Num value = this.getValue(index - 1).plus(this.growthConstant);

        return value;
    }

}
