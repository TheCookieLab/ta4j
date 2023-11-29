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

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.ta4j.core.num.Num;

/**
 * Indicator over a {@link BarSeries bar series}. <p/p> For each index of the
 * bar series, returns a value of type <b>T</b>.
 *
 * @param <T> the type of returned value (Double, Boolean, etc.)
 */
public interface Indicator<T> {

    /**
     * @param index the bar index
     * @return the value of the indicator
     */
    T getValue(int index);

    /**
     * @return the related bar series
     */
    BarSeries getBarSeries();

    /**
     * @return the {@link Num Num extending class} for the given {@link Number}
     */
    Num numOf(Number number);

    /**
     * Returns all values from an {@link Indicator} as an array of Doubles. The
     * returned doubles could have a minor loss of precise, if {@link Indicator} was
     * based on {@link Num Num}.
     *
     * @param ref      the indicator
     * @param index    the index
     * @param barCount the barCount
     * @return array of Doubles within the barCount
     */
    static Double[] toDouble(Indicator<Num> ref, int index, int barCount) {
        int startIndex = Math.max(0, index - barCount + 1);
        return IntStream.range(startIndex, startIndex + barCount)
                .mapToObj(ref::getValue)
                .map(Num::doubleValue)
                .toArray(Double[]::new);
    }

    default Stream<T> stream() {
        return IntStream.range(getBarSeries().getBeginIndex(), getBarSeries().getEndIndex() + 1)
                .mapToObj(this::getValue);
    }

}
