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

package ca.solostudios.guava.kotlin.annotations

import com.google.common.annotations.Beta
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.LOCAL_VARIABLE
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.annotation.AnnotationTarget.TYPEALIAS
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER


/**
 * This annotation marks the collections API that is considered experimental,
 * and that the public API (public classes, methods, or fields) is subject to incompatible changes,
 * or even removal, in a future release.
 *
 * Any public collections API annotated with this is either experimental, or depends on a guava api annotated with [Beta].
 *
 * Any usage of a declaration annotated with `@ExperimentalCollectionsApi` must be either accepted by
 * annotating that usage with the [OptIn] annotation, e.g. `@OptIn(ExperiemntalCollectionsApi::class)`,
 * or by using the compiler argument `-opt-in=ca.solostudios.guava.kotlin.annotations.ExperimentalCollectionsApi`
 *
 * @see Beta
 *
 * @constructor Create empty Experimental collections api
 */
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
@Retention(AnnotationRetention.BINARY)
@Target(
        CLASS,
        ANNOTATION_CLASS,
        PROPERTY,
        FIELD,
        LOCAL_VARIABLE,
        VALUE_PARAMETER,
        CONSTRUCTOR,
        FUNCTION,
        PROPERTY_GETTER,
        PROPERTY_SETTER,
        TYPEALIAS
       )
@MustBeDocumented
public annotation class ExperimentalCollectionsApi
