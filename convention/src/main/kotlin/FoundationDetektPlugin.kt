import com.android.build.gradle.internal.utils.isComposeCompilerPluginApplied
import com.eferraz.buildlogic.ext.libs
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

/**
 * Aplica o Detekt e a configuração partilhada do repositório ([detekt.yml] em build-logic/analysis/detekt).
 * Regras específicas de Compose ficam em [LibraryComposePlugin].
 */
internal class FoundationDetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        with(target) {

            apply(plugin = libs.plugins.detekt.get().pluginId)

            extensions.configure<DetektExtension>("detekt") {
                toolVersion.set(libs.versions.detekt.get())
//                config.setFrom(rootProject.layout.projectDirectory.file("build-logic/analysis/detekt/detekt.yml"))
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

                val froms = arrayListOf(rootProject.layout.projectDirectory.file("build-logic/analysis/detekt/detekt.yml")).apply {
                    if (isComposeCompilerPluginApplied(project))
                        add(rootProject.layout.projectDirectory.file("build-logic/analysis/detekt/detekt-compose.yml"))
                }

                config.setFrom(froms)

//                if (isComposeCompilerPluginApplied(project))
//                    config.from(rootProject.layout.projectDirectory.file("build-logic/analysis/detekt/detekt-compose.yml"))
            }

            dependencies.add("detektPlugins", "dev.detekt:detekt-rules-ktlint-wrapper:${libs.versions.detekt.get()}")

            if (isComposeCompilerPluginApplied(project)) {
                val composeRulesVersion = libs.versions.compose.rules.detekt.get()
                dependencies.add("detektPlugins", "io.nlopez.compose.rules:detekt:$composeRulesVersion")
                dependencies.add("detektPlugins", "io.nlopez.compose.rules:ktlint:$composeRulesVersion")
            }

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
