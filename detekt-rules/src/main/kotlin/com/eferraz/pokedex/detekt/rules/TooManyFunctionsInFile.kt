package com.eferraz.pokedex.detekt.rules

import dev.detekt.api.Config
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import dev.detekt.api.config
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction

/**
 * Conta funções nomeadas (`fun`) por escopo:
 * — todas as funções **top-level** do arquivo (um contador);
 * — funções **membro** de cada classe, interface, object ou enum (contador reiniciado
 *   em cada tipo; tipos aninhados têm contador próprio).
 *
 * Configurável via `detekt.yml` ([detekt extensions](https://detekt.dev/docs/introduction/extensions)).
 */
class TooManyFunctionsInFile(config: Config) : Rule(config, "Excesso de funções nomeadas no mesmo escopo (top-level ou dentro de um tipo).") {

    private val threshold: Int by config(defaultValue = 30)

    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)

        val topLevelCount = file.declarations.count { it is KtNamedFunction }

        maybeReport(
            anchor = file,
            count = topLevelCount,
            scopeDescription = "no escopo top-level do arquivo ${file.name}",
        )

        file.declarations.filterIsInstance<KtClassOrObject>().forEach(::analyzeClassOrObject)
    }

    private fun analyzeClassOrObject(clazz: KtClassOrObject) {

        val memberCount = clazz.declarations.count { it is KtNamedFunction }

        maybeReport(
            anchor = clazz,
            count = memberCount,
            scopeDescription = "no tipo ${clazz.name ?: "<anonymous>"} (${fileSummary(clazz.containingKtFile)})",
        )

        clazz.declarations.filterIsInstance<KtClassOrObject>().forEach(::analyzeClassOrObject)
    }

    private fun maybeReport(anchor: KtElement, count: Int, scopeDescription: String) {

        if (count <= threshold) return

        report(
            Finding(
                entity = entityForAnchor(anchor),
                message = "Declaradas $count funções; o limite é $threshold ($scopeDescription).",
                references = emptyList(),
            ),
        )
    }

    private fun entityForAnchor(anchor: KtElement): Entity =
        when (anchor) {
            is KtClassOrObject -> Entity.atName(anchor)
            else -> Entity.from(anchor)
        }

    private fun fileSummary(file: KtFile?): String =
        file?.name ?: "(arquivo desconhecido)"
}
