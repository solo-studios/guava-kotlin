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

import com.google.common.collect.ImmutableListMultimap
import com.google.common.collect.MultimapBuilder
import com.google.common.collect.Multimaps
import kotlin.collections.Map.Entry
import com.google.common.collect.ImmutableMultimap as ImmutableGuavaMultimap
import com.google.common.collect.Multimap as GuavaMultimap

/**
 * Wraps an immutable guava multimap into a [Multimap] instance.
 *
 * @return The wrapped guava multimap.
 *
 * @see GuavaMultimap
 * @see MutableMultimap
 * @see Multimap
 */
public fun <K, V> ImmutableGuavaMultimap<K, V>.toKotlin(): Multimap<K, V> {
    return GuavaMultimapWrapper(ImmutableListMultimap.copyOf(this))
}

/**
 * Wraps a guava multimap into a [MutableMultimap] instance.
 *
 * @return The wrapped guava multimap.
 *
 * @see GuavaMultimap
 * @see MutableMultimap
 * @see Multimap
 */
public fun <K, V> GuavaMultimap<K, V>.toKotlin(): MutableMultimap<K, V> {
    return MutableGuavaMultimapWrapper(this)
}

/**
 * Transforms a [Multimap] into its immutable guava equivalent
 *
 * @return A copy of the backing guava multimap.
 *
 * @see ImmutableGuavaMultimap
 * @see GuavaMultimap
 * @see Multimap
 */
public fun <K, V> Multimap<K, V>.toGuava(): ImmutableGuavaMultimap<K, V> {
    return when (this) {
        is AbstractGuavaMultimapWrapper -> ImmutableGuavaMultimap.copyOf(this.guavaMultimap)
        else                            -> ImmutableGuavaMultimap.builder<K, V>().also {
            for (key in this.keys) {
                it.putAll(key, this[key])
            }
        }.build()
    }
}

/**
 * Transforms a [MutableMultimap] into its guava equivalent
 *
 * @return A copy of the backing guava multimap.
 *
 * @see GuavaMultimap
 * @see Multimap
 * @see MutableMultimap
 */
public fun <K, V> MutableMultimap<K, V>.toGuava(): GuavaMultimap<K, V> {
    return when (this) {
        is AbstractMutableGuavaMultimapWrapper -> MultimapBuilder.hashKeys().arrayListValues().build(this.guavaMultimap)
        
        else                                   -> {
            MultimapBuilder.hashKeys()
                    .arrayListValues()
                    .build<K, V>()
                    .also {
                        for (key in this.keys)
                            it.putAll(key, this[key])
                    }
        }
    }
}

/**
 * Returns a new [MutableMultimap] filled with all elements of this multimap.
 *
 * @see MultimapBuilder
 */
public fun <K, V> Multimap<K, V>.toMutableMultimap(): MutableMultimap<K, V> {
    return MultimapBuilder.hashKeys().arrayListValues().build(this.toGuava()).toKotlin()
}

/**
 * Returns a new [Multiset] filled with all elements of this multimap.
 *
 * @see ImmutableGuavaMultimap.copyOf
 */
public fun <K, V> Multimap<K, V>.toMultimap(): Multimap<K, V> {
    return ImmutableGuavaMultimap.copyOf(this.toGuava()).toKotlin()
}

/**
 * The value collection type for a multimap.
 * Used by the [ListMultimap], and [SetMultimap] builder inference methods.
 *
 * @see buildMutableListMultimap
 * @see buildMutableSetMultimap
 * @see MultimapBuilder
 */
public enum class MultimapKeyType {
    /**
     * Uses a backing tree map to store keys.
     *
     * @see MultimapBuilder.treeKeys
     */
    TREE_KEYS,
    
    /**
     * Uses a backing hash map to store keys.
     *
     * @see MultimapBuilder.hashKeys
     */
    HASH_KEYS,
    
    /**
     * Uses a backing linked hash map to store keys.
     *
     * @see MultimapBuilder.linkedHashKeys
     */
    LINKED_HASH_KEYS
}

/**
 * A collection that holds pairs of objects (keys and values),
 * however each key may be associated with *multiple* values.
 *
 * Methods in this interface support only read-only access to the multimap;
 * read-write access is supported through the [MutableMultimap] interface.
 *
 * See the Guava User Guide article on [`Multimap`](https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap).
 *
 * @param K the type of multimap keys. The multimap is invariant in its key type, as it
 *          can accept key as a parameter (of [containsKey] for example) and return it in [keys] set.
 * @param V the type of multimap values. The multimap is covariant in its value type.
 *
 * @see GuavaMultimap
 * @see ImmutableGuavaMultimap
 * @see ListMultimap
 */
public interface Multimap<K, out V> : Iterable<Entry<K, V>> {
    // Query Operations
    /**
     * Returns the number of key/value pairs in the multimap.
     *
     *
     * **Note:** this method does not return the number of *distinct keys* in the multimap,
     * which is given by `keySet().size()` or `asMap().size()`. See the opening section of
     * the [Multimap] class documentation for clarification.
     *
     * @see GuavaMultimap.size
     */
    public val size: Int
    
    /**
     * Returns `true` if the multimap is empty (contains no elements), `false` otherwise.
     *
     * @see GuavaMultimap.isEmpty
     */
    public fun isEmpty(): Boolean
    
    /**
     * Returns `true` if the multimap contains the specified [key].
     *
     * @see GuavaMultimap.containsKey
     */
    public fun containsKey(key: K): Boolean
    
    /**
     * Returns `true` if the multimap maps one or more keys to the specified [value].
     *
     * @see GuavaMultimap.containsValue
     */
    public fun containsValue(value: @UnsafeVariance V): Boolean
    
    /**
     * Returns `true` if the multimap contains at least one key-value pair with the key `key` and the value `value`.
     *
     * @see GuavaMultimap.containsEntry
     */
    public fun containsEntry(entry: Pair<K, @UnsafeVariance V>): Boolean
    
    /**
     * Returns a view of the collection of all the values corresponding to the given [key] in the multimap.
     *
     * When the multimap does not [contain][containsKey] the specified key, this will return an empty collection.
     *
     * @see GuavaMultimap.get
     */
    public operator fun get(key: K): Collection<V>
    
    // Views
    /**
     * Returns a read-only [Set] of all the keys in this multimap.
     *
     * @see GuavaMultimap.keySet
     */
    public val keySet: Set<K>
    
    /**
     * Returns a read-only [Set] of all keys in this multimap.
     *
     * @see GuavaMultimap.keys
     */
    public val keys: Multiset<K>
    
    /**
     * Returns a read-only [Collection] of all values in this map. Note that this collection may contain duplicate values.
     *
     * @see GuavaMultimap.values
     */
    public val values: Collection<V>
    
    /**
     * Returns a read-only [Collection] of all key/value pairs in this map.
     *
     * @see GuavaMultimap.entries
     */
    public val entries: Collection<Entry<K, V>>
    
    /**
     * Returns a view of this multimap as a [Map] from each distinct key to the nonempty
     * collection of that key's associated values. Note that `this.asMap().get(k)` is equivalent
     * to `this.get(k)` only when `k` is a key contained in the multimap; otherwise it
     * returns `null` as opposed to an empty collection.
     *
     * @see GuavaMultimap.asMap
     */
    public fun asMap(): Map<K, Collection<V>>
    
    override fun iterator(): Iterator<Entry<K, V>> = entries.iterator()
}

/**
 * A collection that holds pairs of objects (keys and values),
 * however each key may be associated with *multiple* values.
 * Map keys are unique; the map holds only one value for each key.
 *
 * See the Guava User Guide article on [`Multimap`](https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap).
 *
 * @param K the type of map keys. The map is invariant in its key type.
 * @param V the type of map values. The mutable map is invariant in its value type.
 *
 * @see Multimap
 * @see GuavaMultimap
 * @see MutableListMultimap
 */
public interface MutableMultimap<K, V> : Multimap<K, V>, MutableIterable<Entry<K, V>> {
    // Modification Operations
    /**
     * Associates the specified [value] with the specified [key] in the map.
     *
     * @return `true` if the method increased the size of the multimap, or `false` if the
     * multimap already contained the key-value pair and doesn't allow duplicates
     *
     * @see GuavaMultimap.put
     */
    public fun put(key: K, value: V): Boolean
    
    /**
     * Removes a single key-value pair with the key [key] and the value [value] from this
     * multimap, if such exists. If multiple key-value pairs in the multimap fit this description,
     * which one is removed is unspecified.
     *
     * @return `true` if the multimap changed
     *
     * @see GuavaMultimap.remove
     */
    public fun remove(key: K, value: V): Boolean
    
    /**
     * Removes all values associated with the key `key`.
     *
     * Once this method returns, `key` will not be mapped to any values, so it will not
     * appear in [keySet], [asMap], or any other views.
     *
     * @return the values that were removed (possibly empty). The returned collection *may* be modifiable,
     * but updating it will have no effect on the multimap.
     *
     * @see GuavaMultimap.removeAll
     */
    public fun removeAll(key: K): Collection<V>
    
    // Bulk Modification Operations
    /**
     * Updates this map with key/value pairs from the specified map [from].
     *
     * @returns `true` if the multimap changed.
     *
     * @see GuavaMultimap.putAll
     */
    public fun putAll(from: Multimap<K, V>): Boolean
    
    /**
     * Updates this map with key/value pair specified by [key] and [values].
     *
     * @returns `true` if the multimap changed.
     *
     * @see GuavaMultimap.putAll
     */
    public fun putAll(key: K, values: Collection<V>): Boolean
    
    /**
     * Removes all elements from this map.
     *
     * @see GuavaMultimap.clear
     */
    public fun clear()
    
    // Views
    /**
     * Returns a mutable view of the collection of all the values corresponding to the given [key] in the multimap.
     *
     * When the multimap does not [contain][containsKey] the specified key, this will return an empty collection.
     *
     * Changes to the underlying collection will update the underlying multimap, and vice versa.
     *
     * @see GuavaMultimap.get
     */
    public override operator fun get(key: K): MutableCollection<@UnsafeVariance V>
    
    /**
     * Returns a read-write [Collection] of all key/value pairs in this map.
     *
     * Changes to the returned collection or the entries it contains will update the underlying
     * multimap, and vice versa. However, *adding* to the returned collection is not possible.
     *
     * @see GuavaMultimap.entries
     */
    public override val entries: MutableCollection<Entry<K, V>>
    
    override fun iterator(): MutableIterator<Entry<K, V>> = entries.iterator()
}

internal class GuavaMultimapWrapper<K, out V>(
    override val guavaMultimap: ImmutableGuavaMultimap<K, @UnsafeVariance V>
                                             ) : Multimap<K, V>,
                                                 AbstractGuavaMultimapWrapper<K, V>() {
    override fun get(key: K): Collection<V> = guavaMultimap.get(key)
    
    @Suppress("UnstableApiUsage")
    override fun asMap(): Map<K, Collection<V>> = Multimaps.asMap(guavaMultimap)
}

internal class MutableGuavaMultimapWrapper<K, V>(
    override val guavaMultimap: GuavaMultimap<K, V>
                                                ) : MutableMultimap<K, V>,
                                                    AbstractMutableGuavaMultimapWrapper<K, V>() {
    override fun get(key: K): MutableCollection<V> = guavaMultimap.get(key)
    
    @Suppress("UnstableApiUsage")
    override fun asMap(): Map<K, Collection<V>> = Multimaps.asMap(guavaMultimap)
    
    override fun removeAll(key: K): Collection<V> = guavaMultimap.removeAll(key)
}
