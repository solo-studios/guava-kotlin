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

package ca.solostudios.guava.kotlin.cache

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache

/**
 * Builds a cache, which either returns an already-loaded value for a given key or atomically
 * computes or retrieves it using the supplied [loader]. If another thread is currently
 * loading the value for this key, simply waits for that thread to finish and returns its loaded
 * value. Note that multiple threads can concurrently load values for distinct keys.
 *
 * This method does not alter the state of this [CacheBuilder] instance, so it can be
 * invoked again to create multiple independent caches.
 *
 * @param loader the cache loader used to obtain new values
 *
 * @return a cache having the requested features
 */
public inline fun <K, V, K1 : K, V1 : V> CacheBuilder<K, V>.build(noinline loader: (key: K1) -> V1): LoadingCache<K1, V1> {
    return this.build(CacheLoader.from(loader))
}
