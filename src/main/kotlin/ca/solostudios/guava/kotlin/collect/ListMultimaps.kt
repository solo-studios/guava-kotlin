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

package ca.solostudios.guava.kotlin.collect

import com.google.common.collect.MultimapBuilder
import com.google.common.collect.Multimaps
import kotlin.experimental.ExperimentalTypeInference
import com.google.common.collect.ImmutableListMultimap as ImmutableGuavaListMultimap
import com.google.common.collect.ListMultimap as GuavaListMultimap
import com.google.common.collect.MultimapBuilder as GuavaMultimapBuilder

/**
 * Returns an empty read-only [ListMultimap].
 */
public fun <K : Any, V : Any> emptyListMultimap(): ListMultimap<K, V> = ImmutableGuavaListMultimap.of<K, V>().toKotlin()

/**
 * Returns a new read-only [ListMultimap] of given elements.
 */
public fun <K : Any, V : Any> listMultimapOf(vararg elements: Pair<K, V>): ListMultimap<K, V> {
    return if (elements.isNotEmpty())
        ImmutableGuavaListMultimap.builder<K, V>()
            .also {
                for ((key, value) in elements)
                    it.put(key, value)
            }
            .build()
            .toKotlin()
    else
        emptyListMultimap()
}

/**
 * Returns a new read-only [ListMultimap] of given elements.
 */
@JvmName("listMultimapOfCollection")
public fun <K : Any, V : Any> listMultimapOf(vararg elements: Pair<K, Collection<V>>): ListMultimap<K, V> {
    return if (elements.isNotEmpty())
        ImmutableGuavaListMultimap.builder<K, V>()
            .also {
                for ((key, value) in elements)
                    it.putAll(key, value)
            }
            .build()
            .toKotlin()
    else
        emptyListMultimap()
}

/**
 * Returns an empty read-only [ListMultimap].
 */
public fun <K : Any, V> listMultimapOf(): ListMultimap<K, V> = emptyListMultimap()

/**
 * Returns an empty new [MutableListMultimap].
 */
public fun <K, V> mutableListMultimapOf(): MutableListMultimap<K, V> {
    return MultimapBuilder.hashKeys()
        .arrayListValues()
        .build<K, V>()
        .toKotlin()
}

/**
 * Returns a new [MutableListMultimap] with the given elements.
 */
public fun <K, V> mutableListMultimapOf(vararg elements: Pair<K, V>): MutableListMultimap<K, V> {
    return GuavaMultimapBuilder.hashKeys()
        .arrayListValues()
        .build<K, V>()
        .also {
            for ((key, value) in elements)
                it.put(key, value)
        }
        .toKotlin()
}

/**
 * Returns a new [MutableListMultimap] with the given elements.
 */
@JvmName("mutableListMultimapOfCollection")
public fun <K, V> mutableListMultimapOf(vararg elements: Pair<K, Collection<V>>): MutableListMultimap<K, V> {
    return GuavaMultimapBuilder.hashKeys()
        .arrayListValues()
        .build<K, V>()
        .also {
            for ((key, value) in elements)
                it.putAll(key, value)
        }
        .toKotlin()
}

/**
 * Wraps an immutable guava list multimap into a [ListMultimap] instance.
 *
 * @return The wrapped guava list multimap.
 * @see GuavaListMultimap
 * @see MutableListMultimap
 * @see ListMultimap
 */
public fun <K : Any, V : Any> ImmutableGuavaListMultimap<K, V>.toKotlin(): ListMultimap<K, V> {
    return GuavaListMultimapWrapper(this)
}

/**
 * Wraps a guava list multimap into a [MutableListMultimap] instance.
 *
 * @return The wrapped guava list multimap.
 * @see GuavaListMultimap
 * @see MutableListMultimap
 * @see ListMultimap
 */
public fun <K, V> GuavaListMultimap<K, V>.toKotlin(): MutableListMultimap<K, V> {
    return MutableGuavaListMultimapWrapper(this)
}

/**
 * Transforms a [ListMultimap] into its immutable guava equivalent
 *
 * @return A copy of the backing guava list multimap.
 * @see ImmutableGuavaListMultimap
 * @see GuavaListMultimap
 * @see ListMultimap
 */
public fun <K, V> ListMultimap<K, V>.toGuava(): ImmutableGuavaListMultimap<K, V> {
    return when (this) {
        is GuavaListMultimapWrapper -> ImmutableGuavaListMultimap.copyOf(this.guavaMultimap)
        else -> ImmutableGuavaListMultimap.builder<K, V>().also {
            for (key in this.keys) {
                it.putAll(key!!, this[key])
            }
        }.build()
    }
}

/**
 * Transforms a [MutableListMultimap] into its guava equivalent
 *
 * @return A copy of the backing guava list multimap.
 * @see GuavaListMultimap
 * @see MutableListMultimap
 * @see ListMultimap
 */
public fun <K, V> MutableListMultimap<K, V>.toGuava(): GuavaListMultimap<K, V> {
    return when (this) {
        is MutableGuavaListMultimapWrapper -> GuavaMultimapBuilder.hashKeys()
            .arrayListValues()
            .build(this.guavaMultimap)

        else -> GuavaMultimapBuilder.hashKeys()
            .arrayListValues()
            .build<K, V>()
            .also {
                for (key in this.keys)
                    it.putAll(key, this[key])
            }
    }
}

/**
 * Returns a new [MutableListMultimap] filled with all elements of this
 * multimap.
 *
 * @see MultimapBuilder
 */
public fun <K, V> Multimap<K, V>.toMutableListMultimap(): MutableListMultimap<K, V> {
    return MultimapBuilder.hashKeys().arrayListValues().build(this.toGuava()).toKotlin()
}

/**
 * Returns a new [ListMultimap] filled with all elements of this multimap.
 *
 * @see ImmutableGuavaListMultimap.copyOf
 */
public fun <K, V> Multimap<K, V>.toListMultimap(): ListMultimap<K, V> {
    return ImmutableGuavaListMultimap.copyOf(this.toGuava()).toKotlin()
}

/**
 * Builds a new immutable list-multimap with the given [builderAction],
 * using the builder inference api.
 *
 * @param builderAction The builder action to apply to the list-multimap
 * @param K The key type of the list-multimap.
 * @param V The value type of the list-multimap.
 * @return The newly created list-multimap.
 * @see ImmutableGuavaListMultimap.Builder
 */
@OptIn(ExperimentalTypeInference::class)
public inline fun <K : Any, V : Any> buildListMultimap(
    @BuilderInference builderAction: ImmutableGuavaListMultimap.Builder<K, V>.() -> Unit,
): ListMultimap<K, V> {
    return ImmutableGuavaListMultimap.builder<K, V>()
        .apply(builderAction)
        .build()
        .toKotlin()
}

/**
 * Builds a new mutable list-multimap with the given [builderAction], using
 * the builder inference api.
 *
 * @param builderAction The builder action to apply to the list-multimap
 * @param K The key type of the list-multimap.
 * @param V The value type of the list-multimap.
 * @return The newly created list-multimap.
 * @see MultimapBuilder
 */
@OptIn(ExperimentalTypeInference::class)
public inline fun <K, V> buildMutableListMultimap(
    @BuilderInference builderAction: MutableListMultimap<K, V>.() -> Unit,
): MutableListMultimap<K, V> {
    return MultimapBuilder.hashKeys()
        .arrayListValues()
        .build<K, V>()
        .toKotlin()
        .apply(builderAction)
}

/**
 * A [Multimap] that can hold duplicate key-value pairs and that maintains
 * the insertion ordering of values for a given key.
 *
 * See the [Multimap] documentation for information common to all
 * multimaps.
 *
 * The [get] and [asMap] methods return a [List] of values.
 *
 * Methods in this interface support only read-only access to the multimap;
 * read-write access is supported through the [MutableListMultimap]
 * interface.
 *
 * See the Guava User Guide article on
 * [`Multimap`](https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap).
 *
 * @param K the type of multimap keys. The multimap is invariant in its key
 *         type, as it can accept key as a parameter (of [containsKey] for
 *         example) and return it in [keys] set.
 * @param V the type of multimap values. The multimap is covariant in its
 *         value type.
 * @see Multimap
 * @see GuavaListMultimap
 * @see ImmutableGuavaListMultimap
 */
public interface ListMultimap<K, out V> : Multimap<K, V> {
    // Query Operations
    /**
     * Returns a view of the list of all the values corresponding to the given
     * [key] in the multimap.
     *
     * When the multimap does not [contain][containsKey] the specified key,
     * this will return an empty collection.
     *
     * @see GuavaListMultimap.get
     */
    public override operator fun get(key: K): List<V>

    // Views
    /**
     * Returns a view of this multimap as a [Map] from each distinct key to
     * the nonempty collection of that key's associated values. Note that
     * `this.asMap().get(k)` is equivalent to `this.get(k)` only when `k` is a
     * key contained in the multimap; otherwise it returns `null` as opposed to
     * an empty collection.
     *
     * @see GuavaListMultimap.asMap
     */
    public override fun asMap(): Map<K, List<V>>
}

/**
 * A [Multimap] that can hold duplicate key-value pairs and that maintains
 * the insertion ordering of values for a given key.
 *
 * See the [Multimap] documentation for information common to all
 * multimaps.
 *
 * The [get], [asMap], and [removeAll] methods return a [List] of values.
 *
 * See the Guava User Guide article on
 * [`Multimap`](https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap).
 *
 * @param K the type of map keys. The map is invariant in its key type.
 * @param V the type of map values. The mutable map is invariant in its
 *         value type.
 * @see Multimap
 * @see GuavaListMultimap
 */
public interface MutableListMultimap<K, V> : ListMultimap<K, V>, MutableMultimap<K, V> {
    // Modification Operations
    /**
     * Removes all values associated with the key `key`.
     *
     * Once this method returns, `key` will not be mapped to any values, so it
     * will not appear in [keySet], [asMap], or any other views.
     *
     * @return the values that were removed (possibly empty). The returned list
     *         *may* be modifiable, but updating it will have no effect on the
     *         multimap.
     * @see GuavaListMultimap.removeAll
     */
    public override fun removeAll(key: K): List<V>

    // Views
    /**
     * Returns a mutable view of the list of all the values corresponding to
     * the given [key] in the multimap.
     *
     * When the multimap does not [contain][containsKey] the specified key,
     * this will return an empty collection.
     *
     * Changes to the underlying collection will update the underlying
     * multimap, and vice versa.
     *
     * @see GuavaListMultimap.get
     */
    public override operator fun get(key: K): MutableList<@UnsafeVariance V>
}

internal class GuavaListMultimapWrapper<K : Any, out V>(
    override val guavaMultimap: ImmutableGuavaListMultimap<K, @UnsafeVariance V>,
) : ListMultimap<K, V>,
    AbstractGuavaMultimapWrapper<K, V>() {
    override fun get(key: K): List<V> = guavaMultimap.get(key)

    @Suppress("UnstableApiUsage")
    override fun asMap(): Map<K, List<V>> = Multimaps.asMap(guavaMultimap)
}

internal class MutableGuavaListMultimapWrapper<K, V>(
    override val guavaMultimap: GuavaListMultimap<K, V>,
) : MutableListMultimap<K, V>,
    AbstractMutableGuavaMultimapWrapper<K, V>() {
    override fun get(key: K): MutableList<V> = guavaMultimap.get(key)

    @Suppress("UnstableApiUsage")
    override fun asMap(): Map<K, List<V>> = Multimaps.asMap(guavaMultimap)

    override fun removeAll(key: K): List<V> = guavaMultimap.removeAll(key)
}
