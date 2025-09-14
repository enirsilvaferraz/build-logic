import com.android.build.api.dsl.androidLibrary
import com.eferraz.buildlogic.AbstractKmpProjectPlugin
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.KOTLIN_MULTIPLATFORM_LIBRARY
import com.eferraz.buildlogic.CatalogDefinitions.Versions.MIN_SDK
import com.eferraz.buildlogic.CatalogDefinitions.Versions.TARGET_SDK
import com.eferraz.buildlogic.libs
import com.eferraz.buildlogic.plugin
import com.eferraz.buildlogic.version
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KmpLibraryPlugin : AbstractKmpProjectPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        with(target) {

            apply(plugin = libs.plugin(KOTLIN_MULTIPLATFORM_LIBRARY))

            extensions.configure<KotlinMultiplatformExtension> {

                @Suppress("UnstableApiUsage")
                androidLibrary {
                    // namespace = // Aplicado posteriormente
                    compileSdk = libs.version(TARGET_SDK)
                    minSdk = libs.version(MIN_SDK)
                }
            }
        }
    }
}