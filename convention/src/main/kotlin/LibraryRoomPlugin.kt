import androidx.room.gradle.RoomExtension
import com.eferraz.buildlogic.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class LibraryRoomPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugins.ksp.get().pluginId)
            apply(plugin = libs.plugins.room.get().pluginId)

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets {
                    commonMain.dependencies {
                        implementation(libs.bundles.room.common)
                    }
                }
            }

            dependencies {
                listOf(
                    "kspAndroid",
                    "kspIosSimulatorArm64",
                    "kspJvm",
                    "kspIosArm64"
                ).forEach {
                    add(it, libs.bundles.room.common.compiler)
                }
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }
        }
    }
}