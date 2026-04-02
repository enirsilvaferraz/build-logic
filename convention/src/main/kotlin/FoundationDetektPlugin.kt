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

            // Garante o fat JAR shadow do ktlint no classpath do worker; evita falhas ao carregar regras (ver detekt #9177).
            dependencies.add("detektPlugins", libs.detekt.ktlint.repackage)
            dependencies.add("detektPlugins", libs.detekt.rules.ktlint.wrapper)
            dependencies.add("detektPlugins", libs.nlopez.compose.rules.detekt)
            dependencies.add("detektPlugins", libs.nlopez.compose.rules.ktlint)
            dependencies.add("detektPlugins", libs.foundation.detekt.rules)

            configurations.named("detektPlugins").configure {
                resolutionStrategy.eachDependency {
                    if (requested.group == "org.jetbrains.kotlin" && requested.name.startsWith("kotlin-stdlib")) {
                        useVersion(libs.versions.kotlin.get())
                        because("Alinha kotlin-stdlib do wrapper ktlint com o projeto (mistura de versões pode quebrar o carregamento das regras).")
                    }
                }
            }

            tasks.withType<Detekt>().configureEach {
                jvmTarget.set("21")
                exclude { it.file.absolutePath.contains("/build/") }
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
