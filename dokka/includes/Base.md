# Package ca.solostudios.guava.kotlin.base

This package contains extensions to Guava's base module.

## [ToStringHelper][ca.solostudios.guava.kotlin.base.toStringHelper]

[Guava `ToStringHelper`][com.google.common.base.MoreObjects.ToStringHelper]

Adds several extension functions for [`ToStringHelper`][com.google.common.base.MoreObjects.ToStringHelper],
to make it more idiomatic in kotlin.

See the Guava User Guide on
[writing `Object` methods with [com.google.common.base.Objects]](https://github.com/google/guava/wiki/CommonObjectUtilitiesExplained)

## [Splitter as a `Flow`][ca.solostudios.guava.kotlin.base.splitToFlow]

Extension function for [`Splitter`][com.google.common.base.Splitter] that allows it to be consumed as a flow.

To use this, you must include `kotlinx-coroutines-core` and `kotlinx-coroutines-jdk8` as dependencies.

## Throwables

[Guava `Throwables`][com.google.common.base.Throwables]

Adds several extension functions to [Throwable] that delegate to [Guava's `Throwables`][com.google.common.base.Throwables]
