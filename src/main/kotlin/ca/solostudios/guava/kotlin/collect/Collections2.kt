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
import com.google.common.base.Predicate
import com.google.common.collect.Collections2
import com.google.common.collect.Iterables

/**
 * Returns the elements of [this] that satisfy a [predicate]. The returned collection is
 * a live view of [this]; changes to one affect the other.
 *
 * The returned collection isn't threadsafe or serializable, even if [this] is.
 *
 * Many of the filtered collection's methods, such as [size][Collection.size], iterate across every
 * element in the underlying collection and determine which elements satisfy the filter. When a
 * live view is *not* needed, it may be faster to copy `Iterables.filter(unfiltered, predicate)` and use the copy.
 *
 *
 * **Warning:** [predicate] must be *consistent with equals*, as documented at [Predicate.apply].
 * Do not provide a predicate such as `Predicates.instanceOf(ArrayList.class)`, which is inconsistent with equals.
 * (See [Iterables.filter][Iterables.filter] for related functionality.)
 *
 *
 * **`Stream` equivalent:** [Stream.filter][java.util.stream.Stream.filter].
 *
 * @see Collections2.filter
 */
public inline fun <E> Collection<E>.filterView(noinline predicate: (E) -> Boolean): Collection<E> {
    return Collections2.filter(this, predicate)
}

/**
 * Returns the elements of [this] that satisfy a [predicate]. The returned collection is
 * a live view of [this]; changes to one affect the other.
 *
 * The resulting collection's iterator does not support [remove][MutableIterator.remove], but all other
 * collection methods are supported. When given an element that doesn't satisfy the predicate, the
 * collection's [add][MutableCollection.add] and [addAll][MutableCollection.addAll] methods throw an [IllegalArgumentException].
 * When methods such as [removeAll][MutableCollection.removeAll] and [clear][MutableCollection.clear] are
 * called on the filtered collection, only elements that satisfy the filter will be removed from
 * the underlying collection.
 *
 * The returned collection isn't threadsafe or serializable, even if [this] is.
 *
 * Many of the filtered collection's methods, such as [size][Collection.size], iterate across every
 * element in the underlying collection and determine which elements satisfy the filter. When a
 * live view is *not* needed, it may be faster to copy `Iterables.filter(unfiltered, predicate)` and use the copy.
 *
 *
 * **Warning:** [predicate] must be *consistent with equals*, as documented at [Predicate.apply].
 * Do not provide a predicate such as `Predicates.instanceOf(ArrayList.class)`, which is inconsistent with equals.
 * (See [Iterables.filter][Iterables.filter] for related functionality.)
 *
 *
 * **`Stream` equivalent:** [Stream.filter][java.util.stream.Stream.filter].
 *
 *
 * @see Collections2.filter
 */
@JvmName("filterMutable")
public inline fun <E> MutableCollection<E>.filterView(noinline predicate: (E) -> Boolean): MutableCollection<E> {
    return Collections2.filter(this, predicate)
}

/**
 * Returns a collection that applies [function] to each element of [this].
 * The returned collection is a live view of [this]; changes to the base collection
 * affects the returned one.
 *
 * The returned collection isn't threadsafe or serializable, even if [this] is.
 *
 * When a live view is *not* needed, it may be faster to copy the transformed collection
 * and use the copy.
 *
 * If the input [Collection] is known to be a [List], consider [Iterable.map][Iterable.map].
 * If only an [Iterable] is available, use [Iterables.transform].
 *
 *
 * **`Stream` equivalent:** [Stream.map][java.util.stream.Stream.map].
 *
 * @see Collections2.transform
 */
public inline fun <F, T> Collection<F>.transform(noinline function: (F) -> T): Collection<T> {
    return Collections2.transform(this, function)
}

/**
 * Returns a collection that applies [function] to each element of [this].
 * The returned collection is a live view of [this]; changes to the base collection
 * affects the returned one.
 *
 * The returned collection's [add][MutableCollection.add] and [addAll][MutableCollection.addAll] methods throw an {@link
 * UnsupportedOperationException}. All other collection methods are supported, as long as {@code
 * fromCollection} supports them.
 *
 * The returned collection isn't threadsafe or serializable, even if [this] is.
 *
 * When a live view is *not* needed, it may be faster to copy the transformed collection
 * and use the copy.
 *
 * If the input [Collection] is known to be a [List], consider [Iterable.map][Iterable.map].
 * If only an [Iterable] is available, use [Iterables.transform].
 *
 *
 * **`Stream` equivalent:** [Stream.map][java.util.stream.Stream.map].
 *
 * @see Collections2.transform
 */
@JvmName("transformMutable")
public inline fun <F, T> MutableCollection<F>.transform(noinline function: (F) -> T): MutableCollection<T> {
    return Collections2.transform(this, function)
}

/**
 * Returns a [Collection] of all the permutations of the specified [Iterable] using
 * [naturalOrder] for establishing the lexicographical ordering.
 *
 *
 * Examples:
 *
 * ```
 * for (List<String> perm : orderedPermutations(asList("b", "c", "a"))) {
 *     println(perm);
 * }
 * // -> ["a", "b", "c"]
 * // -> ["a", "c", "b"]
 * // -> ["b", "a", "c"]
 * // -> ["b", "c", "a"]
 * // -> ["c", "a", "b"]
 * // -> ["c", "b", "a"]
 *
 * for (List<Integer> perm : orderedPermutations(asList(1, 2, 2, 1))) {
 *     println(perm);
 * }
 * // -> [1, 1, 2, 2]
 * // -> [1, 2, 1, 2]
 * // -> [1, 2, 2, 1]
 * // -> [2, 1, 1, 2]
 * // -> [2, 1, 2, 1]
 * // -> [2, 2, 1, 1]
 * ```
 *
 *
 * *Notes:* This is an implementation of the algorithm for Lexicographical Permutations
 * Generation, described in Knuth's "The Art of Computer Programming", Volume 4, Chapter 7,
 * Section 7.2.1.2. The iteration order follows the lexicographical order. This means that the
 * first permutation will be in ascending order, and the last will be in descending order.
 *
 *
 * Elements that compare equal are considered equal and no new permutations are created by
 * swapping them.
 *
 *
 * An empty iterable has only one permutation, which is an empty list.
 *
 * This method is equivalent to `iterable.orderedPermutations(naturalOrder())`.
 *
 * @return an immutable [Collection] containing all the different permutations of the original iterable.
 *
 * @see Collections2.orderedPermutations
 * @see compareBy
 */
@ExperimentalCollectionsApi
@Suppress("UnstableApiUsage")
public inline fun <E : Comparable<E>> Iterable<E>.orderedPermutations(): Collection<List<E>> {
    return Collections2.orderedPermutations(this, naturalOrder())
}

/**
 * Returns a [Collection] of all the permutations of the specified [Iterable] using
 * the specified [selector] for establishing the lexicographical ordering.
 *
 *
 * Examples:
 *
 * ```
 * for (List<String> perm : orderedPermutations(asList("b", "c", "a"))) {
 *     println(perm);
 * }
 * // -> ["a", "b", "c"]
 * // -> ["a", "c", "b"]
 * // -> ["b", "a", "c"]
 * // -> ["b", "c", "a"]
 * // -> ["c", "a", "b"]
 * // -> ["c", "b", "a"]
 *
 * for (List<Integer> perm : orderedPermutations(asList(1, 2, 2, 1))) {
 *     println(perm);
 * }
 * // -> [1, 1, 2, 2]
 * // -> [1, 2, 1, 2]
 * // -> [1, 2, 2, 1]
 * // -> [2, 1, 1, 2]
 * // -> [2, 1, 2, 1]
 * // -> [2, 2, 1, 1]
 * ```
 *
 *
 * *Notes:* This is an implementation of the algorithm for Lexicographical Permutations
 * Generation, described in Knuth's "The Art of Computer Programming", Volume 4, Chapter 7,
 * Section 7.2.1.2. The iteration order follows the lexicographical order. This means that the
 * first permutation will be in ascending order, and the last will be in descending order.
 *
 *
 * Elements that compare equal are considered equal and no new permutations are created by
 * swapping them.
 *
 *
 * An empty iterable has only one permutation, which is an empty list.
 *
 * @param selector A selector used to select comparable elements
 *
 * @return an immutable [Collection] containing all the different permutations of the original iterable.
 *
 * @see Collections2.orderedPermutations
 * @see compareBy
 */
@ExperimentalCollectionsApi
@Suppress("UnstableApiUsage")
public inline fun <E, R : Comparable<R>> Iterable<E>.orderedPermutations(crossinline selector: (E) -> Comparable<R>): Collection<List<E>> {
    return this.orderedPermutations(compareBy(selector))
}

/**
 * Returns a [Collection] of all the permutations of the specified [Iterable] using
 * the specified [comparator] for establishing the lexicographical ordering.
 *
 *
 * Examples:
 *
 * ```
 * for (List<String> perm : orderedPermutations(asList("b", "c", "a"))) {
 *     println(perm);
 * }
 * // -> ["a", "b", "c"]
 * // -> ["a", "c", "b"]
 * // -> ["b", "a", "c"]
 * // -> ["b", "c", "a"]
 * // -> ["c", "a", "b"]
 * // -> ["c", "b", "a"]
 *
 * for (List<Integer> perm : orderedPermutations(asList(1, 2, 2, 1))) {
 *     println(perm);
 * }
 * // -> [1, 1, 2, 2]
 * // -> [1, 2, 1, 2]
 * // -> [1, 2, 2, 1]
 * // -> [2, 1, 1, 2]
 * // -> [2, 1, 2, 1]
 * // -> [2, 2, 1, 1]
 * ```
 *
 *
 * *Notes:* This is an implementation of the algorithm for Lexicographical Permutations
 * Generation, described in Knuth's "The Art of Computer Programming", Volume 4, Chapter 7,
 * Section 7.2.1.2. The iteration order follows the lexicographical order. This means that the
 * first permutation will be in ascending order, and the last will be in descending order.
 *
 *
 * Elements that compare equal are considered equal and no new permutations are created by
 * swapping them.
 *
 *
 * An empty iterable has only one permutation, which is an empty list.
 *
 * @param comparator a comparator for the iterable's elements.
 *
 * @return an immutable [Collection] containing all the different permutations of the original iterable.
 *
 * @see Collections2.orderedPermutations
 */
@ExperimentalCollectionsApi
@Suppress("UnstableApiUsage")
public inline fun <E> Iterable<E>.orderedPermutations(comparator: Comparator<E>): Collection<List<E>> {
    return Collections2.orderedPermutations(this, comparator)
}

/**
 * Returns a [Collection] of all the permutations of the specified [Collection].
 *
 *
 * *Notes:* This is an implementation of the Plain Changes algorithm for permutations
 * generation, described in Knuth's "The Art of Computer Programming", Volume 4, Chapter 7,
 * Section 7.2.1.2.
 *
 * If the input list contains equal elements, some of the generated permutations will be equal.
 *
 * An empty collection has only one permutation, which is an empty list.
 *
 * @return an immutable [Collection] containing all the different permutations of the original collection.
 *
 * @see Collections2.permutations
 */
@ExperimentalCollectionsApi
@Suppress("UnstableApiUsage")
public inline fun <reified E> Collection<E>.permutations(): Collection<List<E>> {
    return Collections2.permutations(this)
}
