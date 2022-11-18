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

import com.google.common.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.reflect.KClass

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
