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

package ca.solostudios.guava.kotlin.collect

import ca.solostudios.guava.kotlin.annotations.ExperimentalCollectionsApi
import com.google.common.collect.HashMultimap
import com.google.common.collect.MultimapBuilder
import com.google.common.collect.Multimaps
import com.google.common.collect.ImmutableSetMultimap as ImmutableGuavaSetMultimap
import com.google.common.collect.SetMultimap as GuavaSetMultimap

/**
 * Returns an empty read-only multimap.
 */
public inline fun <K, V> emptySetMultimap(): SetMultimap<K, V> = ImmutableGuavaSetMultimap.of<K, V>().toKotlin()

/**
 * Returns a new read-only multimap of given elements.
 */
public inline fun <K, V> setMultimapOf(vararg elements: Pair<K, V>): SetMultimap<K, V> {
    return if (elements.isNotEmpty())
        ImmutableGuavaSetMultimap.builder<K, V>()
                .also {
                    for ((key, value) in elements)
                        it.put(key, value)
                }
                .build()
                .toKotlin()
    else
        emptySetMultimap()
}

/**
 * Returns a new read-only multimap of given elements.
 */
@JvmName("listMultimapOfCollection")
public inline fun <K, V> setMultimapOf(vararg elements: Pair<K, Collection<V>>): SetMultimap<K, V> {
    return if (elements.isNotEmpty())
        ImmutableGuavaSetMultimap.builder<K, V>()
                .also {
                    for ((key, value) in elements)
                        it.putAll(key, value)
                }
                .build()
                .toKotlin()
    else
        emptySetMultimap()
}

/**
 * Returns an empty read-only list.
 */
public inline fun <K, V> setMultimapOf(): SetMultimap<K, V> = emptySetMultimap()

/**
 * Returns an empty new [MutableList].
 */
public inline fun <K, V> mutableSetMultimapOf(): SetMultimap<K, V> = HashMultimap.create<K, V>().toKotlin()

/**
 * Returns a new [MutableList] with the given elements.
 */
public inline fun <K, V> mutableSetMultimapOf(vararg elements: Pair<K, V>): SetMultimap<K, V> {
    return MultimapBuilder.hashKeys()
            .hashSetValues()
            .build<K, V>()
            .also {
                for ((key, value) in elements)
                    it.put(key, value)
            }
            .toKotlin()
}

/**
 * Returns a new [MutableList] with the given elements.
 */
@JvmName("mutableSetMultimapOfCollection")
public inline fun <K, V> mutableSetMultimapOf(vararg elements: Pair<K, Collection<V>>): MutableSetMultimap<K, V> {
    return MultimapBuilder.hashKeys()
            .hashSetValues()
            .build<K, V>()
            .also {
                for ((key, value) in elements)
                    it.putAll(key, value)
            }
            .toKotlin()
}

public fun <K, V> ImmutableGuavaSetMultimap<K, V>.toKotlin(): SetMultimap<K, V> {
    return GuavaSetMultimapWrapper(this)
}

public fun <K, V> GuavaSetMultimap<K, V>.toKotlin(): MutableSetMultimap<K, V> {
    return MutableGuavaSetMultimapWrapper(this)
}

@ExperimentalCollectionsApi
@Suppress("UnstableApiUsage")
public fun <K, V> SetMultimap<K, V>.toGuava(): ImmutableGuavaSetMultimap<K, V> {
    return when (this) {
        is GuavaSetMultimapWrapper -> ImmutableGuavaSetMultimap.copyOf(this.guavaMultimap)
        else                       -> ImmutableGuavaSetMultimap.copyOf(this.entries)
    }
}

public fun <K, V> MutableSetMultimap<K, V>.toGuava(): GuavaSetMultimap<K, V> {
    return when (this) {
        is MutableGuavaSetMultimapWrapper -> MultimapBuilder.hashKeys().hashSetValues().build(this.guavaMultimap)
        
        else                              -> {
            MultimapBuilder.hashKeys()
                    .hashSetValues()
                    .build<K, V>()
                    .also {
                        for (key in this.keys)
                            it.putAll(key, this[key])
                    }
        }
    }
}

/**
 * A [Multimap] that cannot hold duplicate key-value pairs and that maintains the insertion
 * ordering of values for a given key.
 *
 * See the [Multimap] documentation for information common to all multimaps.
 *
 * The [get] and [asMap] methods return a [Set] of values.
 *
 * Methods in this interface support only read-only access to the multimap;
 * read-write access is supported through the [MutableListMultimap] interface.
 *
 * See the Guava User Guide article on [`Multimap`](https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap).
 *
 * @param K the type of multimap keys. The multimap is invariant in its key type, as it
 *          can accept key as a parameter (of [containsKey] for example) and return it in [keys] set.
 * @param V the type of multimap values. The multimap is covariant in its value type.
 *
 * @see Multimap
 * @see GuavaSetMultimap
 * @see ImmutableGuavaSetMultimap
 */
public interface SetMultimap<K, out V> : Multimap<K, V> {
    // Query Operations
    /**
     * Returns a view of the set of all the values corresponding to the given [key] in the multimap.
     *
     * When the multimap does not [contain][containsKey] the specified key, this will return an empty collection.
     *
     * @see GuavaSetMultimap.get
     */
    public override operator fun get(key: K): Set<V>
    
    // Views
    /**
     * Returns a view of this multimap as a [Map] from each distinct key to the nonempty
     * collection of that key's associated values. Note that `this.asMap().get(k)` is equivalent
     * to `this.get(k)` only when `k` is a key contained in the multimap; otherwise it
     * returns `null` as opposed to an empty collection.
     *
     * @see GuavaSetMultimap.asMap
     */
    public override fun asMap(): Map<K, Set<V>>
}

/**
 * A [Multimap] that cannot hold duplicate key-value pairs and that maintains the insertion
 * ordering of values for a given key.
 *
 * See the [Multimap] documentation for information common to all multimaps.
 *
 * The [get], [asMap], and [removeAll] methods return a [Set] of values.
 *
 * See the Guava User Guide article on [`Multimap`](https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap).
 *
 * @param K the type of map keys. The map is invariant in its key type.
 * @param V the type of map values. The mutable map is invariant in its value type.
 *
 * @see Multimap
 * @see GuavaSetMultimap
 */
public interface MutableSetMultimap<K, V> : SetMultimap<K, V>, MutableMultimap<K, V> {
    // Modification Operations
    /**
     * Removes all values associated with the key `key`.
     *
     * Once this method returns, `key` will not be mapped to any values, so it will not
     * appear in [keySet], [asMap], or any other views.
     *
     * @return the values that were removed (possibly empty). The returned set *may* be modifiable,
     * but updating it will have no effect on the multimap.
     *
     * @see GuavaSetMultimap.removeAll
     */
    public override fun removeAll(key: K): Set<V>
    
    // Views
    /**
     * Returns a mutable view of the set of all the values corresponding to the given [key] in the multimap.
     *
     * When the multimap does not [contain][containsKey] the specified key, this will return an empty collection.
     *
     * Changes to the underlying collection will update the underlying multimap, and vice versa.
     *
     * @see GuavaSetMultimap.get
     */
    public override operator fun get(key: K): MutableSet<@UnsafeVariance V>
}

internal class GuavaSetMultimapWrapper<K, out V>(
    override val guavaMultimap: ImmutableGuavaSetMultimap<K, @UnsafeVariance V>
                                                ) : SetMultimap<K, V>,
                                                    AbstractGuavaMultimapWrapper<K, V>() {
    override fun get(key: K): Set<@UnsafeVariance V> = guavaMultimap.get(key)
    
    @Suppress("UnstableApiUsage")
    override fun asMap(): Map<K, Set<V>> = Multimaps.asMap(guavaMultimap)
}

internal class MutableGuavaSetMultimapWrapper<K, V>(
    override val guavaMultimap: GuavaSetMultimap<K, V>
                                                   ) : MutableSetMultimap<K, V>,
                                                       AbstractMutableGuavaMultimapWrapper<K, V>() {
    override fun get(key: K): MutableSet<V> = guavaMultimap.get(key)
    
    @Suppress("UnstableApiUsage")
    override fun asMap(): Map<K, Set<V>> = Multimaps.asMap(guavaMultimap)
    
    override fun removeAll(key: K): Set<V> = guavaMultimap.removeAll(key)
}