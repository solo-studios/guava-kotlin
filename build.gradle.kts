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
import ca.solostudios.nyx.util.reposiliteMaven
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
import java.io.Serializable
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

    alias(libs.plugins.nyx)

    alias(libs.plugins.axion.release)
    alias(libs.plugins.sass.base)
}

nyx {
    info {
        name = "Guava Kotlin"
        module = "guava-kotlin"
        group = "ca.solo-studios"
        version = scmVersion.version
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
            reposiliteMaven {
                name = "SoloStudiosReleases"
                url = uri("https://maven.solo-studios.ca/releases/")
                credentials(PasswordCredentials::class)
            }
            reposiliteMaven {
                name = "SoloStudiosSnapshots"
                url = uri("https://maven.solo-studios.ca/snapshots/")
                credentials(PasswordCredentials::class)
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
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        description = "Processes the included dokka files"

        val projectInfo = ProjectInfo(nyx.info.group, nyx.info.module.get(), nyx.info.version)
        inputs.property("projectInfo", projectInfo)

        from(dokkaDirs.includes) {
            exclude { it.name.startsWith("_") }

            // Yes, these need to be done separately and the replace tokens needs to be first
            filter<ReplaceTokens>(
                "tokens" to mapOf("dependency" to dokkaDirs.readInclude("dependency")),
                "beginToken" to "{{",
                "endToken" to "}}"
            )

            expand("project" to projectInfo)
        }

        into(dokkaDirs.includesOutput)
    }

    val compileDokkaSass by register<SassCompile>("compileDokkaSass") {
        group = BasePlugin.BUILD_GROUP
        source = files(dokkaDirs.styles).asFileTree
        destinationDir = dokkaDirs.stylesOutput
    }

    withType<DokkaTask>().configureEach {
        group = JavaBasePlugin.DOCUMENTATION_GROUP

        dependsOn(compileDokkaSass, processDokkaIncludes)
        inputs.files(dokkaDirs.includesOutput, dokkaDirs.stylesOutput, dokkaDirs.templates, dokkaDirs.scripts)

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "© ${Year.now()} Copyright solo-studios"

            val compiledStyles = dokkaDirs.styles.walk().filter {
                it.isFile && it.extension == "scss" && !it.name.startsWith("_")
            }.map {
                dokkaDirs.stylesOutput.file("${it.nameWithoutExtension}.css").asFile
            }.toList()

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
                localDirectory = projectDir.resolve("src")
                remoteUrl = nyx.info.repository.projectUrl.map { uri("$it/tree/master/src").toURL() }
                remoteLineSuffix = "#L"
            }

            val guavaVersion = libs.guava.get().versionConstraint.requiredVersion.ifEmpty { "snapshot" }
            val guavaDocsUrl = "https://guava.dev/releases/$guavaVersion/api/docs/"

            externalDocumentationLink(guavaDocsUrl, "$guavaDocsUrl/element-list")
        }
    }
}

data class ProjectInfo(val group: String, val module: String, val version: String) : Serializable

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
