import com.eferraz.buildlogic.AbstractKmpProjectPlugin
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.ANDROID_APPLICATION
import com.eferraz.buildlogic.configureAndroidTarget
import com.eferraz.buildlogic.configureDesktopTarget
import com.eferraz.buildlogic.configureIOSTarget
import com.eferraz.buildlogic.libs
import com.eferraz.buildlogic.plugin
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KmpApplicationPlugin : AbstractKmpProjectPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        with(target) {

            apply(plugin = libs.plugin(ANDROID_APPLICATION))

            extensions.configure<KotlinMultiplatformExtension> {
                configureAndroidTarget()
                configureIOSTarget(project.name.capitalized())
                configureDesktopTarget()
            }
        }
    }
}