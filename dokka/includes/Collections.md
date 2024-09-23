# Package ca.solostudios.guava.kotlin.collect

This package contains Guava Collections extensions, as well as collections wrappers to provide typed mutability.

## Collection Types

The following is a list of guava's collection types, as well as their mutably-typed replacement.

### [`BiMap`][com.google.common.collect.BiMap]

[Guava `BiMap`][com.google.common.collect.BiMap]

An extension of [`Map`][kotlin.collections.Map] that guarantees the uniqueness of its values as well as that of its keys.
This is sometimes called an "invertible map," since the restriction on values enables it to support
an [inverse view](https://guava.dev/releases/31.1-jre/api/docs/com/google/common/collect/BiMap.html#inverse())
-- which is another instance of BiMap.

- [`BiMap`][ca.solostudios.guava.kotlin.collect.BiMap]: A read-only bimap.
- [`MutableBiMap`][ca.solostudios.guava.kotlin.collect.MutableBiMap]: A bimap that supports mutability.

### [`Multiset`][ca.solostudios.guava.kotlin.collect.Multiset]

[Guava `Multiset`][com.google.common.collect.Multiset]

An extension of [`Collection`][kotlin.collections.Collection] that may contain duplicate values like a [`List`][kotlin.collections.List],
yet has
order-independent equality like a [`Set`][kotlin.collections.Set].
One typical use for a multiset is to represent a histogram.

- [`Multiset`][ca.solostudios.guava.kotlin.collect.Multiset]: A read-only multiset.
- [`MutableMultiset`][ca.solostudios.guava.kotlin.collect.MutableMultiset]: A multiset that supports mutability.

### [`Multimap`][ca.solostudios.guava.kotlin.collect.Multimap]

[Guava `Multimap`][com.google.common.collect.Multimap]

A new type, which is similar to [`Map`][kotlin.collections.Map], but may contain multiple entries with the same key.
Some behaviors of [`Multimap`][ca.solostudios.guava.kotlin.collect.Multimap] are left unspecified and are provided only by the subtypes
mentioned below.

- [`Multimap`][ca.solostudios.guava.kotlin.collect.Multimap]: A read-only multimap.
- [`MutableMultimap`][ca.solostudios.guava.kotlin.collect.MutableMultiset]: A multimap that supports mutability.

### [`ListMultimap`][ca.solostudios.guava.kotlin.collect.ListMultimap]

[Guava `ListMultimap`][com.google.common.collect.ListMultimap]

An extension of [`Multimap`][ca.solostudios.guava.kotlin.collect.Multimap] which permits duplicate entries, supports random access of
values for a particular key, and has partially order-dependent equality as defined by `ListMultimap.equals(Object)`. `ListMultimap` takes
its name from the fact that the [collection of values][ca.solostudios.guava.kotlin.collect.ListMultimap.get] associated with a given key
fulfills the [`List`][kotlin.collections.List] contract.

- [`ListMultimap`][ca.solostudios.guava.kotlin.collect.ListMultimap]: A read-only list multimap.
- [`MutableListMultimap`][ca.solostudios.guava.kotlin.collect.MutableListMultimap]: A list multimap that supports mutability.

### [`SetMultimap`][ca.solostudios.guava.kotlin.collect.SetMultimap]

[Guava `SetMultimap`][com.google.common.collect.SetMultimap]

An extension of [`Multimap`][ca.solostudios.guava.kotlin.collect.Multimap] which has order-independent equality and does not allow duplicate
entries; that is, while a key may appear twice in a `SetMultimap`, each must map to a different value. `SetMultimap` takes its name from
the fact that the [collection of values][ca.solostudios.guava.kotlin.collect.SetMultimap.get] associated with a given key fulfills the
[`Set`][kotlin.collections.Set] contract.

- [`SetMultimap`][ca.solostudios.guava.kotlin.collect.SetMultimap]: A read-only set multimap.
- [`MutableSetMultimap`][ca.solostudios.guava.kotlin.collect.MutableSetMultimap]: A set multimap that supports mutability.

### [`SortedSetMultimap`][com.google.common.collect.SortedSetMultimap]

[Guava `SortedSetMultimap`][com.google.common.collect.SortedSetMultimap]

An extension of [`SetMultimap`][ca.solostudios.guava.kotlin.collect.SetMultimap] for which
the [collection values][ca.solostudios.guava.kotlin.collect.SetMultimap.get] associated with a
given key
is a [SortedSet][java.util.SortedSet]. 
