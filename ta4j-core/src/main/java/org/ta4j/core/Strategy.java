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

/**
 * A trading strategy.
 *
 * A strategy is a pair of complementary {@link Rule rules}. It may recommend to
 * enter or to exit. Recommendations are based respectively on the entry rule or
 * on the exit rule.
 */
public interface Strategy {

    /**
     * @return the name of the strategy
     */
    String getName();

    /***
     * 
     * @return the description of the strategy
     */
    String getDescription();

    /**
     * @return the entry rule
     */
    Rule getEntryRule();

    /**
     * @return the exit rule
     */
    Rule getExitRule();

    /**
     * @param strategy the other strategy
     * @return the AND combination of two {@link Strategy strategies}
     */
    Strategy and(Strategy strategy);

    /**
     * @param strategy the other strategy
     * @return the OR combination of two {@link Strategy strategies}
     */
    Strategy or(Strategy strategy);

    /**
     * @param name           the name of the strategy
     * @param strategy       the other strategy
     * @param unstablePeriod number of bars that will be strip off for this strategy
     * @return the AND combination of two {@link Strategy strategies}
     */
    Strategy and(String name, Strategy strategy, int unstablePeriod);

    /**
     * @param name           the name of the strategy
     * @param strategy       the other strategy
     * @param unstablePeriod number of bars that will be strip off for this strategy
     * @return the OR combination of two {@link Strategy strategies}
     */
    Strategy or(String name, Strategy strategy, int unstablePeriod);

    /**
     * @return the opposite of the {@link Strategy strategy}
     */
    Strategy opposite();

    /**
     * @param unstablePeriod number of bars that will be strip off for this strategy
     */
    void setUnstablePeriod(int unstablePeriod);

    /**
     * @return unstablePeriod number of bars that will be strip off for this
     *         strategy
     */
    int getUnstablePeriod();

    /**
     * @param index a bar index
     * @return true if this strategy is unstable at the provided index, false
     *         otherwise (stable)
     */
    boolean isUnstableAt(int index);

    /**
     * @param index         the bar index
     * @param tradingRecord the potentially needed trading history
     * @return true to recommend a trade, false otherwise (no recommendation)
     */
    default boolean shouldOperate(int index, TradingRecord tradingRecord) {
        Position position = tradingRecord.getCurrentPosition();
        if (position.isNew()) {
            return shouldEnter(index, tradingRecord);
        } else if (position.isOpened()) {
            return shouldExit(index, tradingRecord);
        }
        return false;
    }

    /**
     * @param index the bar index
     * @return true to recommend to enter, false otherwise
     */
    default boolean shouldEnter(int index) {
        return shouldEnter(index, null);
    }

    /**
     * @param index         the bar index
     * @param tradingRecord the potentially needed trading history
     * @return true to recommend to enter, false otherwise
     */
    default boolean shouldEnter(int index, TradingRecord tradingRecord) {
        return !isUnstableAt(index) && getEntryRule().isSatisfied(index, tradingRecord);
    }

    /**
     * @param index the bar index
     * @return true to recommend to exit, false otherwise
     */
    default boolean shouldExit(int index) {
        return shouldExit(index, null);
    }

    /**
     * @param index         the bar index
     * @param tradingRecord the potentially needed trading history
     * @return true to recommend to exit, false otherwise
     */
    default boolean shouldExit(int index, TradingRecord tradingRecord) {
        return !isUnstableAt(index) && getExitRule().isSatisfied(index, tradingRecord);
    }
}
