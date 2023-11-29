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
package org.ta4j.core.num;

import java.math.RoundingMode;
import java.util.function.Function;

/**
 * Representation of an undefined or unrepresentable value: NaN (not a number)
 * 
 * <p>
 * Special behavior in methods such as:
 * 
 * <ul>
 * <li>{@link NaN#plus(Num)} => NaN</li>
 * <li>{@link NaN#isEqual(Num)} => true</li>
 * <li>{@link NaN#isPositive()} => false</li>
 * <li>{@link NaN#isNegativeOrZero()} => false</li>
 * <li>{@link NaN#min(Num)} => NaN</li>
 * <li>{@link NaN#max(Num)} => NaN</li>
 * <li>{@link NaN#doubleValue()} => {@link Double#NaN}</li>
 * <li>{@link NaN#intValue()} => throws
 * {@link UnsupportedOperationException}</li>
 * </ul>
 */
public class NaN implements Num {

    /** A static Not-a-Number instance. */
    public static final Num NaN = new NaN();

    private NaN() {
    }

    /**
     * Returns a {@code Num} version of the given {@code Number}.
     *
     * <p>
     * <b>Warning:</b> This method returns {@link NaN} regardless of {@link val}.
     *
     * @param val the number
     * @return {@link #NaN}
     */
    public static Num valueOf(Number val) {
        return NaN;
    }

    @Override
    public int compareTo(Num o) {
        return 0;
    }

    @Override
    public int intValue() {
        throw new UnsupportedOperationException("No NaN represantation for int");
    }

    @Override
    public long longValue() {
        throw new UnsupportedOperationException("No NaN represantation for long");
    }

    @Override
    public float floatValue() {
        return Float.NaN;
    }

    @Override
    public double doubleValue() {
        return Double.NaN;
    }

    @Override
    public Number getDelegate() {
        return null;
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public String toString() {
        return "NaN";
    }

    @Override
    public Num plus(Num augend) {
        return this;
    }

    @Override
    public Num minus(Num subtrahend) {
        return this;
    }

    @Override
    public Num multipliedBy(Num multiplicand) {
        return this;
    }

    @Override
    public Num dividedBy(Num divisor) {
        return this;
    }

    @Override
    public Num remainder(Num divisor) {
        return this;
    }

    @Override
    public Num floor() {
        return this;
    }

    @Override
    public Num ceil() {
        return this;
    }

    @Override
    public Num pow(int n) {
        return this;
    }

    @Override
    public Num pow(Num n) {
        return this;
    }

    @Override
    public Num log() {
        return this;
    }

    @Override
    public Num sqrt() {
        return this;
    }

    @Override
    public Num sqrt(int precision) {
        return this;
    }

    @Override
    public Num abs() {
        return this;
    }

    @Override
    public Num negate() {
        return this;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isPositive() {
        return false;
    }

    @Override
    public boolean isPositiveOrZero() {
        return false;
    }

    @Override
    public boolean isNegative() {
        return false;
    }

    @Override
    public boolean isNegativeOrZero() {
        return false;
    }

    /**
     * <b>Warning:</b> This method returns {@code true} if {@code this} and
     * {@code obj} are both {@link #NaN}.
     *
     * @param other the other value, not null
     * @return false if both values are not {@link #NaN}; true otherwise.
     */
    @Override
    public boolean isEqual(Num other) {
        return other != null && other.equals(NaN);
    }

    @Override
    public boolean isGreaterThan(Num other) {
        return false;
    }

    @Override
    public boolean isGreaterThanOrEqual(Num other) {
        return false;
    }

    @Override
    public boolean isLessThan(Num other) {
        return false;
    }

    @Override
    public boolean isLessThanOrEqual(Num other) {
        return false;
    }

    @Override
    public Num min(Num other) {
        return this;
    }

    @Override
    public Num max(Num other) {
        return this;
    }

    @Override
    public Function<Number, Num> function() {
        return number -> NaN;
    }

    @Override
    public boolean isNaN() {
        return true;
    }

    @Override
    public Num round(int scale) {
        return this;
    }

    @Override
    public Num round(int scale, RoundingMode roundingMode) {
        return this;
    }
}
