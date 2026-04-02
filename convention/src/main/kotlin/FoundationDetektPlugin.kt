import com.eferraz.buildlogic.ext.libs
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
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

        if (target === target.rootProject) {
            val root = target
            root.gradle.projectsEvaluated {
                root.subprojects.forEach { subproject ->
                    wireDetektGates(subproject, root)
                }
            }
        }
    }

    private fun wireDetektGates(project: Project, root: Project) {

        fun Task.dependOnAllProjectDetekt() {
            dependsOn(root.tasks.named("detekt"))
            root.subprojects.forEach { sub ->
                sub.tasks.findByName("detekt")?.let { dependsOn(it) }
            }
        }

        with(project) {
            when {
                pluginManager.hasPlugin("com.android.application") ->
                    tasks.named("preBuild").configure { dependOnAllProjectDetekt() }
                path == DESKTOP_APP_PATH -> {
                    tasks.named("compileKotlin").configure { dependOnAllProjectDetekt() }
                    tasks.matching { it.name in DESKTOP_ENTRY_TASKS }.configureEach { dependOnAllProjectDetekt() }
                }
                path == UMBRELLA_APP_PATH ->
                    tasks.matching { it.name in IOS_XCODE_EMBED_TASKS }.configureEach { dependOnAllProjectDetekt() }
            }
        }
    }

    private companion object {

        const val DESKTOP_APP_PATH = ":apps:desktopApp"
        const val UMBRELLA_APP_PATH = ":apps:umbrellaApp"

        val DESKTOP_ENTRY_TASKS = setOf(
            "run",
            "runDistributable",
            "runRelease",
            "runReleaseDistributable",
            "hotRun",
            "hotRunAsync",
            "hotDev",
            "hotDevAsync",
            "runHot",
        )

        val IOS_XCODE_EMBED_TASKS = setOf(
            "embedAndSignAppleFrameworkForXcode",
            "embedSwiftExportForXcode",
        )
    }
}
