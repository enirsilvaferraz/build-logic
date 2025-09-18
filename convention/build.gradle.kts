plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.gradle.plugin.android)
    compileOnly(libs.gradle.plugin.compose)
    compileOnly(libs.gradle.plugin.kotlin)
    compileOnly(libs.gradle.plugin.room)
}

gradlePlugin {

    plugins {

        libs.plugins.foundation.project.application.get().pluginId.let {
            register(it) {
                id = it
                implementationClass = "KmpApplicationPlugin"
            }
        }

        libs.plugins.foundation.project.library.get().pluginId.let {
            register(it) {
                id = it
                implementationClass = "KmpLibraryPlugin"
            }
        }

        libs.plugins.foundation.library.compose.get().pluginId.let {
            register(it) {
                id = it
                implementationClass = "LibraryComposePlugin"
            }
        }

        libs.plugins.foundation.library.navigation.get().pluginId.let {
            register(it) {
                id = it
                implementationClass = "LibraryNavigationPlugin"
            }
        }

        libs.plugins.foundation.library.koin.get().pluginId.let {
            register(it) {
                id = it
                implementationClass = "LibraryKoinPlugin"
            }
        }

        libs.plugins.foundation.library.room.get().pluginId.let {
            register(it) {
                id = it
                implementationClass = "LibraryRoomPlugin"
            }
        }

        libs.plugins.foundation.library.ktor.get().pluginId.let {
            register(it) {
                id = it
                implementationClass = "LibraryKtorPlugin"
            }
        }
    }
}
