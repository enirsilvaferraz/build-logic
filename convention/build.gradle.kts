import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf

plugins {
    `kotlin-dsl`
    base
}

dependencies {
    compileOnly(libs.gradle.plugin.android)
    compileOnly(libs.gradle.plugin.compose)
    compileOnly(libs.gradle.plugin.kotlin)
    compileOnly(libs.gradle.plugin.room)

    // Permite que o plugin acesse as classes geradas do 'libs'
    compileOnly(files(gradle.serviceOf<DependenciesAccessors>().classes.asFiles))
}

gradlePlugin {

    plugins {

        libs.plugins.foundation.project.get().pluginId.let {
            register(it) {
                id = it
                implementationClass = "KmpProjectPlugin"
            }
        }

        libs.plugins.foundation.library.comp.get().pluginId.let {
            register(it) {
                id = it
                implementationClass = "LibraryComposePlugin"
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
