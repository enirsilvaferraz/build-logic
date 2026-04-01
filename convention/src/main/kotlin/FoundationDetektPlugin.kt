import com.eferraz.buildlogic.ext.libs
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

/**
 * Aplica o Detekt e a configuração partilhada do repositório (YAML em /config/detekt).
 * Em módulos Android Application, fixa o JVM target da task Detekt e liga [check] ao [detekt].
 */
internal class FoundationDetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.plugins.detekt.get().pluginId)

            extensions.configure<DetektExtension>("detekt") {
                toolVersion = libs.versions.detekt.get()
                config.setFrom(rootProject.layout.projectDirectory.file("build-logic/analysis/detekt/detekt.yml"))
                buildUponDefaultConfig = true
                allRules = false
                source.setFrom(
                    "src/commonMain/kotlin",
                    "src/androidMain/kotlin",
                    "src/iosMain/kotlin",
                    "src/jvmMain/kotlin",
                    "src/desktopMain/kotlin",
                    "src/main/kotlin",
                )
            }

            val formattingDep = "io.gitlab.arturbosch.detekt:detekt-formatting:${libs.versions.detekt.get()}"
            dependencies.add("detektPlugins", formattingDep)

            tasks.withType<Detekt>().configureEach {
                jvmTarget = "21"
                reports {
                    html.required.set(true)
                    sarif.required.set(true)
                }
            }

            tasks.matching { it.name == "check" }.configureEach {
                dependsOn(tasks.named("detekt"))
            }
        }
    }
}
