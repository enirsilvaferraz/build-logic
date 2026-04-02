# `build-logic` — convenções Gradle

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

## Decisões Arquiteturais

### Included build centralizado

Os plugins centralizam todas as informações necessárias para configurar o gradle dos submódulos, de forma, a evitar repetições desnecessárias de
configuração.

### Dependencias atualizadas

O Catálogo é centralizado, de forma, a compartilhar a atualização das bibliotecas de terceiros para todos os projetos dependentes do `build-logic`.

