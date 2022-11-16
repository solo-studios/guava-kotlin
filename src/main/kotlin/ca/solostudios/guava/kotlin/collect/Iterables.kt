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
import com.google.common.base.Optional
import com.google.common.collect.Iterables
import com.google.common.collect.Iterators
import com.google.common.collect.Lists
import com.google.common.collect.Streams
import java.util.Collections
import java.util.Queue
import java.util.stream.Stream

/**
 * Returns an unmodifiable view of [this].
 *
 * @see Iterables.unmodifiableIterable
 */
public inline fun <T> MutableIterable<T>.unmodifiableIterable(): Iterable<T> {
    return Iterables.unmodifiableIterable(this)
}

/**
 * Returns the number of elements in `this`.
 *
 * @see Iterables.size
 */
public val Iterable<*>.size: Int
    get() = Iterables.size(this)

/**
 * Removes, from an iterable, every element that belongs to the provided collection.
 *
 * This method calls [MutableCollection.removeAll] if [this] is a collection, and
 * [Iterators.removeAll] otherwise.
 *
 * @param elementsToRemove the elements to remove
 *
 * @return `true` if any element was removed from [this]
 *
 * @see Iterables.removeAll
 */
public inline fun <T> MutableIterable<T>.removeAll(elementsToRemove: Collection<T>): Boolean {
    return Iterables.removeAll(this, elementsToRemove)
}

/**
 * Removes, from an iterable, every element that does not belong to the provided collection.
 *
 * This method calls [MutableCollection.retainAll] if [this] is a collection, and
 * [Iterators.retainAll] otherwise.
 *
 * @param elementsToRetain the elements to retain
 *
 * @return `true` if any element was removed from [this]
 *
 * @see Iterables.retainAll
 */
public inline fun <T> MutableIterable<T>.retainAll(elementsToRetain: Collection<T>): Boolean {
    return Iterables.retainAll(this, elementsToRetain)
}

/**
 * Removes, from an iterable, every element that satisfies the provided predicate.
 *
 * Removals may or may not happen immediately as each element is tested against the predicate.
 * The behavior of this method is not specified if [predicate] is dependent on [this].
 *
 * **Java 8 users:** if [this] is a [Collection], use `this.removeIf(predicate)` instead.
 *
 * @param predicate a predicate that determines whether an element should be removed
 *
 * @return `true` if any elements were removed from the iterable
 *
 * @see Iterables.removeIf
 */
public inline fun <T> MutableIterable<T>.removeIf(noinline predicate: (T) -> Boolean): Boolean {
    return Iterables.removeIf(this, predicate)
}

/**
 * Determines whether two iterables contain equal elements in the same order. More specifically,
 * this method returns `true` if [this] and [other] contain the same
 * number of elements and every element of [other] is equal to the corresponding element
 * of [other].
 *
 * @see Iterables.elementsEqual
 */
public inline fun <T> Iterable<T>.elementsEqual(other: Iterable<T>): Boolean {
    return Iterables.elementsEqual(this, other)
}

/**
 * Returns a string representation of [this], with the format `[e1, e2, ..., en]`
 * (that is, identical to [Arrays][java.util.Arrays]`.toString(Iterables.toArray(iterable))`).
 * Note that for *most* implementations of [Collection], `collection.toString()` also gives the same result, but that behavior is not
 * generally guaranteed.
 *
 * @see Iterables.toString
 */
public inline fun <T> Iterable<T>.asString(): String {
    return Iterables.toString(this)
}

/**
 * Copies an iterable's elements into an array.
 *
 * @param T the type of the elements
 *
 * @return a newly-allocated array into which all the elements of the iterable have been copied
 *
 * @see Iterables.toArray
 */
public inline fun <reified T> Iterable<T>.toTypedArray(): Array<out T> {
    return Iterables.toArray(this, T::class.java) as Array<out T>
}

/**
 * Adds all elements in [this] to `collection`.
 *
 * @return `true` if `collection` was modified as a result of this operation.
 *
 * @see Iterables.addAll
 */
public inline fun <T> MutableCollection<T>.addAll(elementsToAdd: Iterable<T>): Boolean {
    return Iterables.addAll(this, elementsToAdd)
}

/**
 * Returns the number of elements in the specified iterable that equal the specified object. This
 * implementation avoids a full iteration when the iterable is a [Multiset] or [Set].
 *
 * **Java 8 users:** In most cases, the [Stream] equivalent of this method is `stream.filter(element::equals).count()`.
 * If `element` might be null, use `stream.filter(Predicate.isEqual(element)).count()` instead.
 *
 * @see Collections.frequency
 * @see Iterables.frequency
 */
public inline fun <T> Iterable<T>.frequency(element: T): Int {
    return Iterables.frequency(this, element)
}

/**
 * Returns an iterable whose iterators cycle indefinitely over the elements of [this].
 *
 * **Warning:** Typical uses of the resulting iterator may produce an infinite loop. You
 * should use an explicit `break` or be certain that you will eventually remove all the
 * elements.
 *
 * To cycle over the iterable `n` times, use the following: `Iterables.concat(Collections.nCopies(n, iterable))`
 *
 * **Java 8 users:** The [Stream] equivalent of this method is `Stream.generate(() -> iterable).flatMap(Streams::stream)`.
 *
 * @see Iterables.cycle
 */
public inline fun <T> Iterable<T>.cycleIterable(): Iterable<T> {
    return Iterables.cycle(this)
}

/**
 * Returns an iterable whose iterators cycle indefinitely over the elements of [this].
 *
 * That iterator supports [remove][MutableIterator.remove] if `iterable.iterator()` does.
 * After [remove][MutableIterator.remove] is called, subsequent cycles omit the removed element,
 * which is no longer in [this]. The iterator's [hasNext][Iterator.hasNext] method returns `true` until [this]
 * is empty.
 *
 * **Warning:** Typical uses of the resulting iterator may produce an infinite loop. You
 * should use an explicit `break` or be certain that you will eventually remove all the
 * elements.
 *
 * To cycle over the iterable `n` times, use the following: `Iterables.concat(Collections.nCopies(n, iterable))`
 *
 * **Java 8 users:** The [Stream] equivalent of this method is `Stream.generate(() -> iterable).flatMap(Streams::stream)`.
 *
 * @see Iterables.cycle
 */
@JvmName("cycleIterableMutable")
public inline fun <T> MutableIterable<T>.cycleIterable(): MutableIterable<T> {
    return Iterables.cycle(this)
}

/**
 * Returns an iterable whose iterators cycle indefinitely over the provided elements.
 *
 * After [remove][MutableIterator.remove] is invoked on a generated iterator, the removed element will no longer
 * appear in either that iterator or any other iterator created from the same source iterable.
 * That is, this method behaves exactly as `Iterables.cycle(Lists.newArrayList(elements))`.
 * The iterator's [hasNext][Iterator.hasNext] method returns `true` until all of the original elements
 * have been removed.
 *
 * **Warning:** Typical uses of the resulting iterator may produce an infinite loop. You
 * should use an explicit `break` or be certain that you will eventually remove all the
 * elements.
 *
 * To cycle over the elements `n` times, use the following: `Iterables.concat(Collections.nCopies(n, Arrays.asList(elements)))`
 *
 * **Java 8 users:** If passing a single element `e`, the [Stream] equivalent of
 * this method is `Stream.generate(() -> e)`. Otherwise, put the elements in a collection
 * and use `Stream.generate(() -> collection).flatMap(Collection::stream)`.
 *
 * @see Iterables.cycle
 */
public inline fun <T> cycleIterable(vararg elements: T): Iterable<T> {
    return Iterables.cycle(Lists.newArrayList(*elements))
}

/**
 * Combines two iterables into a single iterable. The returned iterable has an iterator that
 * traverses the elements in [this], followed by the elements in [other]. The source
 * iterators are not polled until necessary.
 *
 * **Java 8 users:** The [Stream] equivalent of this method is `Stream.concat(this, other)`.
 *
 * @see Iterables.concat
 */
public inline infix fun <T> Iterable<T>.concat(other: Iterable<T>): Iterable<T> {
    return Iterables.concat(this, other)
}

/**
 * Combines two iterables into a single iterable. The returned iterable has an iterator that
 * traverses the elements in [this], followed by the elements in [other]. The source
 * iterators are not polled until necessary.
 *
 * The returned iterable's iterator supports [remove][MutableIterator.remove] when the corresponding input
 * iterator supports it.
 *
 * **Java 8 users:** The [Stream] equivalent of this method is `Stream.concat(this, other)`.
 *
 * @see Iterables.concat
 */
@JvmName("concatMutable")
public inline infix fun <T> MutableIterable<T>.concat(other: MutableIterable<T>): MutableIterable<T> {
    return Iterables.concat(this, other)
}

/**
 * Combines three iterables into a single iterable. The returned iterable has an iterator that
 * traverses the elements in [this], followed by the elements in [a], followed by the
 * elements in [b]. The source iterators are not polled until necessary.
 *
 * **Java 8 users:** The [Stream] equivalent of this method is `Stream.concat(this, b, c)`.
 *
 * @see Iterables.concat
 */
public inline fun <T> Iterable<T>.concat(a: Iterable<T>, b: Iterable<T>): Iterable<T> {
    return Iterables.concat(this, a, b)
}

/**
 * Combines three iterables into a single iterable. The returned iterable has an iterator that
 * traverses the elements in [this], followed by the elements in [a], followed by the
 * elements in [b]. The source iterators are not polled until necessary.
 *
 * The returned iterable's iterator supports [remove][MutableIterator.remove] when the corresponding input
 * iterator supports it.
 *
 * @see Iterables.concat
 */
@JvmName("concatMutable")
public inline fun <T> MutableIterable<T>.concat(a: MutableIterable<T>, b: MutableIterable<T>): MutableIterable<T> {
    return Iterables.concat(this, a, b)
}

/**
 * Combines four iterables into a single iterable. The returned iterable has an iterator that
 * traverses the elements in [this], followed by the elements in [a], followed by the
 * elements in [b], followed by the elements in [c]. The source iterators are not
 * polled until necessary.
 *
 * **Java 8 users:** The [Stream] equivalent of this method is `Streams.concat(a, b, c, d)`.
 *
 * @see Iterables.concat
 */
public inline fun <T> Iterable<T>.concat(a: Iterable<T>, b: Iterable<T>, c: Iterable<T>): Iterable<T> {
    return Iterables.concat(this, a, b, c)
}

/**
 * Combines four iterables into a single iterable. The returned iterable has an iterator that
 * traverses the elements in [this], followed by the elements in [a], followed by the
 * elements in [b], followed by the elements in [c]. The source iterators are not
 * polled until necessary.
 *
 * The returned iterable's iterator supports [remove][MutableIterator.remove] when the corresponding input
 * iterator supports it.
 *
 * **Java 8 users:** The [Stream] equivalent of this method is `Streams.concat(a, b, c, d)`.
 *
 * @see Iterables.concat
 */
@JvmName("concatMutable")
public inline fun <T> MutableIterable<T>.concat(a: MutableIterable<T>, b: MutableIterable<T>, c: MutableIterable<T>): MutableIterable<T> {
    return Iterables.concat(this, a, b, c)
}

/**
 * Combines multiple iterables into a single iterable. The returned iterable has an iterator that
 * traverses the elements in [this], followed by the elements of each iterable in [iterables] sequentially.
 * The source iterators are not polled until necessary.
 *
 * **Java 8 users:** The [Stream] equivalent of this method is `streamOfStreams.flatMap(...)`.
 *
 * @see Iterables.concat
 */
public inline fun <T> Iterable<T>.concat(vararg iterables: Iterable<T>): Iterable<T> {
    return Iterables.concat(this, *iterables)
}

/**
 * Combines multiple iterables into a single iterable. The returned iterable has an iterator that
 * traverses the elements in [this], followed by the elements of each iterable in [iterables] sequentially.
 * The source iterators are not polled until necessary.
 *
 * The returned iterable's iterator supports [remove][MutableIterator.remove] when the corresponding input
 * iterator supports it.
 *
 * **Java 8 users:** The [Stream] equivalent of this method is `streamOfStreams.flatMap(...)`.
 *
 * @see Iterables.concat
 */
@JvmName("concatMutable")
public inline fun <T> MutableIterable<T>.concat(vararg iterables: MutableIterable<T>): MutableIterable<T> {
    return Iterables.concat(this, *iterables)
}

/**
 * Combines multiple iterables into a single iterable. The returned iterable has an iterator that
 * traverses the elements in [this] sequentially.
 * The source iterators are not polled until necessary.
 *
 * **Java 8 users:** The [Stream] equivalent of this method is `streamOfStreams.flatMap(s -> s)`.
 *
 * @see Iterables.concat
 */
public inline fun <T> Iterable<Iterable<T>>.flatConcat(): Iterable<T> {
    return Iterables.concat(this)
}

/**
 * Combines multiple iterables into a single iterable. The returned iterable has an iterator that
 * traverses the elements in [this] sequentially.
 * The source iterators are not polled until necessary.
 *
 * The returned iterable's iterator supports [remove][MutableIterator.remove] when the corresponding input
 * iterator supports it.
 *
 * **Java 8 users:** The [Stream] equivalent of this method is `streamOfStreams.flatMap(s -> s)`.
 *
 * @see Iterables.concat
 */
@JvmName("flatConcatMutable")
public inline fun <T> Iterable<MutableIterable<T>>.flatConcat(): MutableIterable<T> {
    return Iterables.concat(this)
}

/**
 * Divides an iterable into unmodifiable sublists of the given size (the final iterable may be
 * smaller). For example, partitioning an iterable containing `[a, b, c, d, e]` with a
 * partition size of 3 yields `[[a, b, c], [d, e]]` -- an outer iterable containing two
 * inner lists of three and two elements, all in the original order.
 *
 * **Note:** The current implementation eagerly allocates storage for [size] elements.
 * As a consequence, passing values like [Int.MAX_VALUE] can lead to [OutOfMemoryError].
 *
 * **Note:** if [this] is a [List], use [Lists.partition]
 * instead.
 *
 * @param size the desired size of each partition (the last may be smaller)
 *
 * @return an iterable of unmodifiable lists containing the elements of [this] divided
 * into partitions
 *
 * @throws IllegalArgumentException if [size] is non-positive
 *
 * @see Iterables.partition
 */
@Throws(IllegalArgumentException::class)
public inline fun <T> Iterable<T>.partition(size: Int): Iterable<List<T>> {
    return Iterables.partition(this, size)
}

/**
 * Divides an iterable into unmodifiable sublists of the given size, padding the final iterable
 * with null values if necessary. For example, partitioning an iterable containing `[a, b, c, d, e]`
 * with a partition size of 3 yields `[[a, b, c], [d, e, null]]` -- an outer iterable containing
 * two inner lists of three elements each, all in the original order.
 *
 * Iterators returned by the returned iterable do not support the [remove][MutableIterator.remove]
 * method.
 *
 * @param size the desired size of each partition
 *
 * @return an iterable of unmodifiable lists containing the elements of [this] divided
 * into partitions (the final iterable may have trailing null elements)
 *
 * @throws IllegalArgumentException if [size] is non-positive
 *
 * @see Iterables.paddedPartition
 */
@Throws(IllegalArgumentException::class)
public inline fun <T> Iterable<T>.paddedPartition(size: Int): Iterable<List<T?>> {
    return Iterables.paddedPartition(this, size)
}

/**
 * Returns a view of [this] containing all elements that satisfy the input predicate
 * [retainIfTrue].
 *
 * **[Stream] equivalent:** [Stream.filter].
 *
 * @see Iterables.filter
 */
public inline fun <T> Iterable<T>.filterView(noinline retainIfTrue: (T) -> Boolean): Iterable<T> {
    return Iterables.filter(this, retainIfTrue)
}

/**
 * Returns a view of [this] containing all elements that are of the type [T].
 *
 * **[Stream] equivalent:** `stream.filter(type::isInstance).map(type::cast)`.
 * This does perform a little more work than necessary, so another option is to insert an
 * unchecked cast at some later point:
 *
 * ```
 * @Suppress("UNCHECKED_CAST")
 * val result: List<String> = stream.filter(String::class::isInstance).toList() as List<String>
 * ```
 *
 * @see Iterables.filter
 */
public inline fun <reified T> Iterable<*>.filterIsInstanceView(): Iterable<T> {
    return Iterables.filter(this, T::class.java)
}

/**
 * Returns `true` if any element in [this] satisfies the predicate.
 *
 * **[Stream] equivalent:** [Stream.anyMatch].
 *
 * @see Iterables.any
 */
public inline fun <T> Iterable<T>.any(noinline predicate: (T) -> Boolean): Boolean {
    return Iterables.any(this, predicate)
}

/**
 * Returns `true` if every element in [this] satisfies the predicate. If [this] is empty, `true` is returned.
 *
 * **[Stream] equivalent:** [Stream.allMatch].
 *
 * @see Iterables.all
 */
public inline fun <T> Iterable<T>.all(noinline predicate: (T) -> Boolean): Boolean {
    return Iterables.all(this, predicate)
}

/**
 * Returns the first element in [this] that satisfies the given predicate; use this
 * method only when such an element is known to exist. If it is possible that *no* element
 * will match, use [tryFind] or [find] instead.
 *
 * **[Stream] equivalent:** `stream.filter(predicate).findFirst().get()`
 *
 * @throws NoSuchElementException if no element in [this] matches the given predicate and [defaultValue] is `null`.
 *
 * @see Iterables.find
 */
@Throws(NoSuchElementException::class)
public inline fun <T> Iterable<T>.find(defaultValue: T? = null, noinline predicate: (T) -> Boolean): T {
    return if (defaultValue == null) Iterables.find(this, predicate) else Iterables.find(this, predicate, defaultValue)!!
}

/**
 * Returns an [Optional] containing the first element in [this] that satisfies the
 * given predicate, if such an element exists.
 *
 * **Warning:** avoid using a [predicate] that matches `null`. If `null`
 * is matched in [this], a NullPointerException will be thrown.
 *
 * **[Stream] equivalent:** `stream.filter(predicate).findFirst()`
 *
 * @see Iterables.tryFind
 */
public inline fun <T> Iterable<T>.tryFind(noinline predicate: (T) -> Boolean): Optional<T> {
    return Iterables.tryFind(this, predicate)
}

/**
 * Returns the index in [this] of the first element that satisfies the provided [predicate], or `-1` if the Iterable has no such elements.
 *
 * More formally, returns the lowest index `i` such that `predicate.apply(Iterables.get(iterable, i))` returns `true`, or `-1` if there is no
 * such index.
 *
 * @see Iterables.indexOf
 */
public inline fun <T> Iterable<T>.indexOf(noinline predicate: (T) -> Boolean): Int {
    return Iterables.indexOf(this, predicate)
}

/**
 * Returns a view containing the result of applying [function] to each element of [this].
 *
 * If the input [this] is known to be a [List] or other [Collection],
 * consider [List.transform] and [Collection.transform].
 *
 * **[Stream] equivalent:** [Stream.map]
 *
 * @see Iterables.transform
 */
public inline fun <F, T> Iterable<F>.transform(noinline function: (F) -> T): Iterable<T> {
    return Iterables.transform(this, function)
}

/**
 * Returns a view containing the result of applying `function` to each element of `fromIterable`.
 *
 * The returned iterable's iterator supports [remove][MutableIterator.remove] if [this]'s
 * iterator does. After a successful [remove][MutableIterator.remove] call, [this] no longer
 * contains the corresponding element.
 *
 * If the input [this] is known to be a [List] or other [Collection],
 * consider [List.transform] and [Collection.transform].
 *
 * **[Stream] equivalent:** [Stream.map]
 *
 * @see Iterables.transform
 */
@JvmName("transformMutable")
public inline fun <F, T> MutableIterable<F>.transform(noinline function: (F) -> T): MutableIterable<T> {
    return Iterables.transform(this, function)
}

/**
 * Returns the element at the specified position in an iterable.
 *
 * **[Stream] equivalent:** `stream.skip(position).findFirst().get()` (throws
 * [NoSuchElementException] if out of bounds)
 *
 * @param position position of the element to return
 *
 * @return the element at the specified position in [this]
 *
 * @throws IndexOutOfBoundsException if [position] is negative or greater than or equal to
 * the size of [this]
 *
 * @see Iterables.get
 */
@Throws(IndexOutOfBoundsException::class)
public inline operator fun <T> Iterable<T>.get(position: Int): T {
    return Iterables.get(this, position)
}

/**
 * Returns the element at the specified position in an iterable or a default value otherwise.
 *
 *  * **[Stream] equivalent:** `stream.skip(position).findFirst().get()` (throws
 * [NoSuchElementException] if out of bounds)
 *
 * @param position position of the element to return
 *
 * @param defaultValue the default value to return if [position] is greater than the size of [this]
 *
 * @return the element at the specified position in [this], or [defaultValue] if [this]
 * produces fewer that `position + 1` elements and [defaultValue] is not `null`.
 *
 * @throws IndexOutOfBoundsException if [position] is negative, or if [position] is greater than or equal to
 * the number of elements remaining in [this] *and* [defaultValue] is `null`.
 *
 * @see Iterables.get
 */
@Throws(IndexOutOfBoundsException::class)
public inline fun <T> Iterable<T>.get(position: Int, defaultValue: T? = null): T {
    return if (defaultValue == null) Iterables.get(this, position) else Iterables.get(this, position, defaultValue)
}

/**
 * Returns the first element in [this] or `defaultValue` if the iterable is empty.
 * The [Iterators] analog to this method is [Iterators.getNext].
 *
 * If no default value is desired (and the caller instead wants a [NoSuchElementException] to be thrown),
 * it is recommended that `iterable.iterator().next()` is used instead.
 *
 * To get the only element in a single-element [this], consider using [ ][.getOnlyElement] or [.getOnlyElement] instead.
 *
 * **[Stream] equivalent:** `stream.findFirst().orElse(defaultValue)`
 *
 * @param defaultValue the default value to return if the iterable is empty
 *
 * @return the first element of [this] or the default value
 *
 * @see Iterables.getFirst
 */
public inline fun <T> Iterable<T>.getFirst(defaultValue: T): T {
    return Iterables.getFirst(this, defaultValue)
}

/**
 * Returns the last element of [this] or `defaultValue` if the iterable is empty.
 * If [this] is a [List] with [RandomAccess] support, then this operation is
 * guaranteed to be `O(1)`.
 *
 * **[Stream] equivalent:** [Streams.findLast(stream).get()][Streams.findLast]
 *
 * @param defaultValue the value to return if [this] is empty
 *
 * @return the last element of [this] or the default value
 *
 * @throws NoSuchElementException if the iterable is empty and the default value is `null`
 *
 * @see Iterables.getLast
 */
@Throws(NoSuchElementException::class)
public inline fun <T> Iterable<T>.getLast(defaultValue: T? = null): T {
    return if (defaultValue == null) Iterables.getLast(this) else Iterables.getLast(this, defaultValue)
}

/**
 * Returns a view of [this] that skips its first [numberToSkip] elements. If
 * [this] contains fewer than [numberToSkip] elements, the returned iterable skips
 * all of its elements.
 *
 * Modifications to the underlying [Iterable] before a call to [`iterator()`][Iterable.iterator] are
 * reflected in the returned iterator. That is, the iterator skips the first [numberToSkip]
 * elements that exist when the `Iterator` is created, not when [skip] is called.
 *
 * **[Stream] equivalent:** [Stream.skip]
 *
 * @see Iterables.skip
 */
public inline fun <T> Iterable<T>.skip(numberToSkip: Int): Iterable<T> {
    return Iterables.skip(this, numberToSkip)
}

/**
 * Returns a view of [this] that skips its first [numberToSkip] elements. If
 * [this] contains fewer than [numberToSkip] elements, the returned iterable skips
 * all of its elements.
 *
 * Modifications to the underlying [Iterable] before a call to [`iterator()`][Iterable.iterator] are
 * reflected in the returned iterator. That is, the iterator skips the first [numberToSkip]
 * elements that exist when the `Iterator` is created, not when [skip] is called.
 *
 * The returned iterable's iterator supports [`remove()`][MutableIterator.remove] if the iterator of the underlying
 * iterable supports it. Note that it is *not* possible to delete the last skipped element by
 * immediately calling [`remove()`][MutableIterator.remove] on that iterator, as the [MutableIterator] contract states
 * that a call to [`remove()`][MutableIterator.remove] before a call to [`next()`][Iterable] will throw an [IllegalStateException].
 *
 * **[Stream] equivalent:** [Stream.skip]
 *
 * @see Iterables.skip
 */
@JvmName("skipMutable")
public inline fun <T> MutableIterable<T>.skip(numberToSkip: Int): Iterable<T> {
    return Iterables.skip(this, numberToSkip)
}

/**
 * Returns a view of [this] containing its first `limitSize` elements. If [this] contains fewer than `limitSize` elements, the returned view contains all of its
 * elements. The returned iterable's iterator supports `remove()` if [this]'s
 * iterator does.
 *
 *
 * **[Stream] equivalent:** [Stream.limit]
 *
 * @param limitSize the maximum number of elements in the returned iterable
 *
 * @throws IllegalArgumentException if `limitSize` is negative
 *
 * @see Iterables.limit
 */
@Throws(IllegalArgumentException::class)
public inline fun <T> Iterable<T>.limit(limitSize: Int): Iterable<T> {
    return Iterables.limit(this, limitSize)
}

/**
 * Returns a view of the supplied iterable that wraps each generated [Iterator] through
 * [Iterators.consumingIterator].
 *
 *
 * Note: If [this] is a [Queue], the returned iterable will get entries from
 * [Queue.remove] since [Queue]'s iteration order is undefined. Calling [Iterator.hasNext] on a generated iterator from the returned iterable may cause an item to be
 * immediately dequeued for return on a subsequent call to [Iterator.next].
 *
 * @return a view of the supplied iterable that wraps each generated iterator through
 * [`Iterators.consumingIterator(iterator)`][Iterators.consumingIterator]; for queues, an iterable that generates iterators
 * that return and consume the queue's elements in queue order
 *
 * @see Iterables.consumingIterable
 */
public inline fun <T> MutableIterable<T>.consumingIterable(): Iterable<T> {
    return Iterables.consumingIterable(this)
}

// Methods only in Iterables, not in Iterators
/**
 * Determines if the given iterable contains no elements.
 *
 * There is no precise [Iterator] equivalent to this method, since one can only ask an
 * iterator whether it has any elements *remaining* (which one does using [Iterator.hasNext]).
 *
 * **[Stream] equivalent:** `!stream.findAny().isPresent()`
 *
 * @return `true` if the iterable contains no elements
 *
 * @see Iterables.isEmpty
 */
public inline fun <T> Iterable<T>.isEmpty(): Boolean {
    return Iterables.isEmpty(this)
}

/**
 * Returns an iterable over the merged contents of all given [Iterable]s in [this]. Equivalent entries
 * will not be de-duplicated.
 *
 * Callers must ensure that the source [Iterable]s are in non-descending order as this
 * method does not sort its input.
 *
 * For any equivalent elements across all [this], it is undefined which element is
 * returned first.
 *
 * @see Iterables.mergeSorted
 */
@Suppress("UnstableApiUsage")
@OptIn(ExperimentalCollectionsApi::class)
public inline fun <T, R : Comparable<R>> Iterable<Iterable<T>>.mergeSorted(crossinline selector: (T) -> Comparable<R>): Iterable<T> {
    return this.mergeSorted(compareBy(selector))
}

/**
 * Returns an iterable over the merged contents of all given [Iterable]s in [this]. Equivalent entries
 * will not be de-duplicated.
 *
 * Callers must ensure that the source [Iterable]s are in non-descending order as this
 * method does not sort its input.
 *
 * For any equivalent elements across all [this], it is undefined which element is
 * returned first.
 *
 * @see Iterables.mergeSorted
 */
@Suppress("UnstableApiUsage")
@ExperimentalCollectionsApi
public inline fun <T> Iterable<Iterable<T>>.mergeSorted(comparator: Comparator<T>): Iterable<T> {
    return Iterables.mergeSorted(this, comparator)
}
