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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;

/**
 * Cached {@link Indicator indicator}.Caches the constructor of the indicator.
 *
 * Avoid to calculate the same index of the indicator twice.
 *
 * @param <T>
 */
public abstract class CachedIndicator<T> extends AbstractIndicator<T> {

    /**
     * List of cached results
     */
    private final List<T> results;

    /**
     * Should always be the index of the last result in the results list. I.E. the
     * last calculated result.
     */
    protected int highestResultIndex = -1;

    /**
     * Constructor.
     *
     * @param series the related bar series
     */
    protected CachedIndicator(BarSeries series) {
        super(series);
        int limit = series.getMaximumBarCount();
        results = limit == Integer.MAX_VALUE ? new ArrayList<>() : new ArrayList<>(limit);
    }

    /**
     * Constructor.
     *
     * @param indicator a related indicator (with a bar series)
     */
    protected CachedIndicator(Indicator<?> indicator) {
        this(indicator.getBarSeries());
    }

    /**
     * @param index the bar index
     * @return the value of the indicator
     */
    protected abstract T calculate(int index);

    @Override
    public synchronized T getValue(int index) {
        BarSeries series = getBarSeries();

        // If the series is null, don't use cache and calculate the value directly
        if (series == null) {
            T result = calculate(index);
            if (log.isTraceEnabled()) {
                log.trace("{}({}): {}", this, index, result);
            }
            return result;
        }

        final int removedBarsCount = series.getRemovedBarsCount();
        final int maximumResultCount = series.getMaximumBarCount();

        T result;
        if (index < removedBarsCount) {
            // Result already removed from cache
            if (log.isTraceEnabled()) {
                log.trace("{}: result from bar {} already removed from cache, use {}-th instead",
                        getClass().getSimpleName(), index, removedBarsCount);
            }
            increaseLengthTo(removedBarsCount, maximumResultCount);
            highestResultIndex = removedBarsCount;
            result = results.get(0);
            if (result == null) {
                result = calculate(0);
                results.set(0, result);
            }
        } else {
            if (index == series.getEndIndex()) {
                // Don't cache result if last bar
                result = calculate(index);
            } else {
                increaseLengthTo(index, maximumResultCount);
                if (index > highestResultIndex) {
                    // Result not calculated yet
                    highestResultIndex = index;
                    result = calculate(index);
                    results.set(results.size() - 1, result);
                } else {
                    // Result covered by current cache
                    int resultInnerIndex = results.size() - 1 - (highestResultIndex - index);
                    result = results.get(resultInnerIndex);
                    if (result == null) {
                        result = calculate(index);
                        results.set(resultInnerIndex, result);
                    }
                }
            }

        }
        if (log.isTraceEnabled()) {
            log.trace("{}({}): {}", this, index, result);
        }
        return result;
    }

    /**
     * Increases the size of cached results buffer.
     *
     * @param index     the index to increase length to
     * @param maxLength the maximum length of the results buffer
     */
    private void increaseLengthTo(int index, int maxLength) {
        if (highestResultIndex > -1) {
            int newResultsCount = Math.min(index - highestResultIndex, maxLength);
            if (newResultsCount == maxLength) {
                results.clear();
                results.addAll(Collections.nCopies(maxLength, null));
            } else if (newResultsCount > 0) {
                results.addAll(Collections.nCopies(newResultsCount, null));
                removeExceedingResults(maxLength);
            }
        } else {
            // First use of cache
            assert results.isEmpty() : "Cache results list should be empty";
            results.addAll(Collections.nCopies(Math.min(index + 1, maxLength), null));
        }
    }

    /**
     * Removes the N first results which exceed the maximum bar count. (i.e. keeps
     * only the last maximumResultCount results)
     *
     * @param maximumResultCount the number of results to keep
     */
    private void removeExceedingResults(int maximumResultCount) {
        int resultCount = results.size();
        if (resultCount > maximumResultCount) {
            // Removing old results
            final int nbResultsToRemove = resultCount - maximumResultCount;
            if (nbResultsToRemove == 1) {
                results.remove(0);
            } else {
                results.subList(0, nbResultsToRemove).clear();
            }
        }
    }
}
