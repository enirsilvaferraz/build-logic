package com.eferraz.buildlogic.ext

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun KotlinMultiplatformExtension.configureDesktopTarget() {
    jvm()
}

internal fun KotlinMultiplatformExtension.configureAndroidTarget() {
    // Verifica se o target Android já existe (pode ter sido criado por outro plugin como Room)
//    val existingTarget = targets.findByName("android")
//    if (existingTarget == null) {
//        // Target não existe, cria novo
//        androidTarget {
//            compilerOptions {
//                jvmTarget.set(JvmTarget.JVM_11)
//            }
//        }
//    }
    // Se o target já existe, não faz nada - ele já foi configurado pelo plugin que o criou
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