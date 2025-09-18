package com.eferraz.buildlogic

import com.eferraz.buildlogic.CatalogDefinitions.Libraries.KOTLIN_TEST
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.KOTLIN_MULTIPLATFORM
import com.eferraz.buildlogic.ext.library
import com.eferraz.buildlogic.ext.libs
import com.eferraz.buildlogic.ext.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
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
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }

                sourceSets {
                    commonTest.dependencies {
                        implementation(libs.library(KOTLIN_TEST))
                    }
                }
            }
        }
    }
}