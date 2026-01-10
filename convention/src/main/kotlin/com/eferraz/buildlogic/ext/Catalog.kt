package com.eferraz.buildlogic.ext

import org.gradle.accessors.dm.LibrariesForLibs

import org.gradle.api.Project

val Project.libs: LibrariesForLibs
    get() = rootProject.extensions.getByName("libs") as LibrariesForLibs
