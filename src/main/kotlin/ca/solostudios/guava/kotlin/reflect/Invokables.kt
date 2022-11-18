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

package ca.solostudios.guava.kotlin.reflect

import ca.solostudios.guava.kotlin.annotations.ExperimentalGuavaApi
import com.google.common.reflect.Invokable
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaConstructor
import kotlin.reflect.jvm.javaMethod

/**
 * Returns [Invokable] of [this].
 *
 * @see Invokable.from
 */
@ExperimentalGuavaApi
@Suppress("UnstableApiUsage")
public fun Method.asInvokable(): Invokable<*, Any?> {
    return Invokable.from(this)
}

/**
 * Returns [Invokable] of [this].
 *
 * @see Invokable.from
 */
@ExperimentalGuavaApi
@Suppress("UnstableApiUsage")
public fun <T> Constructor<T>.asInvokable(): Invokable<T, T> {
    return Invokable.from(this)
}

/**
 * Returns [Invokable] of [this].
 *
 * @throws NullPointerException if the [javaMethod] and [javaConstructor] are `null`.
 *
 * @see Invokable.from
 * @see KFunction.javaMethod
 * @see KFunction.javaConstructor
 */
@ExperimentalGuavaApi
@Suppress("UnstableApiUsage")
@Throws(NullPointerException::class)
public fun KFunction<*>.asInvokable(): Invokable<*, Any?> {
    when {
        this.javaMethod != null      -> Invokable.from(this.javaMethod!!)
        this.javaConstructor != null -> Invokable.from(this.javaConstructor!!)
        else                         -> throw NullPointerException("Both javaMethod and javaConstructor are null.")
    }
    return Invokable.from(this.javaMethod!!)
}
