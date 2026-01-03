import com.eferraz.buildlogic.CatalogDefinitions.Bundles.KOTLIN_COMMON
import com.eferraz.buildlogic.CatalogDefinitions.Bundles.KOTLIN_COMMON_TEST
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.KOTLIN_MULTIPLATFORM
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.KOTLIN_SERIALIZATION
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.MULTIPLATFORM_LIBRARY
import com.eferraz.buildlogic.ext.bundle
import com.eferraz.buildlogic.ext.libs
import com.eferraz.buildlogic.ext.plugin
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

            apply(plugin = libs.plugin(KOTLIN_MULTIPLATFORM))
            apply(plugin = libs.plugin(KOTLIN_SERIALIZATION))
            apply(plugin = libs.plugin(MULTIPLATFORM_LIBRARY))

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
                        implementation(libs.bundle(KOTLIN_COMMON))
                    }

                    commonTest.dependencies {
                        implementation(libs.bundle(KOTLIN_COMMON_TEST))
                    }
                }
            }
        }
    }
}