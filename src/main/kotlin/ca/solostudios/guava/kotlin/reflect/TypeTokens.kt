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

package ca.solostudios.guava.kotlin.reflect

import com.google.common.reflect.TypeToken
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * Returns an instance of type token that wraps `type`.
 *
 * @see TypeToken.of
 */
public inline fun <reified T> typeTokenOf(): TypeToken<T> {
    return TypeToken.of(T::class.java)
}

/**
 * Returns an instance of type token that wraps `type`.
 *
 * @see TypeToken.of
 */
public fun <T> Class<T>.asTypeToken(): TypeToken<T> {
    return TypeToken.of(this)
}

/**
 * Returns an instance of type token that wraps `type`.
 *
 * @see TypeToken.of
 */
public fun <T : Any> KClass<T>.asTypeToken(): TypeToken<T> {
    return TypeToken.of(this.java)
}

/**
 * Returns an instance of type token that wraps `type`.
 *
 * @see TypeToken.of
 */
public fun Type.asTypeToken(): TypeToken<*> {
    return TypeToken.of(this)
}

/**
 * Returns the raw type of [T]. Formally speaking, if [T] is returned
 * by [Method.getGenericReturnType], the raw type is what's returned
 * by [Method.getReturnType] of the same method object. Specifically:
 * - If [T] is a [KClass] itself, [T] itself is returned.
 * - If [T] is a [ParameterizedType], the raw type of the parameterized
 *   type is returned.
 * - If [T] is a [GenericArrayType], the returned type is the corresponding
 *   array class. For example: `List<Integer>[] => List[]`.
 * - If [T] is a type variable or a wildcard type, the raw type of the
 *   first upper bound is returned. For example: `<X extends Foo> => Foo`.
 *
 * @see TypeToken.getRawType
 */
public val <T : Any> TypeToken<T>.rawKotlinType: KClass<in T>
    get() = this.rawType.kotlin
