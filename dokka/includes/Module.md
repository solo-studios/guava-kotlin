# Module guava-kotlin

Guava Kotlin is a library wrapper around Guava providing more features that are more idiomatic to Kotlin.

## About

Guava Kotlin is a library that complements [Guava](https://github.com/google/guava/),
by providing a more idiomatic way to interact with it in Kotlin.

Guava Kotlin is a bridge between Guava and common Kotlin language features and idioms.

Please see the subpackages for further documentation on how it wraps Guava.

## Setup with Gradle

### Gradle Kotlin DSL

```kotlin
implementation("${project.group}:${project.module}:${project.version}")
```

### Gradle Groovy DSL

```groovy
implementation '${project.group}:${project.module}:${project.version}'
```

### Maven `pom.xml`

```xml
<dependency>
  <groupId>${project.group}</groupId>
  <artifactId>${project.module}</artifactId>
  <version>${project.version}</version>
</dependency>
```
