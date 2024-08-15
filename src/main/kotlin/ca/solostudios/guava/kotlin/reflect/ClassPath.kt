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

import com.google.common.reflect.ClassPath
import java.io.IOException
import java.net.URLClassLoader

/**
 * Returns a [ClassPath] representing all classes and resources loadable
 * from `this` and its ancestor class loaders.
 *
 * **Warning:** [ClassPath] can find classes and resources only from:
 * - [URLClassLoader] instances' `file:` URLs
 * - the [system class loader][ClassLoader.getSystemClassLoader]. To search
 *   the system class loader even when it is not a [URLClassLoader] (as in
 *   Java 9), [ClassPath] searches the files from the `java.class.path`
 *   system property.
 *
 * @throws IOException if the attempt to read class path resources (jar
 *         files or directories) failed.
 * @see ClassPath.from
 */
@Throws(IOException::class)
public fun ClassLoader.asClassPath(): ClassPath {
    return ClassPath.from(this)
}

/**
 * Returns a [ClassPath] representing all classes and resources loadable
 * from `this` and its ancestor class loaders.
 *
 * **Warning:** [ClassPath] can find classes and resources only from:
 * - [URLClassLoader] instances' `file:` URLs
 * - the [system class loader][ClassLoader.getSystemClassLoader]. To search
 *   the system class loader even when it is not a [URLClassLoader] (as in
 *   Java 9), [ClassPath] searches the files from the `java.class.path`
 *   system property.
 *
 * @throws IOException if the attempt to read class path resources (jar
 *         files or directories) failed.
 * @see ClassPath.from
 */
@get:Throws(IOException::class)
public val ClassLoader.classPath: ClassPath
    get() = ClassPath.from(this)
