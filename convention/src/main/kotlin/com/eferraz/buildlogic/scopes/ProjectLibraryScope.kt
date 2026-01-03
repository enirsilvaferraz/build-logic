package com.eferraz.buildlogic.scopes

import org.gradle.api.Project

object ProjectLibraryScope {
    var namespace: String? = null
}

private fun Project.configureLibrary(namespaceParam: String) {

}

fun Project.library(scope: ProjectLibraryScope.() -> Unit) {

    with(ProjectLibraryScope) { scope() }

    configureLibrary(
        ProjectLibraryScope.namespace ?: throw IllegalStateException("Namespace is required")
    )
}