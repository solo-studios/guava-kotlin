/*
 * Copyright (c) 2022 solonovamax <solonovamax@12oclockpoint.com>
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

@file:Suppress("NOTHING_TO_INLINE")

package ca.solostudios.guava.kotlin.base

import com.google.common.base.Throwables
import java.io.IOException

/**
 * Throws [this] if it is not `null` and an instance of [X]. Example usage:
 *
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
 * failure.throwIfInstanceOf<BarException>()
 * failure.throwIfUnchecked()
 * throw AssertionError(failure)
 * ```
 *
 * @see Throwables.throwIfInstanceOf
 */
public inline fun <reified X : Throwable> Throwable?.throwIfInstanceOf() {
    if (this != null && this is X)
        throw this
}

/**
 * Throws [this] if it is not `null` and a [RuntimeException] or [Error]. Example usage:
 *
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
 * failure.throwIfInstanceOf<BarException>()
 * failure.throwIfUnchecked
 * throw AssertionError(failure)
 * ```
 *
 * @see Throwables.throwIfUnchecked
 */
@Throws(RuntimeException::class, Error::class)
public fun Throwable?.throwIfUnchecked() {
    if (this == null)
        return
    
    if (this is RuntimeException)
        throw this
    
    if (this is Error)
        throw this
}

/**
 * Propagates [this] exactly as-is, if and only if it is an instance of [RuntimeException], [Error], or [X]. Example usage:
 *
 * ```kotlin
 * try {
 *     someMethodThatCouldThrowAnything()
 * } catch (e: IKnowWhatToDoWithThisException) {
 *     handle(e)
 * } catch (t: Throwable) {
 *     t.propagateIfPossible<OtherException>()
 *     throw RuntimeException("unexpected", t)
 * }
 * ```
 *
 * @see Throwables.propagateIfPossible
 * @see throwIfUnchecked
 */
public inline fun <reified X : Throwable> Throwable?.propagateIfPossible() {
    
    if (this == null)
        return
    
    if (this is X)
        throw this
    else
        this.throwIfUnchecked()
}

/**
 * Returns the innermost cause of [this]. The first throwable in a chain provides
 * context from when the error or exception was initially detected. Example usage:
 *
 * ```kotlin
 * assertEquals("Unable to assign a customer id", e.getRootCause().message)
 *```
 *
 * @throws IllegalArgumentException if there is a loop in the causal chain
 *
 * @see Throwables.getRootCause
 */
@Throws(IllegalArgumentException::class)
public inline fun Throwable.getRootCause(): Throwable {
    return Throwables.getRootCause(this)
}

/**
 * Gets a [Throwable] cause chain as a list. The first entry in the list will be `this`
 * followed by its cause hierarchy. Note that this is a snapshot of the cause chain and
 * will not reflect any subsequent changes to the cause chain.
 *
 *
 * Here's an example of how it can be used to find specific types of exceptions in the cause
 * chain:
 *
 * ```kotlin
 * e.causalChain.filterIsInstance<IOException>()
 * ```
 *
 * @receiver the non-null `Throwable` to extract causes from
 *
 * @return an unmodifiable list containing the cause chain starting with `this`
 *
 * @throws IllegalArgumentException if there is a loop in the causal chain
 *
 * @see Throwables.getCausalChain
 */
@get:Throws(IllegalArgumentException::class)
public val Throwable.causalChain: List<Throwable>
    get() = Throwables.getCausalChain(this)

/**
 * Returns [this]'s cause, cast to `X`.
 *
 * Prefer this method instead of manually casting an exception's cause. For example,
 * `(e as IOException).getCause()` throws a [ClassCastException] that discards the original
 * exception `e` if the cause is not an [IOException], but
 * `e.getCauseAs<IOException>()` keeps `e` as the [ClassCastException]'s cause.
 *
 * @throws ClassCastException if the cause cannot be cast to the expected type. The [ClassCastException]'s cause is [this].
 *
 * @see Throwables.getCauseAs
 */
@Throws(ClassCastException::class)
public inline fun <reified X : Throwable> Throwable.getCauseAs(
                                                              ): X? {
    return Throwables.getCauseAs(this, X::class.java)
}

/**
 * Returns a string containing the result of [toString()][Throwable.toString], followed by
 * the full, recursive stack trace of `this`. Note that you probably should not be
 * parsing the resulting string; if you need programmatic access to the stack frames, you can call
 * [Throwable.getStackTrace].
 *
 * @see Throwables.getStackTraceAsString
 */
public val Throwable.stackTraceAsString: String
    get() = Throwables.getStackTraceAsString(this)
