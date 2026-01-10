import com.eferraz.buildlogic.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class LibraryKtorPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugins.kotlin.serialization.get().pluginId)

            extensions.configure<KotlinMultiplatformExtension> {

                sourceSets {

                    commonMain.dependencies {
                        implementation(libs.bundles.ktor.common)
                    }

                    androidMain.dependencies {
                        implementation(libs.bundles.ktor.android)
                    }

                    iosMain.dependencies {
                        implementation(libs.bundles.ktor.ios)
                    }

                    jvmMain.dependencies {
                        implementation(libs.bundles.ktor.desktop)
                    }
                }
            }
        }
    }
}