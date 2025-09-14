package com.eferraz.buildlogic

import com.eferraz.buildlogic.CatalogDefinitions.Libraries.KOTLIN_STDLIB
import com.eferraz.buildlogic.CatalogDefinitions.Libraries.KOTLIN_TEST
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.KOTLIN_MULTIPLATFORM
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal abstract class AbstractKmpProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugin(KOTLIN_MULTIPLATFORM))

            extensions.configure<KotlinMultiplatformExtension> {

                explicitApi()

                compilerOptions {
                    // Common compiler options applied to all Kotlin source sets
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }

                listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = project.name.capitalized()
                    }
                }

                sourceSets {

                    commonMain {
                        dependencies {
                            implementation(libs.library(KOTLIN_STDLIB))
                        }
                    }

                    commonTest {
                        dependencies {
                            implementation(libs.library(KOTLIN_TEST))
                        }
                    }
                }
            }
        }
    }
}