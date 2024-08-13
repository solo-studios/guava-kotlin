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

package ca.solostudios.guava.kotlin.reflect

import com.google.common.reflect.Reflection
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

/**
 * Returns the package name of `this` according to the Java Language
 * Specification (section 6.7). Unlike [Class.getPackage], this method only
 * parses the class name, without attempting to define the [Package] and
 * hence load files.
 *
 * @see Reflection.getPackageName
 */
public val KClass<*>.packageName: String?
    get() = Reflection.getPackageName(this.java)

/**
 * Returns the package name of `classFullName` according to the Java
 * Language Specification (section 6.7). Unlike [Class.getPackage], this
 * method only parses the class name, without attempting to define the
 * [Package] and hence load files.
 *
 * @see Reflection.getPackageName
 */
public fun getPackageName(classFullName: String): String {
    return Reflection.getPackageName(classFullName)
}

/**
 * Ensures that the given classes are initialized, as described in
 * [JLS Section 12.4.2](http://java.sun.com/docs/books/jls/third_edition/html/execution.html#12.4.2).
 *
 * WARNING: Normally it's a smell if a class needs to be explicitly
 * initialized, because static state hurts system maintainability and
 * testability. In cases when you have no choice while interoperating
 * with a legacy framework, this method helps to keep the code less ugly.
 *
 * @throws ExceptionInInitializerError if an exception is thrown during
 *         initialization of a class
 * @see Reflection.initialize
 */
@JvmName("initializeKClassArray")
@Throws(ExceptionInInitializerError::class)
public fun initialize(vararg classes: KClass<*>) {
    Reflection.initialize(*classes.map { it.java }.toTypedArray())
}

/**
 * Ensures that the given classes are initialized, as described in
 * [JLS Section 12.4.2](http://java.sun.com/docs/books/jls/third_edition/html/execution.html#12.4.2).
 *
 * WARNING: Normally it's a smell if a class needs to be explicitly
 * initialized, because static state hurts system maintainability and
 * testability. In cases when you have no choice while interoperating
 * with a legacy framework, this method helps to keep the code less ugly.
 *
 * @throws ExceptionInInitializerError if an exception is thrown during
 *         initialization of a class
 * @see Reflection.initialize
 */
@JvmName("initializeClassArray")
@Throws(ExceptionInInitializerError::class)
public fun initialize(vararg classes: Class<*>) {
    Reflection.initialize(*classes)
}

/**
 * Ensures that the given classes are initialized, as described in
 * [JLS Section 12.4.2](http://java.sun.com/docs/books/jls/third_edition/html/execution.html#12.4.2).
 *
 * WARNING: Normally it's a smell if a class needs to be explicitly
 * initialized, because static state hurts system maintainability and
 * testability. In cases when you have no choice while interoperating
 * with a legacy framework, this method helps to keep the code less ugly.
 *
 * @throws ExceptionInInitializerError if an exception is thrown during
 *         initialization of a class
 * @see Reflection.initialize
 */
@JvmName("initializeKClassList")
@Throws(ExceptionInInitializerError::class)
public fun initialize(classes: List<KClass<*>>) {
    Reflection.initialize(*classes.map { it.java }.toTypedArray())
}

/**
 * Ensures that the given classes are initialized, as described in
 * [JLS Section 12.4.2](http://java.sun.com/docs/books/jls/third_edition/html/execution.html#12.4.2).
 *
 * WARNING: Normally it's a smell if a class needs to be explicitly
 * initialized, because static state hurts system maintainability and
 * testability. In cases when you have no choice while interoperating
 * with a legacy framework, this method helps to keep the code less ugly.
 *
 * @throws ExceptionInInitializerError if an exception is thrown during
 *         initialization of a class
 * @see Reflection.initialize
 */
@JvmName("initializeClassList")
@Throws(ExceptionInInitializerError::class)
public fun initialize(classes: List<Class<*>>) {
    Reflection.initialize(*classes.toTypedArray())
}

/**
 * Returns a proxy instance that implements [T] by dispatching method
 * invocations to [handler]. The class loader of [T] will be used to define
 * the proxy class. To implement multiple interfaces or specify a class
 * loader, use [Proxy.newProxyInstance].
 *
 * @throws IllegalArgumentException if [T] does not specify the type of a
 *         Java interface
 * @see Reflection.newProxy
 */
@JvmName("newProxyJava")
@Throws(IllegalArgumentException::class)
public inline fun <reified T> newProxy(noinline handler: (proxy: Any, method: Method, args: Array<Any>) -> Any): T {
    return Reflection.newProxy(T::class.java, handler)
}
