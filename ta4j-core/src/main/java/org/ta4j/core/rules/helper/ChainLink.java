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
package org.ta4j.core.rules.helper;

import java.io.Serializable;
import java.util.Objects;

import org.ta4j.core.Rule;

/**
 * A ChainLink is part of a {@link org.ta4j.core.rules.ChainRule}. Every
 * Chainlink has a {@link Rule} and a threshold. ChainLinks are evaluated in the
 * trade they are added to the ChainRule and the rule has to be satisfied within
 * the threshold number of bars.
 */
public class ChainLink implements Serializable {

    private static final long serialVersionUID = -436033401669929601L;

    private Rule rule;
    private int threshold = 0;

    /**
     * Threshold is the number of bars the provided rule has to be satisfied after
     * the preceding rule
     *
     * @param rule      A {@link Rule} that has to be satisfied within the threshold
     * @param threshold Number of bars the rule has to be satisfied in. The current
     *                  index is included.
     */
    public ChainLink(Rule rule, int threshold) {
        this.rule = rule;
        this.threshold = threshold;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ChainLink))
            return false;
        ChainLink chainLink = (ChainLink) o;
        return getThreshold() == chainLink.getThreshold() && Objects.equals(getRule(), chainLink.getRule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRule(), getThreshold());
    }

    @Override
    public String toString() {
        return "ChainLink{" + "rule=" + rule + ", threshold=" + threshold + '}';
    }
}
