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
/**
 * {@link org.ta4j.core.num.Num Num} interface and implementations of
 * {@link org.ta4j.core.num.NaN NaN}, {@link org.ta4j.core.num.DoubleNum
 * DoubleNum} and {@link org.ta4j.core.num.DecimalNum PrecisionNum}
 *
 * The {@link org.ta4j.core.num.Num Num interface} enables the use of different
 * delegates (Double, {@link java.math.BigDecimal BigDecimal}, ...) for storage
 * and calculations in {@link org.ta4j.core.BarSeries BarSeries},
 * {@link org.ta4j.core.Bar Bars}, {@link org.ta4j.core.Indicator Indicators}
 * and {@link org.ta4j.core.criteria.AbstractAnalysisCriterion
 * AnalysisCriterions}
 */
package org.ta4j.core.num;
