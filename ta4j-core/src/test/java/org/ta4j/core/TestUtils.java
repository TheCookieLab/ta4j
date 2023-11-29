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
package org.ta4j.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

/**
 * Utility class for {@code Num} tests.
 */
public class TestUtils {

    /** Offset for double equality checking */
    public static final double GENERAL_OFFSET = 0.0001;

    private static Logger log = LoggerFactory.getLogger(TestUtils.class);

    /**
     * Verifies that the actual {@code Num} value is equal to the given
     * {@code String} representation.
     *
     * @param expected the given {@code String} representation to compare the actual
     *                 value to
     * @param actual   the actual {@code Num} value
     * @throws AssertionError if the actual value is not equal to the given
     *                        {@code String} representation
     */
    public static void assertNumEquals(String expected, Num actual) {
        assertEquals(actual.numOf(new BigDecimal(expected)), actual);
    }

    /**
     * Verifies that the actual {@code Num} value is equal to the given {@code Num}.
     *
     * @param expected the given {@code Num} representation to compare the actual
     *                 value to
     * @param actual   the actual {@code Num} value
     * @throws AssertionError if the actual value is not equal to the given
     *                        {@code Num} representation
     */
    public static void assertNumEquals(Num expected, Num actual) {
        assertEquals(expected, actual);
    }

    /**
     * Verifies that the actual {@code Num} value is equal to the given {@code int}
     * representation.
     *
     * 
     * @param expected the given {@code int} representation to compare the actual
     *                 value to
     * @param actual   the actual {@code Num} value
     * @throws AssertionError if the actual value is not equal to the given
     *                        {@code int} representation
     */
    public static void assertNumEquals(int expected, Num actual) {
        if (actual.isNaN()) {
            throw new AssertionError("Expected: " + expected + " Actual: " + actual);
        }
        assertEquals(actual.numOf(expected), actual);
    }

    /**
     * Verifies that the actual {@code Num} value is equal (within a positive
     * offset) to the given {@code double} representation.
     *
     * @param expected the given {@code double} representation to compare the actual
     *                 value to
     * @param actual   the actual {@code Num} value
     * @throws AssertionError if the actual value is not equal to the given
     *                        {@code double} representation
     */
    public static void assertNumEquals(double expected, Num actual) {
        assertEquals(expected, actual.doubleValue(), GENERAL_OFFSET);
    }

    /**
     * Verifies that the actual {@code Num} value is not equal to the given
     * {@code int} representation.
     *
     * @param actual     the actual {@code Num} value
     * @param unexpected the given {@code int} representation to compare the actual
     *                   value to
     * @throws AssertionError if the actual value is equal to the given {@code int}
     *                        representation
     */
    public static void assertNumNotEquals(int unexpected, Num actual) {
        assertNotEquals(actual.numOf(unexpected), actual);
    }

    /**
     * Verifies that two indicators have the same size and values to an offset
     * 
     * @param expected indicator of expected values
     * @param actual   indicator of actual values
     */
    public static void assertIndicatorEquals(Indicator<Num> expected, Indicator<Num> actual) {
        org.junit.Assert.assertEquals("Size does not match,", expected.getBarSeries().getBarCount(),
                actual.getBarSeries().getBarCount());
        for (int i = 0; i < expected.getBarSeries().getBarCount(); i++) {
            assertEquals(String.format("Failed at index %s: %s", i, actual.toString()),
                    expected.getValue(i).doubleValue(), actual.getValue(i).doubleValue(), GENERAL_OFFSET);
        }
    }

    /**
     * Verifies that two indicators have either different size or different values
     * to an offset
     * 
     * @param expected indicator of expected values
     * @param actual   indicator of actual values
     */
    public static void assertIndicatorNotEquals(Indicator<Num> expected, Indicator<Num> actual) {
        if (expected.getBarSeries().getBarCount() != actual.getBarSeries().getBarCount())
            return;
        for (int i = 0; i < expected.getBarSeries().getBarCount(); i++) {
            if (Math.abs(expected.getValue(i).doubleValue() - actual.getValue(i).doubleValue()) > GENERAL_OFFSET)
                return;
        }
        throw new AssertionError("Indicators match to " + GENERAL_OFFSET);
    }

    /**
     * Verifies that the actual {@code Num} value is not equal to the given
     * {@code String} representation.
     *
     * @param actual   the actual {@code Num} value
     * @param expected the given {@code String} representation to compare the actual
     *                 value to
     * @throws AssertionError if the actual value is equal to the given
     *                        {@code String} representation
     */
    public static void assertNumNotEquals(String expected, Num actual) {
        assertNotEquals(actual.numOf(new BigDecimal(expected)), actual);
    }

    /**
     * Verifies that the actual {@code Num} value is not equal to the given
     * {@code Num}.
     *
     * @param actual   the actual {@code Num} value
     * @param expected the given {@code Num} representation to compare the actual
     *                 value to
     * @throws AssertionError if the actual value is equal to the given {@code Num}
     *                        representation
     */
    public static void assertNumNotEquals(Num expected, Num actual) {
        assertNotEquals(expected, actual);
    }

    /**
     * Verifies that the actual {@code Num} value is not equal (within a positive
     * offset) to the given {@code double} representation.
     *
     * @param actual   the actual {@code Num} value
     * @param expected the given {@code double} representation to compare the actual
     *                 value to
     * @throws AssertionError if the actual value is equal to the given
     *                        {@code double} representation
     */
    public static void assertNumNotEquals(double expected, Num actual) {
        assertNotEquals(expected, actual.doubleValue(), GENERAL_OFFSET);
    }

    /**
     * Verifies that two indicators have the same size and values
     * 
     * @param expected indicator of expected values
     * @param actual   indicator of actual values
     */
    public static void assertIndicatorEquals(Indicator<Num> expected, Indicator<Num> actual, Num delta) {
        org.junit.Assert.assertEquals("Size does not match,", expected.getBarSeries().getBarCount(),
                actual.getBarSeries().getBarCount());
        for (int i = expected.getBarSeries().getBeginIndex(); i < expected.getBarSeries().getEndIndex(); i++) {
            // convert to DecimalNum via String (auto-precision) avoids Cast Class
            // Exception
            Num exp = DecimalNum.valueOf(expected.getValue(i).toString());
            Num act = DecimalNum.valueOf(actual.getValue(i).toString());
            Num result = exp.minus(act).abs();
            if (result.isGreaterThan(delta)) {
                log.debug("{} expected does not match", exp);
                log.debug("{} actual", act);
                log.debug("{} offset", delta);
                String expString = exp.toString();
                String actString = act.toString();
                int minLen = Math.min(expString.length(), actString.length());
                if (expString.length() > minLen)
                    expString = expString.substring(0, minLen) + "..";
                if (actString.length() > minLen)
                    actString = actString.substring(0, minLen) + "..";
                throw new AssertionError(
                        String.format("Failed at index %s: expected %s but actual was %s", i, expString, actString));
            }
        }
    }

    /**
     * Verifies that two indicators have either different size or different values
     * to an offset
     * 
     * @param expected indicator of expected values
     * @param actual   indicator of actual values
     * @param delta    num offset to which the indicators must be different
     */
    public static void assertIndicatorNotEquals(Indicator<Num> expected, Indicator<Num> actual, Num delta) {
        if (expected.getBarSeries().getBarCount() != actual.getBarSeries().getBarCount()) {
            return;
        }
        for (int i = 0; i < expected.getBarSeries().getBarCount(); i++) {
            Num exp = DecimalNum.valueOf(expected.getValue(i).toString());
            Num act = DecimalNum.valueOf(actual.getValue(i).toString());
            Num result = exp.minus(act).abs();
            if (result.isGreaterThan(delta)) {
                return;
            }
        }
        throw new AssertionError("Indicators match to " + delta);
    }

}
