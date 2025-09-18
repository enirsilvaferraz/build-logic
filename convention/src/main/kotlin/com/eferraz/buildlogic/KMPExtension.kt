package com.eferraz.buildlogic

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun KotlinMultiplatformExtension.configureDesktopTarget() {
    jvm("desktop")
}

internal fun KotlinMultiplatformExtension.configureAndroidTarget() {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
}

internal fun KotlinMultiplatformExtension.configureIOSTarget(name: String? = null) {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        name?.let {
            iosTarget.binaries.framework {
                baseName = it
            }
        }
    }
}