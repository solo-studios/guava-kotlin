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

import com.google.common.collect.Multiset as GuavaMultiset

internal abstract class AbstractGuavaMultisetWrapper<out E> : Multiset<E> {
    
    internal abstract val guavaMultiset: GuavaMultiset<@UnsafeVariance E>
    
    override val size: Int
        get() = guavaMultiset.size
    
    override fun isEmpty(): Boolean = guavaMultiset.isEmpty()
    
    override val elementSet: Set<E>
        get() = guavaMultiset.elementSet()
    
    override val entrySet: Set<GuavaMultiset.Entry<@UnsafeVariance E>>
        get() = guavaMultiset.entrySet()
    
    override fun count(element: @UnsafeVariance E): Int = guavaMultiset.count(element)
    
    override fun iterator(): Iterator<E> = guavaMultiset.iterator()
    
    override fun containsAll(elements: Collection<@UnsafeVariance E>): Boolean = guavaMultiset.containsAll(elements)
    
    override fun contains(element: @UnsafeVariance E): Boolean = guavaMultiset.contains(element)
}

internal abstract class AbstractMutableGuavaMultisetWrapper<E> : MutableMultiset<E>, AbstractGuavaMultisetWrapper<E>() {
    abstract override val guavaMultiset: GuavaMultiset<E>
    
    override fun clear() = guavaMultiset.clear()
    
    override fun retainAll(elements: Collection<E>): Boolean = guavaMultiset.retainAll(elements.toSet())
    
    override fun removeAll(elements: Collection<E>): Boolean = guavaMultiset.removeAll(elements.toSet())
    
    override fun add(element: E): Boolean = guavaMultiset.add(element)
    
    override fun remove(element: E): Boolean = guavaMultiset.remove(element)
    
    override fun add(element: E, occurrences: Int): Int = guavaMultiset.add(element, occurrences)
    
    override fun remove(element: E, occurrences: Int): Int = guavaMultiset.remove(element, occurrences)
    
    override fun setCount(element: E, count: Int): Int = guavaMultiset.setCount(element, count)
    
    override fun setCount(element: E, oldCount: Int, newCount: Int): Boolean = guavaMultiset.setCount(element, oldCount, newCount)
    
    override fun iterator(): MutableIterator<E> = guavaMultiset.iterator()
    
    override fun addAll(elements: Collection<E>): Boolean = guavaMultiset.addAll(elements)
}
