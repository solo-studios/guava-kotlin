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

rootProject.name = "guava-kotlin"

pluginManagement {
    repositories {
        maven("https://maven.solo-studios.ca/releases/")
        mavenCentral()
        gradlePluginPortal()
    }
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.dokka:dokka-core:1.9.20")
        classpath("org.jetbrains.dokka:dokka-base:1.9.20")

        classpath("ca.solo-studios:dokka-script-plugin:0.1.1")
        classpath("ca.solo-studios:dokka-style-tweaks-plugin:1.1.0")
    }
}
