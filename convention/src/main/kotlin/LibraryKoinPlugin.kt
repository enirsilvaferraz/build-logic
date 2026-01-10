import com.android.build.gradle.internal.utils.isComposeCompilerPluginApplied
import com.eferraz.buildlogic.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class LibraryKoinPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugins.ksp.get().pluginId)

            extensions.configure<KotlinMultiplatformExtension> {

                sourceSets {

                    commonMain {

                        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

                        dependencies {

                            implementation(project.dependencies.platform(libs.koin.bom))
                            implementation(libs.bundles.koin.common)

                            if (isComposeCompilerPluginApplied(project))
                                implementation(libs.bundles.koin.common.compose)
                        }
                    }

                    commonTest.dependencies {
                        implementation(libs.bundles.koin.common.test)
                    }
                }
            }

            dependencies {
                add("kspCommonMainMetadata", libs.bundles.koin.common.compiler)
            }

            // Issue fix https://github.com/InsertKoinIO/koin/issues/2174
            tasks.named { it.startsWith("ksp") && it != "kspCommonMainKotlinMetadata" }.configureEach {
                dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
            }
        }
    }
}