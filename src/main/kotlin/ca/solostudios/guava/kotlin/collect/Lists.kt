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
import com.google.common.collect.ImmutableList
import com.google.common.collect.Lists
import java.util.stream.Stream

/**
 * Returns every possible list that can be formed by choosing one element from each of the given
 * lists in order;
 * the "n-ary [Cartesian product](https://en.wikipedia.org/wiki/Cartesian_product)" of the lists.
 * For example:
 *
 * ```kotlin
 * val cartesianProduct = listOf(listOf(1, 2), listOf("A", "B", "C")).cartesianProduct()
 * ```
 *
 * returns a list containing six lists in the following order:
 *
 *  - `[1, "A"]`
 *  - `[1, "B"]`
 *  - `[1, "C"]`
 *  - `[2, "A"]`
 *  - `[2, "B"]`
 *  - `[2, "C"]`
 *
 *
 * The result is guaranteed to be in the "traditional", lexicographical order for Cartesian
 * products that you would get from nesting for loops:
 *
 * ```kotlin
 * for (B b0 in lists.get(0)) {
 *     for (B b1 in lists.get(1)) {
 *         ...
 *         val tuple = listOf(b0, b1, ...)
 *         // operate on tuple
 *     }
 * }
 * ```
 *
 * Note that if any input list is empty, the Cartesian product will also be empty. If no lists
 * at all are provided (an empty list), the resulting Cartesian product has one element, an empty
 * list (counter-intuitive, but mathematically consistent).
 *
 * *Performance notes:* while the cartesian product of lists of size `m, n, p` is a
 * list of size `m x n x p`, its actual memory consumption is much smaller. When the
 * cartesian product is constructed, the input lists are merely copied. Only as the resulting list
 * is iterated are the individual lists created, and these are not retained after iteration.
 *
 * @param B any common base class shared by all axes (often just [Any])
 *
 * @return the Cartesian product, as an immutable list containing immutable lists
 *
 * @throws IllegalArgumentException if the size of the cartesian product would be greater than
 * [Integer.MAX_VALUE]
 *
 * @throws NullPointerException any one of the [List]s in [this], or any element of
 * a provided list is `null`
 *
 * @see Lists.cartesianProduct
 */
@Throws(NullPointerException::class, IllegalArgumentException::class)
public inline fun <B> List<List<B>>.cartesianProduct(): List<List<B>> {
    return Lists.cartesianProduct(this)
}

/**
 * Returns every possible list that can be formed by choosing one element from each of the given
 * lists in order; the "n-ary [Cartesian
 * product](http://en.wikipedia.org/wiki/Cartesian_product)" of the lists. For example:
 *
 * ```kotlin
 * val cartesianProduct = cartesianProductOf(listOf(1, 2), listOf("A", "B", "C"))
 * ```
 *
 * returns a list containing six lists in the following order:
 *
 *  - `[1, "A"]`
 *  - `[1, "B"]`
 *  - `[1, "C"]`
 *  - `[2, "A"]`
 *  - `[2, "B"]`
 *  - `[2, "C"]`
 *
 *
 * The result is guaranteed to be in the "traditional", lexicographical order for Cartesian
 * products that you would get from nesting for loops:
 *
 * ```kotlin
 * for (B b0 in lists.get(0)) {
 *     for (B b1 in lists.get(1)) {
 *         ...
 *         val tuple = listOf(b0, b1, ...)
 *         // operate on tuple
 *     }
 * }
 * ```
 *
 *
 * Note that if any input list is empty, the Cartesian product will also be empty. If no lists
 * at all are provided (an empty list), the resulting Cartesian product has one element, an empty
 * list (counter-intuitive, but mathematically consistent).
 *
 *
 * *Performance notes:* while the cartesian product of lists of size `m, n, p` is a
 * list of size `m x n x p`, its actual memory consumption is much smaller. When the
 * cartesian product is constructed, the input lists are merely copied. Only as the resulting list
 * is iterated are the individual lists created, and these are not retained after iteration.
 *
 * @param lists the lists to choose elements from, in the order that the elements chosen from
 * those lists should appear in the resulting lists
 *
 * @param B any common base class shared by all axes (often just [Any])
 *
 * @return the Cartesian product, as an immutable list containing immutable lists
 *
 * @throws IllegalArgumentException if the size of the cartesian product would be greater than
 * [Integer.MAX_VALUE]
 *
 * @throws NullPointerException if [lists], any one of the [List]s in [lists], or any element of
 * a provided list is `null`
 *
 * @see Lists.cartesianProduct
 */
public inline fun <B> cartesianProductOf(vararg lists: List<B>): List<List<B>?>? {
    return Lists.cartesianProduct(*lists)
}

/**
 * Returns a list that applies [function] to each element of [this]. The returned
 * list is a transformed view of [this]; changes to [this] will be reflected
 * in the returned list and vice versa.
 *
 * The function is applied lazily, invoked when needed. This is necessary for the returned list
 * to be a view, but it means that the function will be applied many times for bulk operations
 * like [List.contains] and [List.hashCode]. For this to perform well, [function] should be fast.
 * To avoid lazy evaluation when the returned list doesn't need to be a
 * view, copy the returned list into a new list of your choosing.
 *
 * If [this] implements [RandomAccess], so will the returned list. The returned
 * list is threadsafe if the supplied list and function are.
 *
 * If only a `Collection` or `Iterable` input is available, use [Collection.transform] or [Iterable.transform].
 *
 * **Note:** serializing the returned list is implemented by serializing [this],
 * its contents, and [function] -- *not* by serializing the transformed values. This
 * can lead to surprising behavior, so serializing the returned list is **not recommended**.
 * Instead, copy the list using [ImmutableList.copyOf] (for example), then
 * serialize the copy. Other methods similar to this do not implement serialization at all for
 * this reason.
 *
 * **Java 8 users:** many use cases for this method are better addressed by [Stream.map][Stream.map]. This method is not being deprecated, but we gently encourage you
 * to migrate to streams.
 *
 * @see Lists.transform
 */
public inline fun <F, T> List<F>.transform(noinline function: (F) -> T): List<T> {
    return Lists.transform(this, function)
}

/**
 * Returns a list that applies [function] to each element of [this]. The returned
 * list is a transformed view of [this]; changes to [this] will be reflected
 * in the returned list and vice versa.
 *
 * Since functions are not reversible, the transform is one-way and new items cannot be stored
 * in the returned list. The `add`, `addAll` and `set` methods are unsupported
 * in the returned list.
 *
 * The function is applied lazily, invoked when needed. This is necessary for the returned list
 * to be a view, but it means that the function will be applied many times for bulk operations
 * like [List.contains] and [List.hashCode]. For this to perform well, [function] should be fast.
 * To avoid lazy evaluation when the returned list doesn't need to be a
 * view, copy the returned list into a new list of your choosing.
 *
 * If [this] implements [RandomAccess], so will the returned list. The returned
 * list is threadsafe if the supplied list and function are.
 *
 * If only a `Collection` or `Iterable` input is available, use [Collection.transform] or [Iterable.transform].
 *
 * **Note:** serializing the returned list is implemented by serializing [this],
 * its contents, and [function] -- *not* by serializing the transformed values. This
 * can lead to surprising behavior, so serializing the returned list is **not recommended**.
 * Instead, copy the list using [ImmutableList.copyOf] (for example), then
 * serialize the copy. Other methods similar to this do not implement serialization at all for
 * this reason.
 *
 * **Java 8 users:** many use cases for this method are better addressed by [Stream.map][Stream.map]. This method is not being deprecated, but we gently encourage you
 * to migrate to streams.
 *
 * @see Lists.transform
 */
@JvmName("transformMutable")
public inline fun <F, T> MutableList<F>.transform(noinline function: (F) -> T): MutableList<T> {
    return Lists.transform(this, function)
}

/**
 * Returns consecutive [sublists][List.subList] of a list, each of the same
 * size (the final list may be smaller). For example, partitioning a list containing `[a, b, c, d, e]`
 * with a partition size of 3 yields `[[a, b, c], [d, e]]` -- an outer list
 * containing two inner lists of three and two elements, all in the original order.
 *
 * The outer list is unmodifiable, but reflects the latest state of the source list. The inner
 * lists are sublist views of the original list, produced on demand using [List.subList],
 * and are subject to all the usual caveats about modification as explained in that API.
 *
 * @param size the desired size of each sublist (the last may be smaller)
 *
 * @return a list of consecutive sublists
 *
 * @throws IllegalArgumentException if `partitionSize` is non-positive
 *
 * @see Lists.partition
 */
public fun <T> List<T>.partition(size: Int): List<List<T>> {
    return Lists.partition(this, size)
}

/**
 * Returns a view of the specified string as an immutable list of [Char] values.
 *
 * @see Lists.charactersOf
 */
public fun String.charactersOf(): List<Char> {
    return Lists.charactersOf(this)
}

/**
 * Returns a view of the specified [CharSequence] as a `List<Character>`, viewing
 * [this] as a sequence of Unicode code units. The view does not support any
 * modification operations, but reflects any changes to the underlying character sequence.
 *
 * @return a `List<Character>` view of the character sequence
 *
 * @see Lists.charactersOf
 */
@ExperimentalCollectionsApi
@Suppress("UnstableApiUsage")
public fun CharSequence.charactersOf(): List<Char?>? {
    return Lists.charactersOf(this)
}
