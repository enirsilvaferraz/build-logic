package com.eferraz.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.plugin(plugin: CatalogDefinitions.Plugins) = findPlugin(plugin.alias).get().get().pluginId
internal fun VersionCatalog.bundle(bundle: CatalogDefinitions.Bundles) = findBundle(bundle.alias).get()
internal fun VersionCatalog.version(version: CatalogDefinitions.Versions) = findVersion(version.alias).get().requiredVersion.toInt()
internal fun VersionCatalog.library(library: CatalogDefinitions.Libraries) = findLibrary(library.alias).get()