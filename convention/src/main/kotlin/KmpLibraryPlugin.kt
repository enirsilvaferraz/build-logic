import com.eferraz.buildlogic.AbstractKmpProjectPlugin
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.ANDROID_LIBRARY
import com.eferraz.buildlogic.ext.configureAndroidTarget
import com.eferraz.buildlogic.ext.configureDesktopTarget
import com.eferraz.buildlogic.ext.configureIOSTarget
import com.eferraz.buildlogic.ext.libs
import com.eferraz.buildlogic.ext.plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KmpLibraryPlugin : AbstractKmpProjectPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        with(target) {

            apply(plugin = libs.plugin(ANDROID_LIBRARY))

            extensions.configure<KotlinMultiplatformExtension> {
                configureAndroidTarget()
                configureIOSTarget()
                configureDesktopTarget()
            }
        }
    }
}