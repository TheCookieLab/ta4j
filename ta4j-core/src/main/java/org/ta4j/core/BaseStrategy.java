/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base implementation of a {@link Strategy}.
 */
public class BaseStrategy implements Strategy {

    /**
     * The logger
     */
    protected final transient Logger log = LoggerFactory.getLogger(getClass());

    /**
     * The class name
     */
    private final String className = getClass().getSimpleName();

    /**
     * Name of the strategy
     */
    private String name;

    /**
     * Description of the strategy
     */
    private String description;

    /**
     * The entry rule
     */
    private Rule entryRule;

    /**
     * The exit rule
     */
    private Rule exitRule;

    /**
     * The unstable period (number of bars).<br>
     * During the unstable period of the strategy any trade placement will be
     * cancelled.<br>
     * I.e. no entry/exit signal will be fired before index == unstablePeriod.
     */
    private int unstablePeriod;

    /**
     * Constructor.
     *
     * @param entryRule the entry rule
     * @param exitRule  the exit rule
     */
    public BaseStrategy(Rule entryRule, Rule exitRule) {
        this(null, null, entryRule, exitRule, 0);
    }

    /**
     * Constructor.
     *
     * @param entryRule      the entry rule
     * @param exitRule       the exit rule
     * @param unstablePeriod strategy will ignore possible signals at
     *                       <code>index</code> < <code>unstablePeriod</code>
     */
    public BaseStrategy(Rule entryRule, Rule exitRule, int unstablePeriod) {
        this(null, null, entryRule, exitRule, unstablePeriod);
    }

    /***
     * 
     * @param name
     * @param entryRule
     * @param exitRule
     */
    public BaseStrategy(String name, Rule entryRule, Rule exitRule) {
        this(name, null, entryRule, exitRule);
    }

    /**
     * Constructor.
     *
     * @param name        the name of the strategy
     * @param description
     * @param entryRule   the entry rule
     * @param exitRule    the exit rule
     */
    public BaseStrategy(String name, String description, Rule entryRule, Rule exitRule) {
        this(name, description, entryRule, exitRule, 0);
    }

    /**
     * Constructor.
     *
     * @param name           the name of the strategy
     * @param description
     * @param entryRule      the entry rule
     * @param exitRule       the exit rule
     * @param unstablePeriod strategy will ignore possible signals at
     *                       <code>index</code> < <code>unstablePeriod</code>
     */
    public BaseStrategy(String name, String description, Rule entryRule, Rule exitRule, int unstablePeriod) {
        if (entryRule == null || exitRule == null) {
            throw new IllegalArgumentException("Rules cannot be null");
        }
        if (unstablePeriod < 0) {
            throw new IllegalArgumentException("Unstable period bar count must be >= 0");
        }
        this.name = name;
        this.description = description;
        this.entryRule = entryRule;
        this.exitRule = exitRule;
        this.unstablePeriod = unstablePeriod;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Rule getEntryRule() {
        return entryRule;
    }

    @Override
    public Rule getExitRule() {
        return exitRule;
    }

    @Override
    public int getUnstablePeriod() {
        return unstablePeriod;
    }

    @Override
    public void setUnstablePeriod(int unstablePeriod) {
        this.unstablePeriod = unstablePeriod;
    }

    @Override
    public boolean isUnstableAt(int index) {
        return index < unstablePeriod;
    }

    @Override
    public boolean shouldEnter(int index, TradingRecord tradingRecord) {
        boolean enter = Strategy.super.shouldEnter(index, tradingRecord);
        traceShouldEnter(index, enter);
        return enter;
    }

    @Override
    public boolean shouldExit(int index, TradingRecord tradingRecord) {
        boolean exit = Strategy.super.shouldExit(index, tradingRecord);
        traceShouldExit(index, exit);
        return exit;
    }

    @Override
    public Strategy and(Strategy strategy) {
        String andName = "and(" + name + "," + strategy.getName() + ")";
        int unstable = Math.max(unstablePeriod, strategy.getUnstablePeriod());
        return and(andName, strategy, unstable);
    }

    @Override
    public Strategy or(Strategy strategy) {
        String orName = "or(" + name + "," + strategy.getName() + ")";
        int unstable = Math.max(unstablePeriod, strategy.getUnstablePeriod());
        return or(orName, strategy, unstable);
    }

    @Override
    public Strategy opposite() {
        return new BaseStrategy("opposite(" + name + ")", null, exitRule, entryRule, unstablePeriod);
    }

    @Override
    public Strategy and(String name, Strategy strategy, int unstablePeriod) {
        return new BaseStrategy(name, null, entryRule.and(strategy.getEntryRule()),
                exitRule.and(strategy.getExitRule()), unstablePeriod);
    }

    @Override
    public Strategy or(String name, Strategy strategy, int unstablePeriod) {
        return new BaseStrategy(name, null, entryRule.or(strategy.getEntryRule()), exitRule.or(strategy.getExitRule()),
                unstablePeriod);
    }

    /**
     * Traces the shouldEnter() method calls.
     *
     * @param index the bar index
     * @param enter true if the strategy should enter, false otherwise
     */
    protected void traceShouldEnter(int index, boolean enter) {
        if (log.isTraceEnabled()) {
            log.trace(">>> {}#shouldEnter({}): {}", className, index, enter);
        }
    }

    /**
     * Traces the shouldExit() method calls.
     *
     * @param index the bar index
     * @param exit  true if the strategy should exit, false otherwise
     */
    protected void traceShouldExit(int index, boolean exit) {
        if (log.isTraceEnabled()) {
            log.trace(">>> {}#shouldExit({}): {}", className, index, exit);
        }
    }
}
