import com.eferraz.buildlogic.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KmpProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugins.kotlin.multiplatform.get().pluginId)
            apply(plugin = libs.plugins.kotlin.serialization.get().pluginId)
            apply(plugin = libs.plugins.multiplatform.library.get().pluginId)

            extensions.configure<KotlinMultiplatformExtension> {

                explicitApi()

                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }

                jvm()

                listOf(
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach { iosTarget ->
                    project.name.capitalized().let {
                        iosTarget.binaries.framework {
                            baseName = it
                        }
                    }
                }

                sourceSets {

                    commonMain.dependencies {
                        implementation(libs.bundles.kotlin.common)
                    }

                    commonTest.dependencies {
                        implementation(libs.bundles.kotlin.common.test)
                    }
                }
            }
        }
    }
}