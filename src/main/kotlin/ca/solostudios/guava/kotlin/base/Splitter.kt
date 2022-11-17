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

package ca.solostudios.guava.kotlin.base

import ca.solostudios.guava.kotlin.annotations.ExperimentalGuavaApi
import com.google.common.base.Splitter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.stream.consumeAsFlow

/**
 * Splits [sequence] into string components and makes them available through a [Flow],
 * which may be lazily evaluated. If you want an eagerly computed [List], use
 * [Splitter.splitToList].
 *
 * @param sequence the sequence of characters to split
 *
 * @return a flow for the segments split from the parameter
 *
 * @see Splitter.splitToStream
 * @see consumeAsFlow
 */
@ExperimentalGuavaApi
@Suppress("UnstableApiUsage")
public fun Splitter.splitToFlow(sequence: CharSequence): Flow<String> {
    return this.splitToStream(sequence).consumeAsFlow()
}
