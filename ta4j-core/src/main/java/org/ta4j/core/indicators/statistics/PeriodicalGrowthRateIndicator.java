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
package org.ta4j.core.indicators.statistics;

import static org.ta4j.core.num.NaN.NaN;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/**
 * Periodical Growth Rate indicator.
 *
 * In general the 'Growth Rate' is useful for comparing the average returns of
 * investments in stocks or funds and can be used to compare the performance
 * e.g. comparing the historical returns of stocks with bonds.
 *
 * This indicator has the following characteristics: - the calculation is
 * timeframe dependendant. The timeframe corresponds to the number of trading
 * events in a period, e. g. the timeframe for a US trading year for end of day
 * bars would be '251' trading days - the result is a step function with a
 * constant value within a timeframe - NaN values while index is smaller than
 * timeframe, e.g. timeframe is year, than no values are calculated before a
 * full year is reached - NaN values for incomplete timeframes, e.g. timeframe
 * is a year and your timeseries contains data for 11,3 years, than no values
 * are calculated for the remaining 0,3 years - the method 'getTotalReturn'
 * calculates the total return over all returns of the coresponding timeframes
 *
 *
 * Further readings: Good sumary on 'Rate of Return':
 * https://en.wikipedia.org/wiki/Rate_of_return Annual return / CAGR:
 * http://www.investopedia.com/terms/a/annual-return.asp Annualized Total
 * Return: http://www.investopedia.com/terms/a/annualized-total-return.asp
 * Annualized Return vs. Cumulative Return:
 * http://www.fool.com/knowledge-center/2015/11/03/annualized-return-vs-cumulative-return.aspx
 *
 */
public class PeriodicalGrowthRateIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> indicator;
    private final int barCount;
    private final Num one;

    /**
     * Constructor. Example: use barCount = 251 and "end of day"-bars for annual
     * behaviour in the US (http://tradingsim.com/blog/trading-days-in-a-year/).
     * 
     * @param indicator the indicator
     * @param barCount  the time frame
     */
    public PeriodicalGrowthRateIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator);
        this.indicator = indicator;
        this.barCount = barCount;
        one = numOf(1);
    }

    /**
     * Gets the TotalReturn from the calculated results of the method 'calculate'.
     * For a barCount = number of trading days within a year (e. g. 251 days in the
     * US) and "end of day"-bars you will get the 'Annualized Total Return'. Only
     * complete barCounts are taken into the calculation.
     * 
     * @return the total return from the calculated results of the method
     *         'calculate'
     */
    public Num getTotalReturn() {

        Num totalProduct = one;
        int completeTimeFrames = (getBarSeries().getBarCount() / barCount);

        for (int i = 1; i <= completeTimeFrames; i++) {
            int index = i * barCount;
            Num currentReturn = getValue(index);

            // Skip NaN at the end of a series
            if (currentReturn != NaN) {
                currentReturn = currentReturn.plus(one);
                totalProduct = totalProduct.multipliedBy(currentReturn);
            }
        }

        return totalProduct.pow(one.dividedBy(numOf(completeTimeFrames)));
    }

    @Override
    protected Num calculate(int index) {

        Num currentValue = indicator.getValue(index);

        int helpPartialTimeframe = index % barCount;
        // TODO: implement Num.floor()
        Num helpFullTimeframes = numOf(
                Math.floor(numOf(indicator.getBarSeries().getBarCount()).dividedBy(numOf(barCount)).doubleValue()));
        Num helpIndexTimeframes = numOf(index).dividedBy(numOf(barCount));

        Num helpPartialTimeframeHeld = numOf(helpPartialTimeframe).dividedBy(numOf(barCount));
        Num partialTimeframeHeld = (helpPartialTimeframeHeld.isZero()) ? one : helpPartialTimeframeHeld;

        // Avoid calculations of returns:
        // a.) if index number is below timeframe
        // e.g. timeframe = 365, index = 5 => no calculation
        // b.) if at the end of a series incomplete timeframes would remain
        Num timeframedReturn = NaN;
        if ((index >= barCount) /* (a) */ && (helpIndexTimeframes.isLessThan(helpFullTimeframes)) /* (b) */) {
            Num movingValue = indicator.getValue(index - barCount);
            Num movingSimpleReturn = (currentValue.minus(movingValue)).dividedBy(movingValue);

            timeframedReturn = one.plus(movingSimpleReturn).pow(one.dividedBy(partialTimeframeHeld)).minus(one);
        }

        return timeframedReturn;

    }
}
