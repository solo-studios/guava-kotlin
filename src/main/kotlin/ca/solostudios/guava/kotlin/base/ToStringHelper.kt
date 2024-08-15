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

@file:Suppress("NOTHING_TO_INLINE")

package ca.solostudios.guava.kotlin.base

import com.google.common.base.MoreObjects
import com.google.common.base.MoreObjects.ToStringHelper

/**
 * Creates an instance of [ToStringHelper].
 *
 * This is helpful for implementing [Object.toString]. Specification by
 * example:
 * ```kotlin
 * // Returns "ClassName{}"
 * this.toStringHelper {
 * }
 *
 * // Returns "ClassName{x=1}"
 * this.toStringHelper {
 *     this["x"] = 1
 * }
 *
 * // Returns "ClassName{x=1, y=foo}"
 * this.toStringHelper {
 *     this["x"] = 1
 *     this["y"] = "foo"
 * }
 *
 * // Returns "ClassName{x=1}"
 * this.toStringHelper {
 *     omitNullValues()
 *
 *     this["x"] = 1
 *     this["y"] = null
 * }
 * ```
 *
 * @param builder The builder applied to [ToStringHelper]
 * @return Returns a string in the format specified by
 *         [MoreObjects.toStringHelper].
 * @receiver the object to generate the string for (typically `this`), used
 *         only for its class name
 * @see MoreObjects.toStringHelper
 */
public inline fun Any.toStringHelper(builder: (ToStringHelper).() -> Unit): String {
    return MoreObjects.toStringHelper(this).apply(builder).toString()
}

/**
 * Adds a name/value pair to the formatted output in `name=value` format.
 *
 * @see ToStringHelper.add
 */
public inline operator fun ToStringHelper.set(name: String, value: Any) {
    this.add(name, value)
}

/**
 * Adds a name/value pair to the formatted output in `name=value` format,
 * for the boolean primitive type.
 *
 * @see ToStringHelper.add
 */
public inline operator fun ToStringHelper.set(name: String, value: Boolean) {
    this.add(name, value)
}

/**
 * Adds a name/value pair to the formatted output in `name=value` format,
 * for the char primitive type.
 *
 * @see ToStringHelper.add
 */
public inline operator fun ToStringHelper.set(name: String, value: Char) {
    this.add(name, value)
}

/**
 * Adds a name/value pair to the formatted output in `name=value` format,
 * for the double primitive type.
 *
 * @see ToStringHelper.add
 */
public inline operator fun ToStringHelper.set(name: String, value: Double) {
    this.add(name, value)
}

/**
 * Adds a name/value pair to the formatted output in `name=value` format,
 * for the float primitive type.
 *
 * @see ToStringHelper.add
 */
public inline operator fun ToStringHelper.set(name: String, value: Float) {
    this.add(name, value)
}

/**
 * Adds a name/value pair to the formatted output in `name=value` format,
 * for the int primitive type.
 *
 * @see ToStringHelper.add
 */
public inline operator fun ToStringHelper.set(name: String, value: Int) {
    this.add(name, value)
}

/**
 * Adds a name/value pair to the formatted output in `name=value` format,
 * for the long primitive type.
 *
 * @see ToStringHelper.add
 */
public inline operator fun ToStringHelper.set(name: String, value: Long) {
    this.add(name, value)
}
