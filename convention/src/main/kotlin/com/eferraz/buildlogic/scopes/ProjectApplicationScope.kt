package com.eferraz.buildlogic.scopes

import com.android.build.api.dsl.ApplicationExtension
import com.eferraz.buildlogic.CatalogDefinitions.Versions.COMPILE_SDK
import com.eferraz.buildlogic.CatalogDefinitions.Versions.MIN_SDK
import com.eferraz.buildlogic.CatalogDefinitions.Versions.TARGET_SDK
import com.eferraz.buildlogic.ext.libs
import com.eferraz.buildlogic.ext.version
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

object ProjectApplicationScope {
    var namespace: String? = null
    var versionName: String? = null
    var versionCode: Int? = null
}

private fun Project.configureApplication(
    namespaceParam: String,
    versionNameParam: String,
    versionCodeParam: Int,
) {

    extensions.configure<ApplicationExtension> {

        namespace = namespaceParam
        compileSdk = libs.version(COMPILE_SDK)

        defaultConfig {
            applicationId = namespaceParam
            minSdk = libs.version(MIN_SDK)
            targetSdk = libs.version(TARGET_SDK)
            versionName = versionNameParam
            versionCode = versionCodeParam
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    extensions.configure<ComposeExtension> {
        this.extensions.configure<DesktopExtension> {
            application {
                mainClass = "$namespaceParam.MainKt"

                nativeDistributions {
                    targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
                    packageName = namespaceParam
                    packageVersion = versionNameParam
                }
            }
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