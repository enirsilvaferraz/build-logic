package com.eferraz.buildlogic

import com.android.build.api.dsl.LibraryExtension
import com.eferraz.buildlogic.CatalogDefinitions.Versions.COMPILE_SDK
import com.eferraz.buildlogic.CatalogDefinitions.Versions.MIN_SDK
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

object ProjectLibraryScope {
    var namespace: String? = null
}

internal fun Project.configureLibrary(namespaceParam: String) {

    extensions.configure<LibraryExtension> {

        namespace = namespaceParam
        compileSdk = libs.version(COMPILE_SDK)

        defaultConfig {
            minSdk = libs.version(MIN_SDK)
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }
}

fun Project.androidLibrary(scope: ProjectLibraryScope.() -> Unit) {

    with(ProjectLibraryScope) { scope() }

    configureLibrary(
        ProjectLibraryScope.namespace ?: throw IllegalStateException("Namespace is required")
    )
}