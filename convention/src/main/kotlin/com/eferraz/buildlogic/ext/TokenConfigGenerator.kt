package com.eferraz.buildlogic.ext

import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

/**
 * Configura a geração do arquivo TokenConfig.kt para uso em commonMain
 * 
 * @param fileName Nome do arquivo e objeto a ser gerado (ex: "TokenConfig")
 * @param packageName O pacote onde o TokenConfig será gerado (ex: "com.eferraz.network")
 * @param properties Lista de nomes de propriedades no local.properties (ex: listOf("BRAPI_TOKEN", "API_KEY"))
 */
fun Project.configureTokenConfigGeneration(
    fileName: String,
    packageName: String,
    properties: List<String>,
): TaskProvider<*> {
    
    // Define o caminho de saída
    val finalOutputPath = packageName.replace(".", "/")
    val outputFile = layout.buildDirectory.file("generated/kotlin/$finalOutputPath/$fileName.kt")
    
    // Gera a task de geração do TokenConfig
    val codeGeneratorTask = tasks.register("codeGeneratorTask") {

        val localPropertiesFile = rootProject.file("local.properties")

        inputs.file(localPropertiesFile).optional()
        outputs.file(outputFile)
        
        doLast {

            // Lê o local.properties dentro da task para garantir que está atualizado
            val localProperties = Properties()
            if (localPropertiesFile.exists()) {
                localPropertiesFile.inputStream().use {
                    localProperties.load(it)
                }
            }
            
            // Gera as constantes para cada propriedade
            val constants = properties.joinToString("\n") { prop ->
                val value = localProperties.getProperty(prop, "")
                "    const val $prop = \"$value\""
            }

            val file = outputFile.get().asFile
            file.parentFile.mkdirs()
            
            // Gera o conteúdo do arquivo
            val fileContent = buildString {
                appendLine("package $packageName")
                appendLine()
                appendLine("internal object $fileName {")
                appendLine(constants)
                appendLine("}")
            }
            
            file.writeText(fileContent)
        }
    }
    
    // Configura para executar antes de todas as compilações do Kotlin
    tasks.withType(KotlinCompile::class.java).configureEach {
        dependsOn(codeGeneratorTask)
    }
    
    // Também configura para tasks de metadata (compatibilidade)
    tasks.matching { 
        (it.name.contains("compile") || it.name.startsWith("ksp")) &&
        (it.name.contains("Kotlin") || it.name.contains("Metadata"))
    }.configureEach {
        dependsOn(codeGeneratorTask)
    }
    
    // Adiciona o diretório gerado ao sourceSets
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets {
            commonMain {
                kotlin.srcDir(layout.buildDirectory.dir("generated/kotlin"))
            }
        }
    }
    
    return codeGeneratorTask
}

