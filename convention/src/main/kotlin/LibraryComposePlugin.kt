import com.eferraz.buildlogic.CatalogDefinitions
import com.eferraz.buildlogic.CatalogDefinitions.Libraries.ACTIVITY_COMPOSE
import com.eferraz.buildlogic.CatalogDefinitions.Libraries.COMPOSE_WINDOW_SUITE
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.COMPOSE_COMPILER
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.COMPOSE_HOT_RELOAD
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.COMPOSE_MULTIPLATFORM
import com.eferraz.buildlogic.ext.library
import com.eferraz.buildlogic.ext.libs
import com.eferraz.buildlogic.ext.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class LibraryComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugin(COMPOSE_MULTIPLATFORM))
            apply(plugin = libs.plugin(COMPOSE_COMPILER))
            apply(plugin = libs.plugin(COMPOSE_HOT_RELOAD))

            val compose = extensions.getByType<ComposeExtension>().dependencies

            extensions.configure<KotlinMultiplatformExtension> {

                sourceSets {

                    commonMain.dependencies {
                        implementation(compose.runtime)
                        implementation(compose.foundation)
                        implementation(compose.material3)
                        implementation(compose.material3AdaptiveNavigationSuite)
                        implementation(compose.materialIconsExtended)
                        implementation(compose.ui)
                        implementation(compose.uiUtil)
                        implementation(compose.components.resources)
                        implementation(compose.components.uiToolingPreview)
                        implementation(libs.library(COMPOSE_WINDOW_SUITE))
                    }

                    commonTest.dependencies {
                        @OptIn(ExperimentalComposeLibrary::class)
                        implementation(compose.uiTest)
                    }

                    androidMain.dependencies {
                        implementation(libs.library(ACTIVITY_COMPOSE))
                    }

                    jvmMain.dependencies {
                        implementation(compose.desktop.currentOs)
                    }
                }
            }
        }
    }
}