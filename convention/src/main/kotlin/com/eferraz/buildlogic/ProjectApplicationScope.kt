package com.eferraz.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

object ProjectApplicationScope {

    var namespace: String? = null
    var versionName: String? = null
    var versionCode: Int? = null
}

internal fun Project.configureApplication(namespaceParam: String, versionNameParam: String, versionCodeParam: Int) {
    extensions.configure<ApplicationExtension> {
        namespace = namespaceParam
        defaultConfig {
            applicationId = namespaceParam
            versionName = versionNameParam
            versionCode = versionCodeParam
        }
    }
}

fun Project.application(scope: ProjectApplicationScope.() -> Unit) {

    with(ProjectApplicationScope) { scope() }

    configureApplication(
        ProjectApplicationScope.namespace ?: throw IllegalStateException("Namespace is required"),
        ProjectApplicationScope.versionName ?: throw IllegalStateException("Version name is required"),
        ProjectApplicationScope.versionCode ?: throw IllegalStateException("Version code is required")
    )
}