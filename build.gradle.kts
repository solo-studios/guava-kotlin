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

@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

import org.gradle.plugins.ide.idea.model.IdeaProject
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin
import org.jetbrains.gradle.ext.ActionDelegationConfig
import org.jetbrains.gradle.ext.CodeStyleConfig
import org.jetbrains.gradle.ext.CopyrightConfiguration
import org.jetbrains.gradle.ext.GroovyCompilerConfiguration
import org.jetbrains.gradle.ext.IdeaCompilerConfiguration
import org.jetbrains.gradle.ext.ProjectSettings
import org.jetbrains.gradle.ext.RunConfiguration
import org.jetbrains.kotlin.incremental.cleanDirectoryContents
import java.net.URL
import java.time.Year
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
val versionObj = Version("0", "1", "2")
version = versionObj.toString()

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    target {
        compilations.configureEach {
            kotlinOptions {
                allWarningsAsErrors = true
                jvmTarget = "1.8"
                apiVersion = "1.6"
                languageVersion = "1.6"
                freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
            }
        }
    }
}

dependencies {
    api(libs.bundles.kotlin)
    api(libs.guava)

    api(libs.kotlin.reflect)

    compileOnly(libs.bundles.kotlinx.coroutines)

    testImplementation(libs.bundles.junit)
    testImplementation(kotlin("test"))

    dokkaPlugin(libs.dokka.versioning.plugin)
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
        metaInf {
            from(rootProject.file("LICENSE"))
        }
    }

    withType<DokkaTask>().configureEach {
        dependsOn(processDokkaIncludes)

        var outputDirectory by outputDirectory
        val docsVersionsDir = outputDirectory.resolve("versions")
        val docsCurrentVersionDir = docsVersionsDir.resolve(version.toString())
        val docsOlderVersions = docsVersionsDir.listFiles { file: File ->
            file != docsCurrentVersionDir
        }?.toList()?.takeIf { isCI }.orEmpty()
        val renderedDocsDir = buildDir.resolve("docs").resolve(outputDirectory.name)


        outputDirectory = docsCurrentVersionDir

        docsOlderVersions.forEach {
            inputs.dir(it)
        }
        outputs.dirs(renderedDocsDir, docsCurrentVersionDir)


        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "© ${Year.now()} Copyright solo-studios"
        }

        pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
            version = project.version.toString()
            olderVersions = docsOlderVersions
        }

        doFirst {
            docsCurrentVersionDir.cleanDirectoryContents()
        }

        doLast {
            renderedDocsDir.deleteRecursively()
            docsCurrentVersionDir.copyRecursively(renderedDocsDir)
            docsCurrentVersionDir.resolve("older").deleteRecursively()
        }

        dokkaSourceSets.configureEach {
            includes.from(processDokkaIncludes.destinationDir.listFiles())

            jdkVersion.set(8)
            reportUndocumented.set(true)

            // Documentation link
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(URL("https://github.com/solo-studios/guava-kotlin/tree/master/src/main/kotlin"))
                remoteLineSuffix.set("#L")
            }

            val guavaVersion = libs.guava.get().versionConstraint.requiredVersion.ifEmpty { "snapshot" }
            val guavaDocsUrl = "https://guava.dev/releases/$guavaVersion/api/docs/"

            externalDocumentationLink(guavaDocsUrl, "$guavaDocsUrl/element-list")
        }

        group = JavaBasePlugin.DOCUMENTATION_GROUP
    }
}

val processDokkaIncludes by tasks.register("processDokkaIncludes", ProcessResources::class) {
    from(projectDir.resolve("dokka/includes")) {
        val projectInfo = ProjectInfo(project.group.toString(), project.name, versionObj)
        filesMatching("Module.md") {
            expand("project" to projectInfo)
        }
    }
    destinationDir = buildDir.resolve("dokka-include")
    group = JavaBasePlugin.DOCUMENTATION_GROUP
}

val dokkaHtml by tasks.named<DokkaTask>("dokkaHtml")
val jar by tasks.named<Jar>("jar")

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    from(dokkaHtml.outputDirectory)
    archiveClassifier.set("javadoc")
    group = JavaBasePlugin.DOCUMENTATION_GROUP
}

val sourcesJar by tasks.registering(Jar::class) {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
    group = JavaBasePlugin.DOCUMENTATION_GROUP
}

artifacts {
    archives(jar)
    archives(sourcesJar)
    archives(javadocJar)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(jar)
            artifact(sourcesJar)
            artifact(javadocJar)

            version = version.toString()
            groupId = group.toString()
            artifactId = "guava-kotlin"

            pom {
                name.set("Guava Kotlin")
                description.set(
                    "Guava Kotlin is a set of extensions and other utilities to Google's Guava library, " +
                            "to provide a more idiomatic way to use it in Kotlin."
                )
                description.set("A set of Kotlin extensions for the Guava library.")
                url.set("https://github.com/solo-studios/guava-kotlin")

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
                    url.set("https://github.com/solo-studios/guava-kotlin/issues")
                }
                scm {
                    connection.set("scm:git:https://github.com/solo-studios/guava-kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com/solo-studios/guava-kotlin.git")
                    url.set("https://github.com/solo-studios/guava-kotlin/")
                }
            }
        }
    }

    repositories {
        maven {
            name = "Sonatype"

            val repositoryId: String? by project
            url = when {
                isSnapshot -> uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                repositoryId != null -> uri("https://s01.oss.sonatype.org/service/local/staging/deployByRepositoryId/$repositoryId/")
                else -> uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }

            credentials(PasswordCredentials::class)
        }
        maven {
            name = "SoloStudios"

            val releasesUrl = uri("https://maven.solo-studios.ca/releases/")
            val snapshotUrl = uri("https://maven.solo-studios.ca/snapshots/")
            url = if (isSnapshot) snapshotUrl else releasesUrl

            credentials(PasswordCredentials::class)
            authentication { // publishing doesn't work without this for some reason
                create<BasicAuthentication>("basic")
            }
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
data class Version(val major: String, val minor: String, val patch: String, val snapshot: Boolean = false) {
    override fun toString(): String = if (!snapshot) "$major.$minor.$patch" else "$major.$minor.$patch-SNAPSHOT"
}

/**
 * Project info class for [processDokkaIncludes].
 */
data class ProjectInfo(val group: String, val module: String, val version: Version)

/*-----------------------*
 | BEGIN IntelliJ Config |
 *-----------------------*/

idea {
    project {
        settings {
            copyright {
                profiles {
                    val copyright = create("guava-kotlin") {
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
                        // language=RegExp
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

val Project.isSnapshot: Boolean
    get() = version.toString().endsWith("-SNAPSHOT")

val isCI: Boolean
    get() = System.getenv("CI") != null

fun IdeaProject.settings(configuration: ProjectSettings.() -> Unit) {
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
