import androidx.room.gradle.RoomExtension
import com.eferraz.buildlogic.CatalogDefinitions.Bundles.ROOM_COMMON
import com.eferraz.buildlogic.CatalogDefinitions.Bundles.ROOM_COMMON_COMPILER
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.KSP
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.ROOM
import com.eferraz.buildlogic.bundle
import com.eferraz.buildlogic.libs
import com.eferraz.buildlogic.plugin
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

            apply(plugin = libs.plugin(KSP))
            apply(plugin = libs.plugin(ROOM))

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets {
                    commonMain {
                        dependencies {
                            implementation(libs.bundle(ROOM_COMMON))
                        }
                    }
                }
            }

            dependencies {
                listOf(
                    "kspAndroid",
                    "kspIosSimulatorArm64",
                    "kspIosX64",
                    "kspIosArm64"
                ).forEach {
                    add(it, libs.bundle(ROOM_COMMON_COMPILER))
                }
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }
        }
    }
}