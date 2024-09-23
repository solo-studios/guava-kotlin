/*
 * Copyright (c) 2024 solonovamax <solonovamax@12oclockpoint.com>
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

import com.google.common.collect.HashBiMap
import kotlin.experimental.ExperimentalTypeInference
import com.google.common.collect.BiMap as GuavaBiMap
import com.google.common.collect.ImmutableBiMap as ImmutableGuavaBiMap

/**
 * Returns an empty read-only [BiMap].
 */
public fun <K, V> emptyBiMap(): BiMap<K, V> = ImmutableGuavaBiMap.of<K, V>().toKotlin()

/**
 * Returns a new read-only [BiMap] of given elements.
 */
public fun <K : Any, V : Any> bimapOf(vararg elements: Pair<K, V>): BiMap<K, V> {
    return if (elements.isNotEmpty())
        ImmutableGuavaBiMap.builder<K, V>()
            .also {
                for ((key, value) in elements)
                    it.put(key, value)
            }
            .build()
            .toKotlin()
    else
        emptyBiMap()
}

/**
 * Returns an empty read-only [BiMap].
 */
public fun <K : Any, V> bimapOf(): BiMap<K, V> = emptyBiMap()

/**
 * Returns an empty new [BiMap].
 */
public fun <K, V> mutableBiMapOf(): MutableBiMap<K, V> {
    return HashBiMap.create<K, V>().toKotlin()
}

/**
 * Returns a new [MutableBiMap] with the given elements.
 */
@JvmName("mutableBiMapOfCollection")
public fun <K, V> mutableBiMapOf(vararg elements: Pair<K, V>): MutableBiMap<K, V> {
    return HashBiMap.create<K, V>().also { it.putAll(elements) }.toKotlin()
}

/**
 * Wraps an immutable guava bimap into a [BiMap] instance.
 *
 * @return The wrapped guava bimap.
 * @see GuavaBiMap
 * @see MutableBiMap
 * @see BiMap
 */
public fun <K, V> ImmutableGuavaBiMap<K, V>.toKotlin(): BiMap<K, V> {
    return GuavaBiMapWrapper(this)
}

/**
 * Wraps a guava bimap into a [MutableBiMap] instance.
 *
 * @return The wrapped guava bimap.
 * @see GuavaBiMap
 * @see MutableBiMap
 * @see BiMap
 */
public fun <K, V> GuavaBiMap<K, V>.toKotlin(): MutableBiMap<K, V> {
    return MutableGuavaBiMapWrapper(this)
}

/**
 * Transforms a [BiMap] into its immutable guava equivalent
 *
 * @return A copy of the backing guava bimap.
 * @see ImmutableGuavaBiMap
 * @see GuavaBiMap
 * @see BiMap
 */
public fun <K, V> BiMap<K, V>.toGuava(): ImmutableGuavaBiMap<K, V> {
    return ImmutableGuavaBiMap.copyOf(this)
}

/**
 * Transforms a [MutableBiMap] into its guava equivalent
 *
 * @return A copy of the backing guava bimap.
 * @see GuavaBiMap
 * @see BiMap
 * @see MutableBiMap
 */
public fun <K, V> MutableBiMap<K, V>.toGuava(): GuavaBiMap<K, V> {
    return HashBiMap.create(this)
}

/**
 * Returns a new [MutableBiMap] filled with all elements of this bimap.
 */
public fun <K, V> BiMap<K, V>.toMutableBiMap(): MutableBiMap<K, V> {
    return HashBiMap.create(this).toKotlin()
}

/**
 * Returns a new [BiMap] filled with all elements of this bimap.
 *
 * @see ImmutableGuavaBiMap.copyOf
 */
public fun <K, V> BiMap<K, V>.toBiMap(): BiMap<K, V> {
    return ImmutableGuavaBiMap.copyOf(this).toKotlin()
}

/**
 * Builds a new immutable bimap with the given [builderAction], using the
 * builder inference api.
 *
 * @param builderAction The builder action to apply to the bimap
 * @param K The key type of the bimap.
 * @param V The value type of the bimap.
 * @return The newly created bimap.
 * @see ImmutableGuavaBiMap.Builder
 */
@OptIn(ExperimentalTypeInference::class)
public inline fun <K : Any, V : Any> buildBiMap(
    @BuilderInference builderAction: ImmutableGuavaBiMap.Builder<K, V>.() -> Unit,
): BiMap<K, V> {
    return ImmutableGuavaBiMap.builder<K, V>()
        .apply(builderAction)
        .build()
        .toKotlin()
}

/**
 * Builds a new mutable bimap with the given [builderAction], using the
 * builder inference api.
 *
 * @param builderAction The builder action to apply to the bimap
 * @param K The key type of the bimap.
 * @param V The value type of the bimap.
 * @return The newly created bimap.
 */
@OptIn(ExperimentalTypeInference::class)
public inline fun <K, V> buildMutableBiMap(
    @BuilderInference builderAction: MutableBiMap<K, V>.() -> Unit,
): MutableBiMap<K, V> {
    return HashBiMap.create<K, V>()
        .toKotlin()
        .apply(builderAction)
}

/**
 * A bimap (or "bidirectional map") is a map that preserves the uniqueness
 * of its values as well as that of its keys. This constraint enables
 * bimaps to support an "inverse view", which is another bimap containing
 * the same entries as this bimap but with reversed keys and values.
 *
 * Methods in this interface support only read-only access to the bimap;
 * read-write access is supported through the [MutableBiMap] interface.
 *
 * See the Guava User Guide article on
 * [`BiMap`](https://github.com/google/guava/wiki/NewCollectionTypesExplained#bimap).
 *
 * @param K the type of bimap keys. The bimap is invariant in its key type,
 *   as it can accept key as a parameter (of [containsKey] for example) and
 *   return it in [keys] set.
 * @param V the type of bimap values. The bimap is invariant in its value
 *   type.
 * @see GuavaBiMap
 * @see ImmutableGuavaBiMap
 */
public interface BiMap<K, V> : Map<K, V> {

    // Views
    /**
     * Returns a read-only [Collection] of all values in this map.
     *
     * Because a bimap has unique values, this method returns a [Set], instead
     * of the [Collection] specified in the [Map] interface.
     */
    public override val values: Set<V>

    /**
     * Returns the inverse view of this bimap, which maps each of this bimap's
     * values to its associated key. The two bimaps are backed by the same
     * data; any changes to one will appear in the other.
     *
     * **Note:**There is no guaranteed correspondence between the iteration
     * order of a bimap and that of its inverse.
     */
    public val inverse: BiMap<V, K>
}

/**
 * A bimap (or "bidirectional map") is a map that preserves the uniqueness
 * of its values as well as that of its keys. This constraint enables
 * bimaps to support an "inverse view", which is another bimap containing
 * the same entries as this bimap but with reversed keys and values.
 *
 * See the Guava User Guide article on
 * [`BiMap`](https://github.com/google/guava/wiki/NewCollectionTypesExplained#bimap).
 *
 * @param K the type of map keys. The map is invariant in its key type.
 * @param V the type of map values. The mutable map is invariant in its
 *   value type.
 * @see BiMap
 * @see GuavaBiMap
 */
public interface MutableBiMap<K, V> : BiMap<K, V>, MutableMap<K, V> {
    /**
     * Associates the specified [value] with the specified [key] in the map.
     *
     * @return the previous value associated with the key, or `null` if the key
     *   was not present in the map.
     * @throws IllegalArgumentException if the given value is already bound to
     *   a different key in this bimap. The bimap will remain unmodified in
     *   this event. To avoid this exception, use [forcePut] instead.
     * @see GuavaBiMap.put
     */
    override fun put(key: K, value: V): V?

    /**
     * An alternate form of `put` that silently removes any existing entry with
     * the value `value` before proceeding with the [.put] operation. If the
     * bimap previously contained the provided key-value mapping, this method
     * has no effect.
     *
     * Note that a successful call to this method could cause the size of the
     * bimap to increase by one, stay the same, or even decrease by one.
     *
     * **Warning:** If an existing entry with this value is removed, the key
     * for that entry is discarded and not returned.
     *
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the value that was previously associated with the key, or `null`
     *   if there was no previous entry. (If the bimap contains null values,
     *   then `forcePut`, like `put`, returns `null` both if the key is absent
     *   and if it is present with a null value.)
     * @see GuavaBiMap.forcePut
     */
    public fun forcePut(key: K, value: V): V?


    // Bulk Operations
    /**
     * Updates this map with key/value pairs from the specified map [from].
     *
     * **Warning:** the results of calling this method may vary depending on
     * the iteration order of `map`.
     *
     * @throws IllegalArgumentException if an attempt to `put` any entry fails.
     *   Note that some map entries may have been added to the bimap before the
     *   exception was thrown.
     * @see GuavaBiMap.putAll
     */
    override fun putAll(from: Map<out K, V>)

    /**
     * Returns a read-only [Collection] of all values in this map.
     *
     * Because a bimap has unique values, this method returns a [Set], instead
     * of the [Collection] specified in the [Map] interface.
     */
    public override val values: MutableSet<V>
}

internal open class GuavaBiMapWrapper<K, V>(
    internal open val guavaBiMap: GuavaBiMap<K, V>,
) : BiMap<K, V> {
    override val values: Set<V>
        get() = guavaBiMap.values
    override val inverse: BiMap<V, K>
        get() = guavaBiMap.inverse().toKotlin()
    override val entries: Set<Map.Entry<K, V>>
        get() = guavaBiMap.entries
    override val keys: Set<K>
        get() = guavaBiMap.keys
    override val size: Int
        get() = guavaBiMap.size

    override fun isEmpty() = guavaBiMap.isEmpty()

    override fun containsValue(value: V) = guavaBiMap.containsValue(value)

    override fun containsKey(key: K) = guavaBiMap.containsKey(key)

    override fun equals(other: Any?) = when (other) {
        is GuavaBiMapWrapper<*, *> -> guavaBiMap == other.guavaBiMap
        else -> guavaBiMap == other
    }

    override fun get(key: K) = guavaBiMap[key]

    override fun hashCode() = guavaBiMap.hashCode()

    override fun toString(): String = guavaBiMap.toString()

    override fun getOrDefault(key: K, defaultValue: V): V = guavaBiMap.getOrDefault(key, defaultValue)
}

internal class MutableGuavaBiMapWrapper<K, V>(
    guavaBiMap: GuavaBiMap<K, V>,
) : MutableBiMap<K, V>, GuavaBiMapWrapper<K, V>(guavaBiMap) {
    override fun put(key: K, value: V) = guavaBiMap.put(key, value)

    override fun forcePut(key: K, value: V) = guavaBiMap.forcePut(key, value)

    override fun putAll(from: Map<out K, V>) = guavaBiMap.putAll(from)

    override val values: MutableSet<V>
        get() = guavaBiMap.values
    override val inverse: MutableBiMap<V, K>
        get() = guavaBiMap.inverse().toKotlin()
    override val size: Int
        get() = guavaBiMap.size
    override val keys: MutableSet<K>
        get() = guavaBiMap.keys
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = guavaBiMap.entries

    override fun get(key: K) = guavaBiMap[key]

    override fun isEmpty() = guavaBiMap.isEmpty()

    override fun clear() = guavaBiMap.clear()

    override fun remove(key: K): V? = guavaBiMap.remove(key)

    override fun containsKey(key: K) = guavaBiMap.containsKey(key)

    override fun containsValue(value: V) = guavaBiMap.containsValue(value)
}
