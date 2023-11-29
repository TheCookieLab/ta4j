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
package org.ta4j.core.rules;

import java.util.Arrays;
import java.util.LinkedList;

import org.ta4j.core.Rule;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.rules.helper.ChainLink;

/**
 * A chainrule has an initial rule that has to be satisfied before chain links
 * are evaluated. If the initial rule is satisfied every rule of chain link has
 * to be satisfied within a specified amount of bars (threshold).
 *
 */
public class ChainRule extends AbstractRule {
    private final Rule initialRule;
    LinkedList<ChainLink> rulesInChain = new LinkedList<>();

    /**
     * @param initialRule the first rule that has to be satisfied before
     *                    {@link ChainLink} are evaluated
     * @param chainLinks  {@link ChainLink} that has to be satisfied after the
     *                    inital rule within their thresholds
     */
    public ChainRule(Rule initialRule, ChainLink... chainLinks) {
        this.initialRule = initialRule;
        rulesInChain.addAll(Arrays.asList(chainLinks));
    }

    /** This rule uses the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        int lastRuleWasSatisfiedAfterBars = 0;
        int startIndex = index;

        if (!initialRule.isSatisfied(index, tradingRecord)) {
            traceIsSatisfied(index, false);
            return false;
        }
        traceIsSatisfied(index, true);

        for (ChainLink link : rulesInChain) {
            boolean satisfiedWithinThreshold = false;
            startIndex = startIndex - lastRuleWasSatisfiedAfterBars;
            lastRuleWasSatisfiedAfterBars = 0;

            for (int i = 0; i <= link.getThreshold(); i++) {
                int resultingIndex = startIndex - i;
                if (resultingIndex < 0) {
                    break;
                }

                satisfiedWithinThreshold = link.getRule().isSatisfied(resultingIndex, tradingRecord);

                if (satisfiedWithinThreshold == true) {
                    break;
                }

                lastRuleWasSatisfiedAfterBars++;
            }

            if (!satisfiedWithinThreshold) {
                traceIsSatisfied(index, false);
                return false;
            }
        }

        traceIsSatisfied(index, true);
        return true;
    }
}
