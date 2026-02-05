import com.android.build.gradle.internal.utils.isComposeCompilerPluginApplied
import com.eferraz.buildlogic.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class LibraryKoinPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugins.koin.compiler.get().pluginId)

            extensions.configure<KotlinMultiplatformExtension> {

                sourceSets {

                    commonMain.dependencies {

                        implementation(project.dependencies.platform(libs.koin.bom))
                        implementation(libs.bundles.koin.common)

                        if (isComposeCompilerPluginApplied(project))
                            implementation(libs.bundles.koin.common.compose)
                    }

                    commonTest.dependencies {
                        implementation(libs.bundles.koin.common.test)
                    }
                }
            }
        }
    }
}