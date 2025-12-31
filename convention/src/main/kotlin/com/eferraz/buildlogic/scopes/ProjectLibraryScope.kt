package com.eferraz.buildlogic.scopes

import com.eferraz.buildlogic.CatalogDefinitions.Versions.COMPILE_SDK
import com.eferraz.buildlogic.CatalogDefinitions.Versions.MIN_SDK
import com.eferraz.buildlogic.ext.libs
import com.eferraz.buildlogic.ext.version
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

object ProjectLibraryScope {
    var namespace: String? = null
}

private fun Project.configureLibrary(namespaceParam: String) {

//    extensions.configure<KotlinMultiplatformExtension> {
//        // Verifica se o target Android já existe (pode ter sido criado por outro plugin como Room)
//        val existingTarget = targets.findByName("android")
//        if (existingTarget == null) {
//            // Target não existe, cria novo (sem configurar namespace/compileSdk aqui)
//            androidTarget {
//                // namespace e compileSdk serão configurados via kotlin.android no build.gradle.kts
//            }
//        }
//        // Se o target já existe, não faz nada - ele já foi configurado pelo plugin que o criou
//        // O namespace e compileSdk devem ser configurados via kotlin.android no build.gradle.kts
//    }
}

fun Project.library(scope: ProjectLibraryScope.() -> Unit) {

    with(ProjectLibraryScope) { scope() }

    configureLibrary(
        ProjectLibraryScope.namespace ?: throw IllegalStateException("Namespace is required")
    )
}