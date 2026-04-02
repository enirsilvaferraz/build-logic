package com.eferraz.pokedex.detekt.rules

import dev.detekt.test.TestConfig
import dev.detekt.test.lint
import kotlin.test.Test
import kotlin.test.assertEquals

class TooManyFunctionsInFileTest {

    @Test
    fun `reports when top-level function count exceeds threshold`() {
        val code =
            """
            fun a() = Unit
            fun b() = Unit
            fun c() = Unit
            """.trimIndent()

        val findings = TooManyFunctionsInFile(TestConfig("threshold" to "2")).lint(code)

        assertEquals(1, findings.size)
    }

    @Test
    fun `does not report when below threshold`() {
        val code =
            """
            fun a() = Unit
            fun b() = Unit
            """.trimIndent()

        val findings = TooManyFunctionsInFile(TestConfig("threshold" to "5")).lint(code)

        assertEquals(0, findings.size)
    }

    @Test
    fun `counts member functions per class separately`() {
        val code =
            """
            class A {
              fun a1() = Unit
              fun a2() = Unit
              fun a3() = Unit
            }
            class B {
              fun b1() = Unit
              fun b2() = Unit
              fun b3() = Unit
            }
            """.trimIndent()

        val findings = TooManyFunctionsInFile(TestConfig("threshold" to "2")).lint(code)

        assertEquals(2, findings.size)
    }

    @Test
    fun `top-level and class scopes are independent`() {
        val code =
            """
            fun t1() = Unit
            fun t2() = Unit
            class C {
              fun c1() = Unit
              fun c2() = Unit
              fun c3() = Unit
            }
            """.trimIndent()

        val findings = TooManyFunctionsInFile(TestConfig("threshold" to "2")).lint(code)

        assertEquals(1, findings.size)
    }

    @Test
    fun `nested class has its own counter`() {
        val code =
            """
            class Outer {
              fun o1() = Unit
              class Inner {
                fun i1() = Unit
                fun i2() = Unit
                fun i3() = Unit
              }
            }
            """.trimIndent()

        val findings = TooManyFunctionsInFile(TestConfig("threshold" to "2")).lint(code)

        assertEquals(1, findings.size)
    }

    @Test
    fun `interface member functions are counted`() {
        val code =
            """
            interface I {
              fun a()
              fun b()
              fun c()
            }
            """.trimIndent()

        val findings = TooManyFunctionsInFile(TestConfig("threshold" to "2")).lint(code)

        assertEquals(1, findings.size)
    }

    @Test
    fun `suppress on class hides member function finding`() {
        val code =
            """
            @Suppress("TooManyFunctionsInFile")
            class C {
              fun a() = Unit
              fun b() = Unit
              fun c() = Unit
            }
            """.trimIndent()

        val findings = TooManyFunctionsInFile(TestConfig("threshold" to "2")).lint(code)

        assertEquals(0, findings.size)
    }

    @Test
    fun `suppress with ruleset prefix hides finding`() {
        val code =
            """
            @Suppress("FoundationDetekt:TooManyFunctionsInFile")
            class C {
              fun a() = Unit
              fun b() = Unit
              fun c() = Unit
            }
            """.trimIndent()

        val findings = TooManyFunctionsInFile(TestConfig("threshold" to "2")).lint(code)

        assertEquals(0, findings.size)
    }

    @Test
    fun `file suppress hides top-level finding`() {
        val code =
            """
            @file:Suppress("TooManyFunctionsInFile")

            fun a() = Unit
            fun b() = Unit
            fun c() = Unit
            """.trimIndent()

        val findings = TooManyFunctionsInFile(TestConfig("threshold" to "2")).lint(code)

        assertEquals(0, findings.size)
    }
}
