package com.eferraz.buildlogic.ext

import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.loadProperties

fun Project.generateRuntimeConfigFromLocalProperties(
    packageName: String = "com.eferraz.entities.runtime",
): TaskProvider<*> {

    val outputFile = layout.buildDirectory.file(
        "generated/kotlin/${packageName.replace(".", "/")}/RuntimeConfig.kt",
    )

    val codeGeneratorTask = tasks.register("generateRuntimeConfig") {

        val localPropertiesFile = rootProject.file("local.properties")

        inputs.file(localPropertiesFile).optional()
        outputs.file(outputFile)

        doLast {

            val localProperties = loadProperties(localPropertiesFile.path)

            val constantsBody = localProperties.entries
                .joinToString("\n") { (key, value) ->

                    val (kotlinType, kotlinLiteral) = inferTypedLiteral(value.toString())
                    val rawKey = key.toString().toUpperSnakeConstantName()

                    "    public const val $rawKey: $kotlinType = $kotlinLiteral"
                }

            val file = outputFile.get().asFile
            file.parentFile.mkdirs()
            file.writeText(
                buildString {
                    appendLine("package $packageName")
                    appendLine()
                    appendLine("public object RuntimeConfig {")
                    append(constantsBody)
                    appendLine()
                    appendLine("}")
                },
            )
        }
    }

    tasks.withType(KotlinCompile::class.java).configureEach {
        dependsOn(codeGeneratorTask)
    }

    tasks.matching {
        (it.name.contains("compile") || it.name.startsWith("ksp")) &&
                (it.name.contains("Kotlin") || it.name.contains("Metadata"))
    }.configureEach {
        dependsOn(codeGeneratorTask)
    }

    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets {
            commonMain {
                kotlin.srcDir(layout.buildDirectory.dir("generated/kotlin"))
            }
        }
    }

    return codeGeneratorTask
}

private fun inferTypedLiteral(rawValue: String): Pair<String, String> {

    val s = rawValue.trim()

    s.toBooleanStrictOrNull()?.let {
        return "Boolean" to it.toString()
    }

    s.toDoubleOrNull()?.let {
        return "Double" to it.toString()
    }

    s.toIntOrNull()?.let {
        return "Int" to it.toString()
    }

    return "String" to "\"$s\""
}

/**
 * Converte uma chave de `local.properties` num nome de constante em UPPER_SNAKE_CASE.
 * `-` e `.` tratam-se como `_`; segmentos camelCase separados antes de juntar (ex.: `fooBar` → `FOO_BAR`).
 */
private fun String.toUpperSnakeConstantName() =
    replace('-', '_').replace('.', '_').uppercase()