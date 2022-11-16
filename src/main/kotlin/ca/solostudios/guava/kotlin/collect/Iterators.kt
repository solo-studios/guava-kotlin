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
import com.google.common.collect.ImmutableList
import com.google.common.collect.Iterables
import com.google.common.collect.Iterators
import com.google.common.collect.PeekingIterator
import java.util.Arrays
import java.util.Collections
import java.util.Enumeration

/**
 * Returns an unmodifiable view of [this].
 *
 * @see Iterators.unmodifiableIterator
 */
public inline fun <T> MutableIterator<T>.unmodifiableIterator(): Iterator<T> {
    return Iterators.unmodifiableIterator(this)
}

/**
 * Returns the number of elements remaining in `this`.
 * The iterator will be left exhausted: its [hasNext][Iterator.hasNext] method will return `false`.
 *
 * @see Iterators.size
 */
public val Iterator<*>.size: Int
    get() = Iterators.size(this)

/**
 *  Returns `true` if [this] contains [element].
 *
 *  @see Iterators.contains
 */
public inline fun <T> Iterator<T>.contains(element: T): Boolean {
    return Iterators.contains(this, element)
}

/**
 * Traverses an iterator and removes every element that belongs to the provided collection.
 * The iterator will be left exhausted: its [hasNext][Iterator.hasNext] method will return `false`.
 *
 * @param elementsToRemove the elements to remove
 *
 * @return `true` if any element was removed from [this]
 *
 * @see Iterators.removeAll
 */
public inline fun <T> MutableIterator<T>.removeAll(elementsToRemove: Collection<T>): Boolean {
    return Iterators.removeAll(this, elementsToRemove)
}

/**
 * Removes every element that satisfies the provided predicate from the iterator.
 * The iterator will be left exhausted: its [hasNext][Iterator.hasNext] method will return `false`.
 *
 * @param predicate a predicate that determines whether an element should be removed
 *
 * @return `true` if any elements were removed from the [this]
 *
 * @see Iterators.removeIf
 */
public inline fun <T> MutableIterator<T>.removeIf(noinline predicate: (T) -> Boolean): Boolean {
    return Iterators.removeIf(this, predicate)
}

/**
 * Traverses an iterator and removes every element that does not belong to the provided collection.
 * The iterator will be left exhausted: its [hasNext][Iterator.hasNext] method will return `false`.
 *
 * @param elementsToRetain the elements to retain
 *
 * @return `true` if any element was removed from [this]
 *
 * @see Iterators.retainAll
 */
public inline fun <T> MutableIterator<T>.retainAll(elementsToRetain: Collection<T>): Boolean {
    return Iterators.retainAll(this, elementsToRetain)
}

/**
 * Determines whether two iterators contain equal elements in the same order. More specifically,
 * this method returns `true` if [this] and [other] contain the same
 * number of elements and every element of [this] is equal to the corresponding element
 * of [other].
 *
 * Note that this will modify the supplied iterators, since they will have been advanced some
 * number of elements forward.
 *
 * @see Iterators.elementsEqual
 */
public inline fun <T> Iterator<T>.elementsEqual(other: Iterator<T>): Boolean {
    return Iterators.elementsEqual(this, other)
}

/**
 * Returns a string representation of [this], with the format `[e1, e2, ..., en]`.
 * The iterator will be left exhausted: its [hasNext][Iterator.hasNext] method will return `false`.
 *
 * @see Iterators.toString
 */
public inline fun <T> Iterator<T>.asString(): String {
    return Iterators.toString(this)
}

/**
 * Copies an iterator's elements into an array.
 * The iterator will be left exhausted: its [hasNext][Iterator.hasNext] method will return `false`.
 *
 * @param T the type of the elements
 *
 * @return a newly-allocated array into which all the elements of the iterator have been copied
 *
 * @see Iterators.toArray
 */
public inline fun <reified T> Iterator<T>.toTypedArray(): Array<out T> {
    return Iterators.toArray(this, T::class.java) as Array<out T>
}

/**
 * Adds all elements in `iterator` to [this].
 * The iterator will be left exhausted: its [hasNext][Iterator.hasNext] method will return `false`.
 *
 * @return `true` if `collection` was modified as a result of this operation
 *
 * @see Iterators.addAll
 */
public inline fun <T> MutableCollection<T>.addAll(iterator: Iterator<T>): Boolean {
    return Iterators.addAll(this, iterator)
}

/**
 * Returns the number of elements in the specified iterator that equal the specified object.
 * The iterator will be left exhausted: its [hasNext][Iterator.hasNext] method will return `false`.
 *
 * @see Collections.frequency
 * @see Iterators.frequency
 */
public inline fun <T> Iterator<T>.frequency(element: T): Int {
    return Iterators.frequency(this, element)
}

/**
 * Returns an iterator that cycles indefinitely over the elements of [this].
 *
 * The iterator's [hasNext][Iterator.hasNext] method returns `true` until [this]
 * is empty.
 *
 * **Warning:** Typical uses of the resulting iterator may produce an infinite loop. You
 * should use an explicit `break` or be certain that you will eventually remove all the
 * elements.
 *
 * @see Iterators.cycle
 */
public inline fun <T> Iterable<T>.cycleIterator(): Iterator<T> {
    return Iterators.cycle(this)
}

/**
 * Returns an iterator that cycles indefinitely over the elements of [this].
 *
 * The returned iterator supports [remove][MutableIterator.remove] if the provided iterator does.
 * After [remove][MutableIterator.remove] is called, subsequent cycles omit the removed element,
 * which is no longer in [this].
 * The iterator's [hasNext][Iterator.hasNext] method returns `true` until [this]
 * is empty.
 *
 * **Warning:** Typical uses of the resulting iterator may produce an infinite loop. You
 * should use an explicit `break` or be certain that you will eventually remove all the
 * elements.
 *
 * @see Iterators.cycle
 */
@JvmName("cycleMutable")
public inline fun <T> MutableIterable<T>.cycleIterator(): MutableIterator<T> {
    return Iterators.cycle(this)
}

/**
 * Returns an iterator that cycles indefinitely over the provided elements.
 *
 *
 * The returned iterator supports [remove][MutableIterator.remove]. After
 * [remove][MutableIterator.remove] is called, subsequent cycles omit the removed element,
 * but `elements` does not change.
 * The iterator's [hasNext][Iterator.hasNext] method returns `true` until all of the original elements
 * have been removed.
 *
 *
 * **Warning:** Typical uses of the resulting iterator may produce an infinite loop. You
 * should use an explicit `break` or be certain that you will eventually remove all the
 * elements.
 *
 * @see Iterators.cycle
 */
public inline fun <T> cycleIterator(vararg elements: T): MutableIterator<T> {
    return Iterators.cycle(*elements)
}

/**
 * Combines two iterators into a single iterator. The returned iterator iterates across the
 * elements in [this], followed by the elements in [other]. The source iterators are not
 * polled until necessary.
 *
 * @see Iterators.concat
 */
public inline infix fun <T> Iterator<T>.concat(other: Iterator<T>): Iterator<T> {
    return Iterators.concat(this, other)
}

/**
 * Combines two iterators into a single iterator. The returned iterator iterates across the
 * elements in [this], followed by the elements in [other]. The source iterators are not
 * polled until necessary.
 *
 *
 * The returned iterator supports [remove][MutableIterator.remove] when the corresponding input iterator
 * supports it.
 *
 * @see Iterators.concat
 */
@JvmName("concatMutable")
public inline infix fun <T> MutableIterator<T>.concat(other: MutableIterator<T>): MutableIterator<T> {
    return Iterators.concat(this, other)
}

/**
 * Combines three iterators into a single iterator. The returned iterator iterates across the
 * elements in [this], followed by the elements in [a], followed by the elements in [b].
 * The source iterators are not polled until necessary.
 *
 * @see Iterators.concat
 */
public inline fun <T> Iterator<T>.concat(a: Iterator<T>, b: Iterator<T>): Iterator<T> {
    return Iterators.concat(this, a, b)
}

/**
 * Combines three iterators into a single iterator. The returned iterator iterates across the
 * elements in [this], followed by the elements in [a], followed by the elements in [b].
 * The source iterators are not polled until necessary.
 *
 *
 * The returned iterator supports [remove][MutableIterator.remove] when the corresponding input iterator
 * supports it.
 *
 * @see Iterators.concat
 */
@JvmName("concatMutable")
public inline fun <T> MutableIterator<T>.concat(a: MutableIterator<T>, b: MutableIterator<T>): MutableIterator<T> {
    return Iterators.concat(this, a, b)
}

/**
 * Combines two iterators into a single iterator. The returned iterator iterates across the
 * elements in [this], followed by the elements in [a], followed by the elements in [b],
 * followed by the elements in [c].
 * The source iterators are not polled until necessary.
 *
 * @see Iterators.concat
 */
public inline fun <T> Iterator<T>.concat(a: Iterator<T>, b: Iterator<T>, c: Iterator<T>): Iterator<T> {
    return Iterators.concat(this, a, b, c)
}

/**
 * Combines two iterators into a single iterator. The returned iterator iterates across the
 * elements in [this], followed by the elements in [a], followed by the elements in [b],
 * followed by the elements in [c].
 * The source iterators are not polled until necessary.
 *
 *
 * The returned iterator supports [remove][MutableIterator.remove] when the corresponding input iterator
 * supports it.
 *
 * @see Iterators.concat
 */
@JvmName("concatMutable")
public inline fun <T> MutableIterator<T>.concat(a: MutableIterator<T>, b: MutableIterator<T>, c: MutableIterator<T>): MutableIterator<T> {
    return Iterators.concat(this, a, b, c)
}

/**
 * Combines two iterators into a single iterator. The returned iterator iterates across the
 * elements in [this], followed by the elements in [iterators] sequentially.
 * The source iterators are not polled until necessary.
 *
 * @see Iterators.concat
 */
public inline fun <T> Iterator<T>.concat(vararg iterators: Iterator<T>): Iterator<T> {
    return Iterators.concat(this, *iterators)
}

/**
 * Combines two iterators into a single iterator. The returned iterator iterates across the
 * elements in [this], followed by the elements in [iterators] sequentially.
 * The source iterators are not polled until necessary.
 *
 * The returned iterator supports [remove][MutableIterator.remove] when the corresponding input iterator
 * supports it.
 *
 * @see Iterators.concat
 */
@JvmName("concatMutable")
public inline fun <T> MutableIterator<T>.concat(vararg iterators: MutableIterator<T>): MutableIterator<T> {
    return Iterators.concat(this, *iterators)
}

/**
 * Combines two iterators into a single iterator. The returned iterator iterates across the
 * elements in [this] sequentially.
 * The source iterators are not polled until necessary.
 *
 * @see Iterators.concat
 */
public inline fun <T> Iterator<Iterator<T>>.flatConcat(): Iterator<T> {
    return Iterators.concat(this)
}

/**
 * Combines two iterators into a single iterator. The returned iterator iterates across the
 * elements in [this] sequentially.
 * The source iterators are not polled until necessary.
 *
 * The returned iterator supports [remove][MutableIterator.remove] when the corresponding input iterator
 * supports it.
 *
 * @see Iterators.concat
 */
@JvmName("flatConcatMutable")
public inline fun <T> Iterator<MutableIterator<T>>.flatConcat(): MutableIterator<T> {
    return Iterators.concat(this)
}

/**
 * Combines two iterators into a single iterator. The returned iterator iterates across the
 * elements in [this] sequentially.
 * The source iterators are not polled until necessary.
 *
 * @see Iterators.concat
 */
public inline fun <T> Iterable<Iterator<T>>.flatConcat(): Iterator<T> {
    return Iterators.concat(this.iterator())
}

/**
 * Combines two iterators into a single iterator. The returned iterator iterates across the
 * elements in [this] sequentially.
 * The source iterators are not polled until necessary.
 *
 * The returned iterator supports [remove][MutableIterator.remove] when the corresponding input iterator
 * supports it.
 *
 * @see Iterators.concat
 */
@JvmName("flatConcatMutable")
public inline fun <T> Iterable<MutableIterator<T>>.flatConcat(): MutableIterator<T> {
    return Iterators.concat(this.iterator())
}

/**
 * Concats a varargs array of iterators without making a defensive copy of the array.
 *
 * @see Iterators.concatNoDefensiveCopy
 */
public inline fun <T> concatNoDefensiveCopy(vararg inputs: Iterator<T>): Iterator<T> {
    return Iterators.concat(*inputs)
}

/**
 * Divides an iterator into unmodifiable sublists of the given size (the final list may be
 * smaller). For example, partitioning an iterator containing `[a, b, c, d, e]` with a
 * partition size of 3 yields `[[a, b, c], [d, e]]` -- an outer iterator containing two
 * inner lists of three and two elements, all in the original order.
 *
 * The returned lists implement [RandomAccess].
 *
 * **Note:** The current implementation eagerly allocates storage for [size] elements.
 * As a consequence, passing values like [Int.MAX_VALUE] can lead to an [OutOfMemoryError].
 *
 * @param size The desired size of each partition (the last may be smaller)
 *
 * @return an iterator of immutable lists containing the elements of `iterator` divided into
 * partitions
 *
 * @throws IllegalArgumentException if [size] is non-positive
 *
 * @see Iterators.partition
 */
@Throws(IllegalArgumentException::class)
public inline fun <T> Iterator<T>.partition(size: Int): Iterator<List<T>> {
    return Iterators.partition(this, size)
}

/**
 * Divides an iterator into unmodifiable sublists of the given size, padding the final iterator
 * with null values if necessary. For example, partitioning an iterator containing `[a, b,
 * c, d, e]` with a partition size of 3 yields `[[a, b, c], [d, e, null]]` -- an outer
 * iterator containing two inner lists of three elements each, all in the original order.
 *
 * The returned lists implement [java.util.RandomAccess].
 *
 * **Note:** The current implementation eagerly allocates storage for [size] elements.
 * As a consequence, passing values like [Int.MAX_VALUE] can lead to an [OutOfMemoryError].
 *
 * @param size the desired size of each partition
 *
 * @return an iterator of immutable lists containing the elements of `iterator` divided into
 * partitions (the final iterable may have trailing null elements)
 *
 * @throws IllegalArgumentException if `size` is non-positive
 *
 * @see Iterators.paddedPartition
 */
@Throws(IllegalArgumentException::class)
public inline fun <T> Iterator<T>.paddedPartition(size: Int): Iterator<List<T?>> {
    return Iterators.paddedPartition(this, size)
}

/**
 * Returns a view of [this] containing all elements that satisfy the input predicate
 * [retainIfTrue].
 *
 * @see Iterators.filter
 */
public inline fun <T> Iterator<T>.filterView(noinline retainIfTrue: (T) -> Boolean): Iterator<T> {
    return Iterators.filter(this, retainIfTrue)
}

/**
 * Returns a view of [this] containing all elements that are of the type [T].
 *
 * @see Iterators.filter
 */
public inline fun <reified T> Iterator<*>.filterIsInstanceView(): Iterator<T> {
    return Iterators.filter(this, T::class.java)
}

/**
 * Returns `true` if one or more elements returned by [this] satisfy the given
 * [predicate].
 *
 * @see Iterators.any
 */
public inline fun <T> Iterator<T>.any(noinline predicate: (T) -> Boolean): Boolean {
    return Iterators.any(this, predicate)
}

/**
 * Returns `true` if every element returned by [this] satisfies the given
 * [predicate]. If [this] is empty, `true` is returned.
 *
 * @see Iterators.all
 */
public inline fun <T> Iterator<T>.all(noinline predicate: (T) -> Boolean): Boolean {
    return Iterators.all(this, predicate)
}

/**
 * Returns the first element in [this] that satisfies the given [predicate]; use this
 * method only when such an element is known to exist.
 * The iterator will be left exhausted: its [hasNext][Iterator.hasNext] method will return `false`.
 * If it is possible that *no* element will match, use [Iterator.tryFind] or [Iterator.find] instead.
 *
 * **Warning:** avoid using a [predicate] that matches `null`. If `null`
 * is matched in [this], a NullPointerException will be thrown.
 *
 * @throws NoSuchElementException if no element in [this] matches the given predicate and [defaultValue] is `null`.
 *
 * @see Iterators.find
 */
@Throws(NoSuchElementException::class)
public inline fun <T> Iterator<T>.find(defaultValue: T? = null, noinline predicate: (T) -> Boolean): T {
    return if (defaultValue == null) Iterators.find(this, predicate) else Iterators.find(this, predicate, defaultValue)!!
}

/**
 * Returns an [Optional] containing the first element in [this] that satisfies the
 * given predicate, if such an element exists.
 * If no such element is found, an empty [Optional] will be returned from this method and
 * the iterator will be left exhausted: its [hasNext][Iterator.hasNext] method will return `false`.
 *
 *
 * **Warning:** avoid using a [predicate] that matches `null`. If `null`
 * is matched in [this], a NullPointerException will be thrown.
 *
 * @see Iterators.tryFind
 */
public inline fun <T> Iterator<T>.tryFind(noinline predicate: (T) -> Boolean): Optional<T> {
    return Iterators.tryFind(this, predicate)
}

/**
 * Returns the index in [this] of the first element that satisfies the provided [predicate], or `-1` if the Iterator has no such elements.
 *
 * More formally, returns the lowest index `i` such that `predicate.apply(Iterators.get(iterator, i))` returns `true`, or `-1` if there is no
 * such index.
 *
 * If `-1` is returned, the iterator will be left exhausted: its [hasNext][Iterator.hasNext] method will
 * return `false`. Otherwise, the iterator will be set to the element which satisfies the
 * [predicate].
 *
 * @see Iterators.indexOf
 */
public inline fun <T> Iterator<T>.indexOf(noinline predicate: (T) -> Boolean): Int {
    return Iterators.indexOf(this, predicate)
}

/**
 * Returns a view containing the result of applying [function] to each element of [this].
 *
 * @see Iterators.transform
 */
public inline fun <F, T> Iterator<F>.transform(noinline function: (F) -> T): Iterator<T> {
    return Iterators.transform(this, function)
}

/**
 * Returns a view containing the result of applying [function] to each element of [this].
 *
 * The returned iterator supports [remove][MutableIterator.remove] if [this] does. After a
 * successful [remove][MutableIterator.remove] call, [this] no longer contains the corresponding
 * element.
 *
 * @see Iterators.transform
 */
@JvmName("transformMutable")
public inline fun <F, T> MutableIterator<F>.transform(noinline function: (F) -> T): MutableIterator<T> {
    return Iterators.transform(this, function)
}

/**
 * Advances [this]` `position + 1` times, returning the element at the [position]th position.
 *
 * @param position position of the element to return
 *
 * @return the element at the specified position in [this]
 *
 * @throws IndexOutOfBoundsException if [position] is negative or greater than or equal to
 * the number of elements remaining in [this].
 *
 * @see Iterators.get
 */
@Throws(IndexOutOfBoundsException::class)
public inline operator fun <T> Iterator<T>.get(position: Int): T {
    return Iterators.get(this, position)
}

/**
 * Advances [this]` `position + 1` times, returning the element at the [position]th position or a default value otherwise.
 *
 * @param position position of the element to return
 * @param defaultValue the default value to return if the iterator is empty or if [position]
 * is greater than the number of elements remaining in [this]
 *
 * @return the element at the specified position in [this], or [defaultValue] if [this]
 * produces fewer that `position + 1` elements and [defaultValue] is not `null`.
 *
 * @throws IndexOutOfBoundsException if [position] is negative, or if [position] is greater than or equal to
 * the number of elements remaining in [this] *and* [defaultValue] is `null`.
 *
 * @see Iterators.get
 */
@Throws(IndexOutOfBoundsException::class)
public inline fun <T> Iterator<T>.get(position: Int, defaultValue: T? = null): T {
    return if (defaultValue == null) Iterators.get(this, position) else Iterators.get(this, position, defaultValue)
}

/**
 * Returns the next element in [this] or [defaultValue] if the iterator is empty.
 * The [Iterables] analog to this method is [Iterables.getFirst].
 *
 * @param defaultValue the default value to return if the iterator is empty
 *
 * @return the next element of [this] or the default value
 *
 * @see Iterators.getNext
 */
public inline fun <T> Iterator<T>.getNext(defaultValue: T): T {
    return Iterators.getNext(this, defaultValue)
}

/**
 * Advances [this] to the end, returning the last element or [defaultValue] if the
 * iterator is empty and [defaultValue] is not `null`.
 *
 * @return the last element of [this]
 *
 * @throws NoSuchElementException if the iterator is empty and [defaultValue] is `null`.
 *
 * @see Iterators.getLast
 */
@Throws(NoSuchElementException::class)
public inline fun <T> Iterator<T>.getLast(defaultValue: T? = null): T {
    return if (defaultValue == null) Iterators.getLast(this) else Iterators.getLast(this, defaultValue)
}

/**
 * Calls [next][Iterator.next] on [this], either [numberToAdvance] times or until
 * [hasNext][Iterator.hasNext] returns `false`, whichever comes first.
 *
 * @return the number of elements the iterator was advanced
 *
 * @see Iterators.advance
 */
public inline fun <T> Iterator<T>.advance(numberToAdvance: Int): Int {
    return Iterators.advance(this, numberToAdvance)
}

/**
 * Returns a view containing the first [limitSize] elements of [this].
 * If [this] contains fewer than [limitSize] elements, the returned view contains all of its
 * elements.
 *
 * @param limitSize the maximum number of elements in the returned iterator
 *
 * @throws IllegalArgumentException if [limitSize] is negative
 *
 * @see Iterators.limit
 */
@Throws(IllegalArgumentException::class)
public inline fun <T> Iterator<T>.limit(limitSize: Int): Iterator<T> {
    return Iterators.limit(this, limitSize)
}

/**
 * Returns a view containing the first [limitSize] elements of [this].
 * If [this] contains fewer than [limitSize] elements, the returned view contains all of its
 * elements. The returned iterator supports `remove()` if `iterator` does.
 *
 * @param limitSize the maximum number of elements in the returned iterator
 *
 * @throws IllegalArgumentException if [limitSize] is negative
 *
 * @see Iterators.limit
 */
@JvmName("limitMutable")
@Throws(IllegalArgumentException::class)
public inline fun <T> MutableIterator<T>.limit(limitSize: Int): MutableIterator<T> {
    return Iterators.limit(this, limitSize)
}

/**
 * Returns a view of the supplied [this] that removes each element from the supplied
 * [this] as it is returned.
 *
 * The provided iterator must support [remove][MutableIterator.remove] or else the returned iterator
 * will fail on the first call to `next`.
 *
 * @return an iterator that removes and returns elements from the supplied iterator
 *
 * @see Iterators.consumingIterator
 */
public inline fun <T> MutableIterator<T>.consumingIterator(): Iterator<T> {
    return Iterators.consumingIterator(this)
}

/**
 * Returns an iterator containing the elements of [array] in order. The returned iterator is
 * a view of the array; subsequent changes to the array will be reflected in the iterator.
 *
 * **Note:** It is often preferable to represent your data using a collection type, for
 * example using [Arrays.asList], making this method unnecessary.
 *
 * The `Iterable` equivalent of this method is either [Arrays.asList],
 * [ImmutableList.copyOf]}, or [ImmutableList.of].
 *
 * @see Iterators.forArray
 */
public inline fun <T> forArray(vararg array: T): Iterator<T> {
    return Iterators.forArray(*array)
}

/**
 * Returns an iterator containing only [this].
 *
 * The [Iterable] equivalent of this method is [Collections.singleton].
 *
 * @see Iterators.singletonIterator
 */
public inline fun <T> T.singletonIterator(): Iterator<T> {
    return Iterators.singletonIterator(this)
}

/**
 * Adapts an [Enumeration] to the [Iterator] interface.
 *
 *
 * This method has no equivalent in [Iterables] because viewing an [Enumeration] as
 * an [Iterable] is impossible. However, the contents can be *copied* into a collection
 * using [Collections.list].
 *
 *
 * **Java 9 users:** use `enumeration.asIterator()` instead.
 *
 * @see Iterators.forEnumeration
 */
public fun <T> Enumeration<T>.forEnumeration(): Iterator<T> {
    return Iterators.forEnumeration(this)
}

/**
 * Adapts an [Iterator] to the [Enumeration] interface.
 *
 *
 * The [Iterable] equivalent of this method is either [Collections.enumeration] (if
 * you have a [Collection]), or `Iterators.asEnumeration(collection.iterator())`.
 *
 * @see Iterators.asEnumeration
 */
public fun <T> Iterator<T>.asEnumeration(): Enumeration<T> {
    return Iterators.asEnumeration(this)
}

/**
 * Returns a [PeekingIterator] backed by the given iterator.
 *
 *
 * Calls to the [peek][PeekingIterator.peek] method with no intervening calls to [Iterator.next] do not affect the
 * iteration, and hence return the same object each time. A subsequent call to [Iterator.next] is
 * guaranteed to return the same object again. For example:
 *
 * ```kotlin
 * val peekingIterator = Iterators.peekingIterator(Iterators.forArray("a", "b"));
 * val a1 = peekingIterator.peek() // returns "a"
 * val a2 = peekingIterator.peek() // also returns "a"
 * val a3 = peekingIterator.next() // also returns "a"
 * val a4 = peekingIterator.next() // returns "b"
 * ```
 *
 *
 *
 * Any structural changes to the underlying iteration (aside from those performed by the
 * iterator's own [PeekingIterator.remove] method) will leave the iterator in an undefined
 * state.
 *
 *
 * The returned iterator does not support removal after peeking, as explained by [PeekingIterator.remove].
 *
 *
 * Note: If the given iterator is already a [PeekingIterator], it *might* be
 * returned to the caller, although this is neither guaranteed to occur nor required to be
 * consistent. For example, this method *might* choose to pass through recognized
 * implementations of [PeekingIterator] when the behavior of the implementation is known to
 * meet the contract guaranteed by this method.
 *
 *
 * There is no [Iterable] equivalent to this method, so use this method to wrap each
 * individual iterator as it is generated.
 *
 * @receiver The backing iterator. The [PeekingIterator] assumes ownership of this
 * iterator, so users should cease making direct calls to it after calling this method.
 *
 * @return a peeking iterator backed by that iterator. Apart from the additional [PeekingIterator.peek] method,
 * this iterator behaves exactly the same as [this].
 *
 * @see Iterators.peekingIterator
 */
public fun <T> Iterator<T>.peekingIterator(): PeekingIterator<T> {
    return Iterators.peekingIterator(this)
}

/**
 * Returns an iterator over the merged contents of all given [this], traversing every
 * element of the input iterators. Equivalent entries will not be de-duplicated.
 *
 * Callers must ensure that the source [Iterator]s are in non-descending order as this
 * method does not sort its input.
 *
 * For any equivalent elements across all of [this], it is undefined which element is
 * returned first.
 */
@ExperimentalCollectionsApi
public inline fun <T, R : Comparable<R>> Iterable<Iterator<T>>.mergeSorted(crossinline selector: (T) -> Comparable<R>): Iterator<T> {
    return this.mergeSorted(compareBy(selector))
}

/**
 * Returns an iterator over the merged contents of all given [this], traversing every
 * element of the input iterators. Equivalent entries will not be de-duplicated.
 *
 * Callers must ensure that the source [Iterator]s are in non-descending order as this
 * method does not sort its input.
 *
 * For any equivalent elements across all of [this], it is undefined which element is
 * returned first.
 */
@Suppress("UnstableApiUsage")
@ExperimentalCollectionsApi
public inline fun <T> Iterable<Iterator<T>>.mergeSorted(comparator: Comparator<T>): Iterator<T> {
    return Iterators.mergeSorted(this, comparator)
}
