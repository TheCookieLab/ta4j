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
package org.ta4j.core.indicators.helpers;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import static org.ta4j.core.TestUtils.assertNumEquals;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class ConsecutiveBooleanStreakIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    private BarSeries barSeries;

    public ConsecutiveBooleanStreakIndicatorTest(Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        barSeries = new MockBarSeries(numFunction, 1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d, 10d);
    }

    @Test
    public void givenTrueCheckValue_whenGetValue_thenReturnsCorrectStreak() {
        FixedBooleanIndicator booleanValues = new FixedBooleanIndicator(barSeries, true, true, true, false, true, true,
                false, false, true, false);

        ConsecutiveBooleanStreakIndicator subject = new ConsecutiveBooleanStreakIndicator(booleanValues, true);

        assertNumEquals(3, subject.getValue(2));
        assertNumEquals(0, subject.getValue(3));
        assertNumEquals(2, subject.getValue(5));
        assertNumEquals(0, subject.getValue(9));
    }

    @Test
    public void givenFalseCheckValue_whenGetValue_thenReturnsCorrectStreak() {
        FixedBooleanIndicator booleanValues = new FixedBooleanIndicator(barSeries, true, true, true, false, true, true,
                false, false, true, false);

        ConsecutiveBooleanStreakIndicator subject = new ConsecutiveBooleanStreakIndicator(booleanValues, false);

        assertNumEquals(0, subject.getValue(2));
        assertNumEquals(1, subject.getValue(3));
        assertNumEquals(0, subject.getValue(5));
        assertNumEquals(2, subject.getValue(7));
    }

    @Test
    public void testEdgeCases() {
        FixedBooleanIndicator booleanValues = new FixedBooleanIndicator(barSeries, true, false, false, false, false,
                false, false, false, false, false);

        ConsecutiveBooleanStreakIndicator subjectTrue = new ConsecutiveBooleanStreakIndicator(booleanValues, true);
        ConsecutiveBooleanStreakIndicator subjectFalse = new ConsecutiveBooleanStreakIndicator(booleanValues, false);

        assertNumEquals(1, subjectTrue.getValue(0));
        assertNumEquals(9, subjectFalse.getValue(9));

        booleanValues = new FixedBooleanIndicator(barSeries, true, true, true, true, true, true, true, true, true,
                true);
        subjectTrue = new ConsecutiveBooleanStreakIndicator(booleanValues, true);
        subjectFalse = new ConsecutiveBooleanStreakIndicator(booleanValues, false);

        assertNumEquals(10, subjectTrue.getValue(9));
        assertNumEquals(0, subjectFalse.getValue(9));
    }
}
