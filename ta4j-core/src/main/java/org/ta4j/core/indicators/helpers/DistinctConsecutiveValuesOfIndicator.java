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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;

/***
 * 
 * @author David
 */
public class DistinctConsecutiveValuesOfIndicator extends CachedIndicator<Num> {

    private final Indicator<Num> indicator;

    public DistinctConsecutiveValuesOfIndicator(Indicator<Num> indicator) {
        super(indicator.getBarSeries());
        this.indicator = indicator;
    }

    @Override
    protected Num calculate(int index) {
        Stream.Builder<Num> builder = Stream.builder();
        this.indicator.stream().reduce(null, (prev, curr) -> {
            if (prev == null || !curr.equals(prev)) {
                builder.add(curr);
            }
            return curr;
        });

        List<Num> values = builder.build().collect(Collectors.toList());
        int safeIndex = Math.min(values.size() - 1, index);

        return values.get(safeIndex);
    }
}
