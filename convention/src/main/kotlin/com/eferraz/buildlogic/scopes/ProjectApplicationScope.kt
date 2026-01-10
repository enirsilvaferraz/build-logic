package com.eferraz.buildlogic.scopes

import com.android.build.api.dsl.ApplicationExtension
import com.eferraz.buildlogic.ext.libs
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
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        defaultConfig {
            applicationId = namespaceParam
            minSdk = libs.versions.android.minSdk.get().toInt()
            targetSdk = libs.versions.android.targetSdk.get().toInt()
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
                    jvmArgs("-Dapple.awt.application.appearance=system")
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