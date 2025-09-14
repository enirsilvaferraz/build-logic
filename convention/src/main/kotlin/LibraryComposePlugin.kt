import com.eferraz.buildlogic.CatalogDefinitions.Libraries.ACTIVITY_COMPOSE
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.COMPOSE_COMPILER
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.COMPOSE_MULTIPLATFORM
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.KOTLIN_MULTIPLATFORM
import com.eferraz.buildlogic.library
import com.eferraz.buildlogic.libs
import com.eferraz.buildlogic.plugin
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

            apply(plugin = libs.plugin(KOTLIN_MULTIPLATFORM))
            apply(plugin = libs.plugin(COMPOSE_MULTIPLATFORM))
            apply(plugin = libs.plugin(COMPOSE_COMPILER))

            val compose = extensions.getByType<ComposeExtension>().dependencies

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets {
                    commonMain.dependencies {
                        implementation(compose.runtime)
                        implementation(compose.foundation)
                        implementation(compose.material3)
                        implementation(compose.materialIconsExtended)
                        implementation(compose.ui)
                        implementation(compose.uiUtil)
                        implementation(compose.components.resources)
                        implementation(compose.components.uiToolingPreview)
                    }

                    commonTest.dependencies {

                        @OptIn(ExperimentalComposeLibrary::class)
                        implementation(compose.uiTest)
                    }

                    androidMain.dependencies {
                        implementation(libs.library(ACTIVITY_COMPOSE))
                    }
                }
            }
        }
    }
}