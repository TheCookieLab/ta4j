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
package org.ta4j.core.criteria;

import java.util.function.Function;

import org.ta4j.core.BarSeries;
import org.ta4j.core.ExternalCriterionTest;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.XlsTestsUtils;
import org.ta4j.core.num.Num;

public class XLSCriterionTest implements ExternalCriterionTest {

    private Class<?> clazz;
    private String fileName;
    private int criterionColumn;
    private int statesColumn;
    private BarSeries cachedSeries = null;
    private final Function<Number, Num> numFunction;

    /**
     * Constructor.
     * 
     * @param clazz           class containing the file resources
     * @param fileName        file name of the file containing the workbook
     * @param criterionColumn column number containing the calculated criterion
     *                        values
     * @param statesColumn    column number containing the trading record states
     */
    public XLSCriterionTest(Class<?> clazz, String fileName, int criterionColumn, int statesColumn,
            Function<Number, Num> numFunction) {
        this.clazz = clazz;
        this.fileName = fileName;
        this.criterionColumn = criterionColumn;
        this.statesColumn = statesColumn;
        this.numFunction = numFunction;
    }

    /**
     * Gets the BarSeries from the XLS file. The BarSeries is cached so that
     * subsequent calls do not execute getSeries.
     * 
     * @return BarSeries from the file
     * @throws Exception if getSeries throws IOException or DataFormatException
     */
    public BarSeries getSeries() throws Exception {
        if (cachedSeries == null) {
            cachedSeries = XlsTestsUtils.getSeries(clazz, fileName, numFunction);
        }
        return cachedSeries;
    }

    /**
     * Gets the final criterion value from the XLS file given the parameters.
     * 
     * @param params criterion parameters
     * @return Num final criterion value
     * @throws Exception if getFinalCriterionValue throws IOException or
     *                   DataFormatException
     */
    public Num getFinalCriterionValue(Object... params) throws Exception {
        return XlsTestsUtils.getFinalCriterionValue(clazz, fileName, criterionColumn, getSeries().function(), params);
    }

    /**
     * Gets the trading record from the XLS file.
     * 
     * @return TradingRecord from the file
     */
    public TradingRecord getTradingRecord() throws Exception {
        return XlsTestsUtils.getTradingRecord(clazz, fileName, statesColumn, getSeries().function());
    }

}
