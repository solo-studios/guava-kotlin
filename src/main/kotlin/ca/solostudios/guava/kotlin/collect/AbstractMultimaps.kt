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

package ca.solostudios.guava.kotlin.collect

import java.util.Spliterator
import kotlin.collections.Map.Entry
import com.google.common.collect.Multimap as GuavaMultimap

internal abstract class AbstractGuavaMultimapWrapper<K, out V> : Multimap<K, V> {
    
    internal abstract val guavaMultimap: GuavaMultimap<K, @UnsafeVariance V>
    
    override fun get(key: K): Collection<@UnsafeVariance V> = guavaMultimap.get(key)
    
    override fun asMap(): Map<K, Collection<@UnsafeVariance V>> = guavaMultimap.asMap()
    
    override val size: Int
        get() = guavaMultimap.size()
    
    override fun isEmpty(): Boolean = guavaMultimap.isEmpty
    
    override fun containsEntry(entry: Pair<K, @UnsafeVariance V>): Boolean = guavaMultimap.containsEntry(entry.first, entry.second)
    
    override fun containsValue(value: @UnsafeVariance V): Boolean = guavaMultimap.containsValue(value)
    
    override fun containsKey(key: K): Boolean = guavaMultimap.containsKey(key)
    
    override val keySet: Set<K>
        get() = guavaMultimap.keySet()
    
    override val keys: Multiset<K>
        get() = guavaMultimap.keys().toKotlin()
    
    override val values: Collection<V>
        get() = guavaMultimap.values()
    
    override val entries: Collection<Entry<K, V>>
        get() = guavaMultimap.entries()
    
    override fun equals(other: Any?): Boolean = when (other) {
        is AbstractGuavaMultimapWrapper<*, *> -> guavaMultimap == other.guavaMultimap
        else                                  -> guavaMultimap == other
    }
    
    override fun hashCode(): Int = guavaMultimap.hashCode()
    
    override fun spliterator(): Spliterator<Entry<K, @UnsafeVariance V>> = guavaMultimap.entries().spliterator()
    
    override fun iterator(): Iterator<Entry<K, V>> = guavaMultimap.entries().iterator()
    
    override fun toString(): String = guavaMultimap.toString()
}

internal abstract class AbstractMutableGuavaMultimapWrapper<K, V> : MutableMultimap<K, V>, AbstractGuavaMultimapWrapper<K, V>() {
    abstract override val guavaMultimap: GuavaMultimap<K, V>
    
    override fun get(key: K): MutableCollection<V> = guavaMultimap.get(key)
    
    override fun clear() = guavaMultimap.clear()
    override val entries: MutableCollection<Entry<K, V>>
        get() = guavaMultimap.entries()
    
    override fun iterator(): MutableIterator<Entry<K, V>> = guavaMultimap.entries().iterator()
    
    override fun remove(key: K, value: V): Boolean = guavaMultimap.remove(key, value)
    
    override fun put(key: K, value: V): Boolean = guavaMultimap.put(key, value)
    
    override fun removeAll(key: K): Collection<V> = guavaMultimap.removeAll(key)
    
    override fun putAll(from: Multimap<K, V>): Boolean {
        var changed = false
        for (key in from.keys) {
            changed = putAll(key, from[key]) or changed
        }
        return changed
    }
    
    override fun putAll(key: K, values: Collection<V>) = guavaMultimap.putAll(key, values)
}
