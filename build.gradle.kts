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

import ca.solostudios.dokkascript.plugin.DokkaScriptsConfiguration
import ca.solostudios.dokkascript.plugin.DokkaScriptsPlugin
import ca.solostudios.dokkastyles.plugin.DokkaStyleTweaksConfiguration
import ca.solostudios.dokkastyles.plugin.DokkaStyleTweaksPlugin
import ca.solostudios.nyx.util.soloStudios
import com.sass_lang.embedded_protocol.OutputStyle
import io.freefair.gradle.plugins.sass.SassCompile
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.plugins.ide.idea.model.IdeaProject
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.gradle.ext.ActionDelegationConfig
import org.jetbrains.gradle.ext.CodeStyleConfig
import org.jetbrains.gradle.ext.CopyrightConfiguration
import org.jetbrains.gradle.ext.GroovyCompilerConfiguration
import org.jetbrains.gradle.ext.IdeaCompilerConfiguration
import org.jetbrains.gradle.ext.ProjectSettings
import org.jetbrains.gradle.ext.RunConfiguration
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

    alias(libs.plugins.sass.base)

    alias(libs.plugins.nyx)

    idea
    alias(libs.plugins.idea.ext)
}

nyx {
    info {
        name = "guava-kotlin"
        group = "ca.solo-studios"
        version = Version("0", "1", "2").toString()
        description = """
            Guava Kotlin is a kotlin wrapper for guava to make it more idiomatic in kotlin.
        """.trimIndent()

        organizationName = "Solo Studios"
        organizationUrl = "https://solo-studios.ca/"

        developer {
            id = "solonovamax"
            name = "solonovamax"
            email = "solonovamax@12oclockpoint.com"
            url = "https://github.com/solonovamax"
        }

        repository.fromGithub("solo-studios", "guava-kotlin")

        license.useApachev2()
    }

    compile {
        jvmTarget = 8

        javadocJar = true
        sourcesJar = true

        allWarnings = true
        warningsAsErrors = true
        distributeLicense = true
        buildDependsOnJar = true
        reproducibleBuilds = true

        kotlin {
            withExplicitApi()

            apiVersion = "1.9"
            languageVersion = "1.9"

            compilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
        }
    }

    publishing {
        withSignedPublishing()

        repositories {
            maven {
                name = "SonatypeStaging"
                val repositoryId: String? by project
                url = when {
                    repositoryId != null -> uri("https://s01.oss.sonatype.org/service/local/staging/deployByRepositoryId/$repositoryId/")
                    else -> uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                }
                credentials(PasswordCredentials::class)
            }
            maven {
                name = "SoloStudiosReleases"
                url = uri("https://maven.solo-studios.ca/releases/")
                credentials(PasswordCredentials::class)
                authentication { // publishing doesn't work without this for some reason
                    create<BasicAuthentication>("basic")
                }
            }
            maven {
                name = "SoloStudiosSnapshots"
                url = uri("https://maven.solo-studios.ca/snapshots/")
                credentials(PasswordCredentials::class)
                authentication { // publishing doesn't work without this for some reason
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }
}

repositories {
    soloStudios()
    mavenCentral()
}

dependencies {
    api(libs.kotlin.stdlib)
    api(libs.guava)

    api(libs.kotlin.reflect)

    compileOnly(libs.bundles.kotlinx.coroutines)

    dokkaHtmlPlugin(libs.dokka.plugin.script)
    dokkaHtmlPlugin(libs.dokka.plugin.style.tweaks)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.bundles.junit)
}

sass {
    omitSourceMapUrl = true
    outputStyle = OutputStyle.COMPRESSED
    sourceMapContents = false
    sourceMapEmbed = false
    sourceMapEnabled = false
}

val dokkaDirs = DokkaDirectories(project)

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()

        failFast = false
        maxParallelForks = max(Runtime.getRuntime().availableProcessors() - 1, 1)
    }

    val processDokkaIncludes by registering(ProcessResources::class) {
        from(dokkaDirs.includes) {
            exclude { it.name.startsWith("_") }

            // Yes, these need to be done separately and the replace tokens needs to be first
            filter<ReplaceTokens>(
                "tokens" to mapOf("dependency" to dokkaDirs.readInclude("dependency")),
                "beginToken" to "{{",
                "endToken" to "}}"
            )

            expand(
                "project" to mapOf(
                    "group" to nyx.info.group,
                    "module" to nyx.info.module.get(),
                    "version" to nyx.info.version,
                )
            )
        }

        into(dokkaDirs.includesOutput)
        group = JavaBasePlugin.DOCUMENTATION_GROUP
    }

    val compileDokkaSass by register<SassCompile>("compileDokkaSass") {
        group = BasePlugin.BUILD_GROUP
        source = files(dokkaDirs.styles).asFileTree
        destinationDir = dokkaDirs.stylesOutput
    }

    withType<DokkaTask>().configureEach {
        dependsOn(compileDokkaSass, processDokkaIncludes)
        inputs.files(dokkaDirs.includesOutput, dokkaDirs.stylesOutput, dokkaDirs.templates, dokkaDirs.scripts)

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "© ${Year.now()} Copyright solo-studios"

            val compiledStyles = dokkaDirs.styles.walk().filter {
                it.isFile && it.extension == "scss" && !it.name.startsWith("_")
            }.map {
                dokkaDirs.stylesOutput.file("${it.nameWithoutExtension}.css").asFile
            }.toList()

            file("a").resolve("aaa")

            customStyleSheets = compiledStyles
            templatesDir = dokkaDirs.templates
        }

        pluginConfiguration<DokkaScriptsPlugin, DokkaScriptsConfiguration> {
            scripts = dokkaDirs.scripts.walk().filter { it.isFile }.toList()
        }

        pluginConfiguration<DokkaStyleTweaksPlugin, DokkaStyleTweaksConfiguration> {
            minimalScrollbar = true
            darkPurpleHighlight = true
            darkColorSchemeFix = true
            improvedBlockquoteBorder = true
            lighterBlockquoteText = true
            sectionTabFontWeight = "500"
            sectionTabTransition = true
            improvedSectionTabBorder = true
            disableCodeWrapping = true
            sidebarWidth = "340px"
        }

        dokkaSourceSets.configureEach {
            includes.from(dokkaDirs.includesOutput.asFileTree)

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

/**
 * Version class, which does version stuff.
 */
data class Version(val major: String, val minor: String, val patch: String, val snapshot: Boolean = false) {
    override fun toString(): String = if (!snapshot) "$major.$minor.$patch" else "$major.$minor.$patch-SNAPSHOT"
}

data class DokkaDirectories(val project: Project) {
    private val base = project.projectDir.resolve("dokka")
    private val baseOutput = project.layout.buildDirectory.dir("dokka").get()

    val includes = base.resolve("includes")
    val includesOutput = baseOutput.dir("includes")

    val styles = base.resolve("styles")
    val stylesOutput = baseOutput.dir("styles")

    val templates = base.resolve("templates")
    val scripts = base.resolve("scripts")

    fun readInclude(name: String) = includes.resolve("_$name.md").readText()
}

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
