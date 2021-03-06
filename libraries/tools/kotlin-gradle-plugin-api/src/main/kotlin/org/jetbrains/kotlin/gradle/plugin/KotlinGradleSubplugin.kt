/*
 * Copyright 2010-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.gradle.plugin

import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import java.io.File

open class SubpluginOption(val key: String, val value: String)

class FilesSubpluginOption(
        key: String,
        val files: List<File>,
        val kind: FilesOptionKind = FilesOptionKind.INTERNAL,
        value: String = files.joinToString(File.pathSeparator) { it.canonicalPath })
    : SubpluginOption(key, value)

class CompositeSubpluginOption(
        key: String,
        value: String,
        val originalOptions: List<SubpluginOption>)
    : SubpluginOption(key, value)

/** Defines how the files option should be handled with regard to Gradle model */
enum class FilesOptionKind {
    /** The files option is an implementation detail and should not be treated as an input or an output.  */
    INTERNAL

    // More options might be added when use cases appear for them,
    // such as output directories, inputs or classpath options.
}

/** Defines a subplugin option that should be excluded from Gradle input/output checks */
open class InternalSubpluginOption(key: String, value: String) : SubpluginOption(key, value)

interface KotlinGradleSubplugin<in KotlinCompile : AbstractCompile> {
    fun isApplicable(project: Project, task: AbstractCompile): Boolean

    fun apply(
        project: Project,
        kotlinCompile: KotlinCompile,
        javaCompile: AbstractCompile?,
        variantData: Any?,
        androidProjectHandler: Any?,
        kotlinCompilation: KotlinCompilation?
    ): List<SubpluginOption>

    fun getSubpluginKotlinTasks(
            project: Project,
            kotlinCompile: KotlinCompile
    ): List<AbstractCompile> = emptyList()

    fun getCompilerPluginId(): String

    fun getPluginArtifact(): SubpluginArtifact
}

open class SubpluginArtifact(val groupId: String, val artifactId: String, val version: String? = null)

class JetBrainsSubpluginArtifact(artifactId: String) : SubpluginArtifact(groupId = "org.jetbrains.kotlin", artifactId = artifactId)
