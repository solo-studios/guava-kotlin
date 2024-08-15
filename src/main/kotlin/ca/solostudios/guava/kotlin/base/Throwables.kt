/*
 * Copyright (c) 2022-2024 solonovamax <solonovamax@12oclockpoint.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.solostudios.guava.kotlin.base

import com.google.common.base.Throwables
import java.io.IOException

/**
 * Throws [this] if it is not `null` and an instance of [X]. Example usage:
 * ```kotlin
 * var failure: Throwable? = null
 * for (foo: Foo in foos) {
 *     try {
 *         foo.bar()
 *     } catch(t: BarException) {
 *         failure = t
 *     } catch (t: RuntimeException) {
 *         failure = t
 *     } catch (t: Error) {
 *         failure = t
 *     }
 * }
 *
 * if (failure != null) {
 *     failure.throwIfInstanceOf<BarException>()
 *     throw AssertionError(failure)
 * }
 * ```
 *
 * @see Throwables.throwIfInstanceOf
 */
public inline fun <reified X : Throwable> Throwable.throwIfInstanceOf() {
    Throwables.throwIfInstanceOf(this, X::class.java)
}

/**
 * Throws [this] if it is a [RuntimeException] or [Error]. Example usage:
 * ```kotlin
 * var failure: Throwable? = null
 * for (foo: Foo in foos) {
 *     try {
 *         foo.bar()
 *     } catch (t: RuntimeException) {
 *         failure = t
 *     } catch (t: Error) {
 *         failure = t
 *     }
 * }
 *
 * if (failure != null) {
 *     failure.throwIfUnchecked()
 *     throw AssertionError(failure)
 * }
 * ```
 *
 * @see Throwables.throwIfUnchecked
 */
@Throws(RuntimeException::class, Error::class)
public fun Throwable.throwIfUnchecked() {
    return Throwables.throwIfUnchecked(this)
}

/**
 * Returns the innermost cause of `this`. The first throwable in a chain
 * provides context from when the error or exception was initially
 * detected. Example usage:
 * ```kotlin
 * assertEquals("Unable to assign a customer id", e.rootCause.message)
 * ```
 *
 * @throws IllegalArgumentException if there is a loop in the causal chain
 * @see Throwables.getRootCause
 */
public val Throwable.rootCause: Throwable
    get() = Throwables.getRootCause(this)

/**
 * Gets a [Throwable] cause chain as a list. The first entry in the list
 * will be `this` followed by its cause hierarchy. Note that this is a
 * snapshot of the cause chain and will not reflect any subsequent changes
 * to the cause chain.
 *
 * Here's an example of how it can be used to find specific types of
 * exceptions in the cause chain:
 * ```kotlin
 * e.causalChain.filterIsInstance<IOException>()
 * ```
 *
 * @return an unmodifiable list containing the cause chain starting with
 *         `this`
 * @receiver the non-null `Throwable` to extract causes from
 * @throws IllegalArgumentException if there is a loop in the causal chain
 * @see Throwables.getCausalChain
 */
@get:Throws(IllegalArgumentException::class)
public val Throwable.causalChain: List<Throwable>
    get() = Throwables.getCausalChain(this)

/**
 * Returns [this]'s cause, cast to `X`.
 *
 * Prefer this method instead of manually casting an exception's cause. For
 * example, `e.getCause() as IOException` throws a [ClassCastException]
 * that discards the original exception `e` if the cause is not an
 * [IOException], but `e.causeAs<IOException>()` keeps `e` as the
 * [ClassCastException]'s cause.
 *
 * @throws ClassCastException if the cause cannot be cast to the expected
 *         type. The [ClassCastException]'s cause is `this`.
 * @see Throwables.getCauseAs
 */
@Throws(ClassCastException::class)
public inline fun <reified X : Throwable> Throwable.causeAs(): X? {
    return Throwables.getCauseAs(this, X::class.java)
}

/**
 * Returns a string containing the result of
 * [toString()][Throwable.toString], followed by the full, recursive stack
 * trace of `this`. Note that you probably should not be parsing the
 * resulting string; if you need programmatic access to the stack frames,
 * you can call [Throwable.getStackTrace].
 *
 * @see Throwables.getStackTraceAsString
 */
public val Throwable.stackTraceAsString: String
    get() = Throwables.getStackTraceAsString(this)
