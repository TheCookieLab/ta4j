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
package org.ta4j.core.indicators;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.SumIndicator;
import org.ta4j.core.num.Num;

/**
 * Coppock Curve indicator.
 *
 * @see <a href=
 *      "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:coppock_curve">
 *      http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:coppock_curve</a>
 */
public class CoppockCurveIndicator extends CachedIndicator<Num> {

    private final WMAIndicator wma;

    /**
     * Constructor with default values: <br/>
     * - longRoCBarCount=14 <br/>
     * - shortRoCBarCount=11 <br/>
     * - wmaBarCount=10
     *
     * @param indicator the indicator
     */
    public CoppockCurveIndicator(Indicator<Num> indicator) {
        this(indicator, 14, 11, 10);
    }

    /**
     * Constructor.
     * 
     * @param indicator        the indicator (usually close price)
     * @param longRoCBarCount  the time frame for long term RoC
     * @param shortRoCBarCount the time frame for short term RoC
     * @param wmaBarCount      the time frame (for WMA)
     */
    public CoppockCurveIndicator(Indicator<Num> indicator, int longRoCBarCount, int shortRoCBarCount, int wmaBarCount) {
        super(indicator);
        SumIndicator sum = new SumIndicator(new ROCIndicator(indicator, longRoCBarCount),
                new ROCIndicator(indicator, shortRoCBarCount));
        wma = new WMAIndicator(sum, wmaBarCount);
    }

    @Override
    protected Num calculate(int index) {
        return wma.getValue(index);
    }
}
