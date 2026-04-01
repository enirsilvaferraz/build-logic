import com.eferraz.buildlogic.ext.libs
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
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
                toolVersion.set(libs.versions.detekt.get())
                config.setFrom(rootProject.layout.projectDirectory.file("build-logic/analysis/detekt/detekt.yml"))
                buildUponDefaultConfig.set(true)
                allRules.set(false)
                source.setFrom(
                    "src/commonMain/kotlin",
                    "src/androidMain/kotlin",
                    "src/iosMain/kotlin",
                    "src/jvmMain/kotlin",
                    "src/desktopMain/kotlin",
                    "src/main/kotlin",
                )
            }

            val formattingDep = "dev.detekt:detekt-rules-ktlint-wrapper:${libs.versions.detekt.get()}"
            dependencies.add("detektPlugins", formattingDep)

            tasks.withType<Detekt>().configureEach {
                jvmTarget.set("21")
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
