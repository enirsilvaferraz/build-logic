import com.eferraz.buildlogic.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class LibraryComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugins.compose.multiplatform.get().pluginId)
            apply(plugin = libs.plugins.compose.compiler.get().pluginId)
            apply(plugin = libs.plugins.compose.hot.reload.get().pluginId)

            extensions.configure<KotlinMultiplatformExtension> {

                sourceSets {

                    commonMain.dependencies {
                        implementation(libs.bundles.compose.common)
                    }
                }
            }
        }
    }
}