import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.eferraz.buildlogic.ext.libs
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal class KmpProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugins.kotlin.multiplatform.get().pluginId)
            apply(plugin = libs.plugins.kotlin.serialization.get().pluginId)
            apply(plugin = libs.plugins.multiplatform.library.get().pluginId)
            apply(plugin = libs.plugins.foundation.detekt.get().pluginId)

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }
            }

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

                android {
                    // project.name pode conter '-' (ex.: :design-system); identificadores Java não.
                    namespace = "com.eferraz.${project.name.replace('-', '_')}"
                    compileSdk = libs.versions.android.compileSdk.get().toInt()
                    androidResources.enable = true
                }

                sourceSets {

                    commonMain.dependencies {
                        implementation(libs.bundles.kotlin.common)
                    }

                    commonTest.dependencies {
                        implementation(libs.bundles.kotlin.common.test)
                    }

                    jvmTest.dependencies {
                        implementation(libs.bundles.kotlin.jvm.test)
                    }
                }
            }
        }
    }

    fun KotlinMultiplatformExtension.android(configure: Action<KotlinMultiplatformAndroidLibraryTarget>): Unit =
        (this as ExtensionAware).extensions.configure("android", configure)
}