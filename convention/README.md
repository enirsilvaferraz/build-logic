# Convenções Gradle (`:convention`)

Este módulo é a **biblioteca de plugins** do composite build `build-logic`. Cada plugin encapsula um conjunto de configurações, dependências e opções
de compilação que os módulos KMP do repositório reutilizam via **Version Catalog** (`alias(libs.plugins.foundation.*)`), em vez de repetir a mesma
configuração em cada `build.gradle.kts`.

Visão geral do `build-logic`: [../README.md](../README.md).

---

## O que este projeto compila

- **Plugins Gradle** registrados em [build.gradle.kts](build.gradle.kts), com IDs definidos em [../gradle/libs.versions.toml](../gradle/libs.versions.toml) sob o prefixo `foundation-*`.
- **Extensões Kotlin** em `src/main/kotlin/com/eferraz/buildlogic/ext/`, usadas pelos próprios plugins ou por scripts de build que compartilhem o classpath do `build-logic`.

---

## Plugins expostos

No `plugins { }` dos módulos use o **alias** do catálogo (`alias(libs.plugins.foundation.*)`). O **ID Gradle** é o identificador público do plugin (aparece em relatórios e depuração).

### `foundation.kmp.project`

- **Alias no catálogo:** `libs.plugins.foundation.project`
- **Classe:** `KmpProjectPlugin`
- **Função:** base KMP — Kotlin Multiplatform, Android Library, serialization, targets (JVM, iOS, Android), `explicitApi`, dependências comuns de teste e aplicação de **Detekt** (`foundation.detekt`).
- **Aplica-se a:** Subprojetos KMP.

### `foundation.compose`

- **Alias no catálogo:** `libs.plugins.foundation.library.comp`
- **Classe:** `LibraryComposePlugin`
- **Função:** Compose Multiplatform + compiler, flags extras no compilador Kotlin e bundle Compose em `commonMain`; tooling Android no classpath de runtime.
- **Aplica-se a:** Subprojetos KMP.

### `foundation.koin`

- **Alias no catálogo:** `libs.plugins.foundation.library.koin`
- **Classe:** `LibraryKoinPlugin`
- **Função:** plugin do compilador Koin, BOM e bundles em `commonMain` / `commonTest`; se o Compose compiler já estiver aplicado, adiciona dependências Koin para Compose.
- **Aplica-se a:** Subprojetos KMP.

### `foundation.room`

- **Alias no catálogo:** `libs.plugins.foundation.library.room`
- **Classe:** `LibraryRoomPlugin`
- **Função:** KSP + Room, bundle Room em `commonMain`, dependências de compilação KSP por alvo e diretório de schema em `schemas`.
- **Aplica-se a:** Subprojetos KMP.

### `foundation.ktor`

- **Alias no catálogo:** `libs.plugins.foundation.library.ktor`
- **Classe:** `LibraryKtorPlugin`
- **Função:** serialization (se ainda não vier de outro plugin) e bundles Ktor em `commonMain` e nos source sets específicos de Android, iOS e JVM/Desktop.
- **Aplica-se a:** Subprojetos KMP.

### `foundation.detekt`

- **Alias no catálogo:** `libs.plugins.foundation.detekt`
- **Classe:** `FoundationDetektPlugin`
- **Função:** Detekt com config central em `build-logic/analysis/detekt/`, baseline por módulo, plugins extras (ktlint repackage, regras Compose, **regras próprias** `:detekt-rules`).
- **Saiba mais:** [../detekt-rules/README.md](../detekt-rules/README.md).
- **Aplica-se a:** Projetos Raiz.

---

## Exemplos de configuração

Trechos típicos de `build.gradle.kts` de um módulo que já usa o catálogo do repositório (`libs`).

### Projetos KMP

Biblioteca multiplataforma **sem** Compose: o plugin de projeto define targets (JVM, iOS, Android), `explicitApi`, serialization, dependências comuns de teste e **Detekt** (via `foundation.detekt` aplicado internamente).

```kotlin
plugins {
    alias(libs.plugins.foundation.project)
}
```

### Projetos CMP

Mesma base, mais **Compose Multiplatform** e compiler. Aplique o plugin Compose **depois** do de projeto.

```kotlin
plugins {
    alias(libs.plugins.foundation.project)
    alias(libs.plugins.foundation.library.comp)
}
```

### Projetos Android, iOS e JVM

Em breve novos plugins serão adicionados para atender a essas plataformas.