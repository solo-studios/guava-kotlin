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

@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.gradle.ext.ActionDelegationConfig
import org.jetbrains.gradle.ext.CodeStyleConfig
import org.jetbrains.gradle.ext.CopyrightConfiguration
import org.jetbrains.gradle.ext.GroovyCompilerConfiguration
import org.jetbrains.gradle.ext.IdeaCompilerConfiguration
import org.jetbrains.gradle.ext.ProjectSettings
import org.jetbrains.gradle.ext.RunConfiguration
import kotlin.math.max

plugins {
    java
    signing
    `java-library`
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    
    alias(libs.plugins.dokka)
    
    idea
    alias(libs.plugins.idea.ext)
}

group = "ca.solo-studios"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    target {
        compilations.configureEach {
            kotlinOptions {
                jvmTarget = "1.8"
                apiVersion = "1.5"
                languageVersion = "1.5"
            }
        }
    }
}

dependencies {
    api(libs.bundles.kotlin)
    api(libs.guava)
    
    testImplementation(libs.bundles.junit)
    testImplementation(kotlin("test"))
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        
        failFast = false
        maxParallelForks = max(Runtime.getRuntime().availableProcessors() - 1, 1)
    }
    
    withType<Javadoc>().configureEach {
        options {
            encoding = "UTF-8"
        }
    }
    
    withType<Jar>().configureEach {
        from(rootProject.file("LICENSE"))
    }
}

val dokkaHtml by tasks.getting(DokkaTask::class)

val javadoc by tasks.getting(Javadoc::class)

val jar by tasks.getting(Jar::class)

val javadocJar by tasks.creating(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(sourcesJar)
            artifact(javadocJar)
            artifact(jar)
            
            version = version as String
            groupId = group as String
            artifactId = "guava-kotlin-extensions"
            
            pom {
                name.set("Guava Kotlin Extensions")
                description.set("A set of Kotlin extensions for the Guava library.")
                url.set("https://github.com/solo-studios/guava-kotlin-extensions")
                
                inceptionYear.set("2022")
                
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("solonovamax")
                        name.set("solonovamax")
                        email.set("solonovamax@12oclockpoint.com")
                        url.set("https://github.com/solonovamax")
                    }
                }
                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/solo-studios/guava-kotlin-extensions/issues")
                }
                scm {
                    connection.set("scm:git:https://github.com/solo-studios/guava-kotlin-extensions.git")
                    developerConnection.set("scm:git:ssh://github.com/solo-studios/guava-kotlin-extensions.git")
                    url.set("https://github.com/solo-studios/guava-kotlin-extensions/")
                }
            }
        }
    }
    
    repositories {
        maven {
            name = "sonatypeStaging"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials(org.gradle.api.credentials.PasswordCredentials::class)
        }
        maven {
            name = "sonatypeSnapshot"
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            credentials(PasswordCredentials::class)
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])
}

/**
 * Version class, which does version stuff.
 */
data class Version(val major: String, val minor: String, val patch: String) {
    override fun toString(): String = "$major.$minor.$patch"
}

/*-----------------------*
 | BEGIN IntelliJ Config |
 *-----------------------*/

idea {
    project {
        settings {
            copyright {
                profiles {
                    val copyright = create("PolyBot") {
                        notice = """
                            |Copyright (c) ${"$"}originalComment.match("Copyright \(c\) (\d+)", 1, "-")${"$"}today.year solonovamax <solonovamax@12oclockpoint.com>
                            |
                            |Licensed under the Apache License, Version 2.0 (the "License");
                            |you may not use this file except in compliance with the License.
                            |You may obtain a copy of the License at
                            |
                            |    https://www.apache.org/licenses/LICENSE-2.0
                            |
                            |Unless required by applicable law or agreed to in writing, software
                            |distributed under the License is distributed on an "AS IS" BASIS,
                            |WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                            |See the License for the specific language governing permissions and
                            |limitations under the License.
                        """.trimMargin()
                        keyword = "Copyright"
                        //language=RegExp
                        allowReplaceRegexp = "20[0-9]{2}"
                    }
                    useDefault = copyright.name
                    
                    scopes = mapOf(
                            "Project Files" to copyright.name
                                  )
                }
            }
        }
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

/*---------------------*
 | END IntelliJ Config |
 *---------------------*/

/*-----------------------*
 | BEGIN Utility Methods |
 *-----------------------*/

fun org.gradle.plugins.ide.idea.model.IdeaProject.settings(configuration: ProjectSettings.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.delegateActions(configuration: ActionDelegationConfig.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.taskTriggers(configuration: IdeaCompilerConfiguration.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.compiler(configuration: IdeaCompilerConfiguration.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.groovyCompiler(configuration: GroovyCompilerConfiguration.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.codeStyle(configuration: CodeStyleConfig.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.copyright(configuration: CopyrightConfiguration.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.encodings(configuration: org.jetbrains.gradle.ext.EncodingConfiguration.() -> Unit) {
    (this as ExtensionAware).configure(configuration)
}

fun ProjectSettings.runConfigurations(configuration: PolymorphicDomainObjectContainer<RunConfiguration>.() -> Unit) {
    (this as ExtensionAware).configure<NamedDomainObjectContainer<RunConfiguration>> {
        (this as PolymorphicDomainObjectContainer<RunConfiguration>).apply(configuration)
    }
}

/*---------------------*
 | END Utility Methods |
 *---------------------*/
