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

import ca.solostudios.guava.kotlin.annotations.ExperimentalCollectionsApi
import com.google.common.collect.HashMultiset
import com.google.common.collect.ImmutableMultiset
import com.google.common.collect.Iterables
import com.google.common.collect.Multisets
import java.util.Collections
import com.google.common.collect.ImmutableMultiset as ImmutableGuavaMultiset
import com.google.common.collect.Multiset as GuavaMultiset


public fun <E> ImmutableGuavaMultiset<E>.toKotlin(): Multiset<E> {
    return GuavaMultisetWrapper(this)
}

public fun <E> GuavaMultiset<E>.toKotlin(): MutableMultiset<E> {
    return MutableGuavaMultisetWrapper(this)
}

public fun <E> Multiset<E>.toGuava(): ImmutableGuavaMultiset<E> {
    return when (this) {
        is AbstractGuavaMultisetWrapper -> ImmutableGuavaMultiset.copyOf(this.guavaMultiset)
        else                            -> ImmutableGuavaMultiset.copyOf(this)
    }
}

public fun <E> MutableMultiset<E>.toGuava(): GuavaMultiset<E> {
    return when (this) {
        is AbstractMutableGuavaMultisetWrapper -> this.guavaMultiset
        else                                   -> HashMultiset.create(this)
    }
}

public fun <E> Collection<E>.toMutableMultiset(): MutableMultiset<E> {
    return HashMultiset.create(this).toKotlin()
}

public fun <E> Collection<E>.toMultiset(): Multiset<E> {
    return ImmutableMultiset.copyOf(this).toKotlin()
}

@ExperimentalCollectionsApi
@Suppress("UnstableApiUsage")
public operator fun <E> Multiset<E>.plus(other: Multiset<E>): Multiset<E> {
    return Multisets.sum(this.toGuava(), other.toGuava()).toKotlin()
}

@ExperimentalCollectionsApi
@Suppress("UnstableApiUsage")
public operator fun <E> Multiset<E>.minus(other: Multiset<E>): Multiset<E> {
    return Multisets.difference(this.toGuava(), other.toGuava()).toKotlin()
}

/**
 * A generic unordered collection that supports order-independent equality, like [Set], but may have duplicate
 * elements. A multiset is also sometimes called a *bag*.
 *
 * Methods in this interface support only read-only access to the set;
 * read/write access is supported through the [MutableMultiset] interface.
 *
 * See the Guava User Guide article on [`Multiset`](https://github.com/google/guava/wiki/NewCollectionTypesExplained#multiset).
 *
 * @param E the type of elements contained in the set. The set is covariant in its element type.
 *
 * @see GuavaMultiset
 * @see ImmutableGuavaMultiset
 */
public interface Multiset<out E> : Collection<E> {
    // Query Operations
    /**
     * Returns the number of elements in the multiset.
     *
     * **Note:** this method does not return the number of *distinct elements* in the
     * multiset, which is given by `entrySet().size()`.
     *
     * @see GuavaMultiset.size
     */
    public override val size: Int
    
    /**
     * Returns `true` if the multiset is empty (contains no elements), `false` otherwise.
     *
     * @see GuavaMultiset.isEmpty
     */
    public override fun isEmpty(): Boolean
    
    /**
     * Returns the number of occurrences of an element in this multiset (the *count* of the
     * element). Note that for an [Object.equals]-based multiset, this gives the same result as
     * [Collections.frequency] (which would presumably perform more poorly).
     *
     *
     * **Note:** the utility method [Iterables.frequency] generalizes this operation; it
     * correctly delegates to this method when dealing with a multiset, but it can also accept any
     * other iterable type.
     *
     * @param element the element to count occurrences of
     *
     * @return the number of occurrences of the element in this multiset; possibly zero but never
     * negative
     */
    public fun count(element: @UnsafeVariance E): Int
    
    // Views
    /**
     * Returns a read-only [Set] of all non-duplicate values in this map.
     *
     * @see GuavaMultiset.elementSet
     */
    public val elementSet: Set<E>
    
    /**
     * Returns a read-only [Collection] of all elements in this map, grouped into [Multiset.Entry][GuavaMultiset.Entry] instances,
     * each providing an element of the multiset and the count of that element.
     *
     * @see GuavaMultiset.entrySet
     */
    public val entrySet: Set<GuavaMultiset.Entry<@UnsafeVariance E>>
}

/**
 * A generic unordered collection that supports order-independent equality, like [Set], but may have duplicate
 * elements. A multiset is also sometimes called a *bag*.
 *
 * See the Guava User Guide article on [`Multiset`](https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap).
 *
 * @param E the type of elements contained in the set. The set is covariant in its element type.
 *
 * @see GuavaMultiset
 */
public interface MutableMultiset<E> : Multiset<E>, MutableCollection<E> {
    // Modification Operations
    /**
     * Adds a number of occurrences of an element to this multiset.
     *
     * @param element the element to add occurrences of; may be null only if explicitly allowed by the
     * implementation
     * @param occurrences the number of occurrences of the element to add. May be zero, in which case
     * no change will be made.
     *
     * @return the count of the element before the operation; possibly zero
     *
     * @see GuavaMultiset.add
     */
    public fun add(element: E, occurrences: Int): Int
    
    /**
     * Removes a number of occurrences of the specified element from this multiset. If the multiset
     * contains fewer than this number of occurrences to begin with, all occurrences will be removed.
     *
     * @param element the element to conditionally remove occurrences of
     * @param occurrences the number of occurrences of the element to remove. May be zero, in which
     * case no change will be made.
     *
     * @return the count of the element before the operation; possibly zero
     *
     * @throws IllegalArgumentException if `occurrences` is negative
     *
     * @see GuavaMultiset.remove
     */
    @Throws(IllegalArgumentException::class)
    public fun remove(element: E, occurrences: Int): Int
    
    /**
     * Adds or removes the necessary occurrences of an element such that the element attains the
     * desired count.
     *
     * @param element the element to add or remove occurrences of; may be null only if explicitly
     * allowed by the implementation
     * @param count the desired count of the element in this multiset
     *
     * @return the count of the element before the operation; possibly zero
     *
     * @throws IllegalArgumentException if `count` is negative
     *
     * @see GuavaMultiset.setCount
     */
    @Throws(IllegalArgumentException::class)
    public fun setCount(element: E, count: Int): Int
    
    /**
     * Conditionally sets the count of an element to a new value, as described in [setCount], provided that the element has the expected current count. If the
     * current count is not [oldCount], no change is made.
     *
     * @param element the element to conditionally set the count of; may be null only if explicitly
     * allowed by the implementation
     * @param oldCount the expected present count of the element in this multiset
     * @param newCount the desired count of the element in this multiset
     *
     * @return `true` if the condition for modification was met.
     * This implies that the multiset was indeed modified, unless `oldCount == newCount`.
     *
     * @see GuavaMultiset.setCount
     */
    public fun setCount(element: E, oldCount: Int, newCount: Int): Boolean
}

internal class GuavaMultisetWrapper<out E>(
    override val guavaMultiset: ImmutableGuavaMultiset<@UnsafeVariance E>
                                          ) : Multiset<@UnsafeVariance E>,
                                              AbstractGuavaMultisetWrapper<E>()

internal class MutableGuavaMultisetWrapper<E>(
    override val guavaMultiset: GuavaMultiset<E>
                                             ) : MutableMultiset<E>,
                                                 AbstractMutableGuavaMultisetWrapper<E>()
