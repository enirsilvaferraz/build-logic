import com.eferraz.buildlogic.CatalogDefinitions
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.COMPOSE_COMPILER
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.COMPOSE_MULTIPLATFORM
import com.eferraz.buildlogic.ext.bundle
import com.eferraz.buildlogic.ext.libs
import com.eferraz.buildlogic.ext.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class LibraryComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugin(COMPOSE_MULTIPLATFORM))
            apply(plugin = libs.plugin(COMPOSE_COMPILER))
//            apply(plugin = libs.plugin(COMPOSE_HOT_RELOAD))

            extensions.configure<KotlinMultiplatformExtension> {

                sourceSets {

                    commonMain.dependencies {
                        implementation(libs.bundle(CatalogDefinitions.Bundles.COMPOSE_COMMON))
                    }
                }
            }
        }
    }
}