import com.eferraz.buildlogic.ext.libs
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType

internal class FoundationDetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugins.detekt.get().pluginId)

            extensions.configure<DetektExtension>("detekt") {

                toolVersion.set(libs.versions.detekt.get())

                buildUponDefaultConfig.set(true)
                allRules.set(false)

                baseline.set(layout.projectDirectory.file("analysis/detekt-baseline.xml"))

                source.setFrom(
                    "src/commonMain/kotlin",
                    "src/androidMain/kotlin",
                    "src/iosMain/kotlin",
                    "src/jvmMain/kotlin",
                    "src/desktopMain/kotlin",
                    "src/main/kotlin",
                )

                config.setFrom(
                    rootProject.layout.projectDirectory.file("build-logic/analysis/detekt/detekt.yml"),
                    rootProject.layout.projectDirectory.file("build-logic/analysis/detekt/detekt-compose.yml")
                )
            }

            dependencies.add("detektPlugins", libs.detekt.rules.ktlint.wrapper)
            dependencies.add("detektPlugins", libs.nlopez.compose.rules.detekt)
            dependencies.add("detektPlugins", libs.nlopez.compose.rules.ktlint)

            tasks.withType<Detekt>().configureEach {
                jvmTarget.set("21")
                reports {
                    checkstyle.required.set(true)
                    html.required.set(true)
                    sarif.required.set(true)
                    markdown.required.set(true)
                }
            }
        }
    }
}
