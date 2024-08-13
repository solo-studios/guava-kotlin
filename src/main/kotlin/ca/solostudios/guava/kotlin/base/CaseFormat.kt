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

import com.google.common.base.CaseFormat

/**
 * Converts the [CaseFormat] of a string.
 *
 * @param from The case format this string is currently in.
 * @param to The case format for this string to be formatted to.
 * @return The new string formatted in the case format of [to].
 */
public fun String.convert(from: CaseFormat, to: CaseFormat): String {
    return from.to(to, this)
}
