# Composite Build (`build-logic`)

`build-logic` é um **Composite Build** que concentra **plugins Kotlin DSL** reutilizados pelos módulos de projetos KMP.
O objetivo é **uma única fonte de verdade** para multiplataforma, Android, Compose, Koin, Room e Ktor — em vez de copiar blocos `plugins { }` e
`kotlin { }` em cada `build.gradle.kts`.

---

## Estrutura

| Projeto             | Função                                                | README                                           |
|---------------------|-------------------------------------------------------|--------------------------------------------------|
| **`:convention`**   | Biblioteca de convenções de plugins.                  | [convention/README.md](convention/README.md)     |
| **`:detekt-rules`** | Biblioteca com regras Detekt próprias e customizadas. | [detekt-rules/README.md](detekt-rules/README.md) |

| Pastas            | Função                                                  |
|-------------------|---------------------------------------------------------|
| **`analysis`**    | Concentra as regras do Detekt.                          |
| **`gradle`**      | Concentra o catalogo de versões (`libs.versions.toml`). |
| **`ide-condigs`** | Concentra configurações da IDE (dicionário e outros) .  |

---

## Plugins expostos (resumo)

| ID (conceito)               | Classe                   | O que aplica                                                                           |
|-----------------------------|--------------------------|----------------------------------------------------------------------------------------|
| **foundation.project**      | `KmpProjectPlugin`       | Aplicado a projetos Kotlin Multiplataforma, concentra configurações de target.         |
| **foundation.detekt**       | `FoundationDetektPlugin` | Aplica o Detekt no projeto raiz para que os subprojetos sejam avaliados.               |
| **foundation.library.comp** | `LibraryComposePlugin`   | Convenções para módulos com **Compose** (compiler, recursos Android quando aplicável). |
| **foundation.library.koin** | `LibraryKoinPlugin`      | Convenções para módulos com **Koin** (anotações / geração).                            |
| **foundation.library.room** | `LibraryRoomPlugin`      | Convenções para módulos com **Room** no KMP.                                           |
| **foundation.library.ktor** | `LibraryKtorPlugin`      | Convenções para módulos com **Ktor Client** no KMP.                                    |

---

## Stack (versões)

| Tecnologia                                       | Versão            |
|--------------------------------------------------|-------------------|
| Gradle (wrapper)                                 | 9.5.0-milestone-6 |
| Kotlin                                           | 2.3.20            |
| Android Gradle Plugin (AGP)                      | 9.1.0             |
| Compose Multiplatform                            | 1.11.0-beta01     |
| Compose Material3                                | 1.11.0-alpha05    |
| Compose Adaptive (Material3)                     | 1.3.0-alpha06     |
| Compose Material Icons Extended                  | 1.7.3             |
| Navigation3                                      | 1.1.0-beta01      |
| KSP                                              | 2.3.3             |
| Ktor Client                                      | 3.4.1             |
| Room                                             | 2.8.4             |
| SQLite (bundled / Room KMP)                      | 2.6.2             |
| Koin BOM                                         | 4.2.0             |
| Coil 3                                           | 3.4.0             |
| Kotlinx DateTime                                 | 0.7.1             |
| Kermit                                           | 2.1.0             |
| Detekt                                           | 2.0.0-alpha.2     |
| Compose Detekt rules (nlopez)                    | 0.5.6             |
| MockK (testes)                                   | 1.14.9            |
| DataTable Material3                              | 0.12.0            |

**Catálogo Atualizado**: [gradle/libs.versions.toml](gradle/libs.versions.toml)

---

## Decisões Arquiteturais

### Included build centralizado

Os plugins centralizam todas as informações necessárias para configurar o gradle dos submódulos, de forma, a evitar repetições desnecessárias de
configuração.

### Dependencias atualizadas

O Catálogo é centralizado, de forma, a compartilhar a atualização das bibliotecas de terceiros para todos os projetos dependentes do `build-logic`.

