package com.eferraz.buildlogic

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

object ProjectLibraryScope {
    var namespace: String? = null
}

internal fun Project.configureLibrary(namespaceParam: String) {
    extensions.configure<KotlinMultiplatformExtension> {
        @Suppress("UnstableApiUsage")
        androidLibrary {
            namespace = "${namespaceParam}.${project.name}"
        }
    }
}

fun Project.library(scope: ProjectLibraryScope.() -> Unit) {

    with(ProjectLibraryScope) { scope() }

    configureLibrary(
        ProjectLibraryScope.namespace ?: throw IllegalStateException("Namespace is required")
    )
}