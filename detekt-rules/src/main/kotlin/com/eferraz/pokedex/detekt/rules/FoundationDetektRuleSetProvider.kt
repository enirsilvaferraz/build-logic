package com.eferraz.pokedex.detekt.rules

import dev.detekt.api.RuleSet
import dev.detekt.api.RuleSetId
import dev.detekt.api.RuleSetProvider

class FoundationDetektRuleSetProvider : RuleSetProvider {

    override val ruleSetId = RuleSetId("FoundationDetekt")

    override fun instance(): RuleSet =
        RuleSet(
            ruleSetId,
            listOf(
                ::TooManyFunctionsInFile,
            ),
        )
}
