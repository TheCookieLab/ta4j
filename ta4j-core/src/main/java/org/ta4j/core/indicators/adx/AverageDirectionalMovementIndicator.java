package org.ta4j.core.indicators.adx;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.RecursiveCachedIndicator;
import org.ta4j.core.indicators.helpers.DirectionalMovementIndicator;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;

/**
 * Average directional movement indicator (ADMI/ADX).
 * <p>
 * </p>
 */
public class AverageDirectionalMovementIndicator extends RecursiveCachedIndicator<Num> {

    private final int timeFrame;
    private final DirectionalMovementIndicator dm;

    public AverageDirectionalMovementIndicator(BarSeries series, int timeFrame) {
        super(series);
        this.timeFrame = timeFrame;
        this.dm = new DirectionalMovementIndicator(series, timeFrame);
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return DoubleNum.valueOf(1);
        }
        Num nbPeriods = DoubleNum.valueOf(timeFrame);
        Num nbPeriodsMinusOne = DoubleNum.valueOf(timeFrame - 1);
        return getValue(index - 1).multipliedBy(nbPeriodsMinusOne).dividedBy(nbPeriods).plus(dm.getValue(index).dividedBy(nbPeriods));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " timeFrame: " + timeFrame;
    }
}
