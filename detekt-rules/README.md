# Regras Detekt (`:detekt-rules`)

Este módulo guarda **regras de análise estáticas** escritas para o [Detekt](https://detekt.dev/). Elas são checagens extras, além das que vêm prontas
no Detekt, para manter o código do projeto dentro das convenções que definimos aqui.

Em termos práticos: quando você roda o Detekt no repositório, o Gradle carrega este pacote e o Detekt passa a conhecer nosso conjunto de regras
chamado **FoundationDetekt**.

Esse projeto compila as regras em Kotlin e publica um artefato que o build usa como plugin do Detekt. Baseando-se nos arquivos de configuração
[../analysis/detekt/detekt.yml](../analysis/detekt/detekt.yml).

O nome técnico do pacote no build é `com.eferraz.buildlogic:detekt-rules`. No catálogo de versões do `build-logic` ele aparece como
`libs.foundation.detekt.rules`. Quem **encaixa** esse plugin no Gradle é o plugin de convenção **`foundation.detekt`**, definido em
[../convention/src/main/kotlin/FoundationDetektPlugin.kt](../convention/src/main/kotlin/FoundationDetektPlugin.kt).

---

## Regras Customizadas

Declaradas em: [FoundationDetektRuleSetProvider](src/main/kotlin/com/eferraz/pokedex/detekt/rules/FoundationDetektRuleSetProvider.kt).

### `TooManyFunctionsInFile`

**Ideia:** evitar arquivos com tantas funções que ficam difíceis de ler e de manter.

**Como conta:** não é só “funções no arquivo de uma vez”. O que vale é o **escopo**:

- Funções no nível do arquivo (fora de classe) entram todas no mesmo limite.
- Dentro de cada `class`, `interface`, `object` ou `enum`, a contagem **começa de novo**.
- Tipos aninhados têm o próprio limite, separado do tipo que os contém.

**Onde ajustar:** no `detekt.yml`, no bloco `FoundationDetekt` → `TooManyFunctionsInFile`. Ali você usa:

- **`active`**: `true` ou `false` para ligar ou desligar a regra.
- **`threshold`**: quantas funções no máximo por escopo. O valor padrão no código é 30; o Pokedex pode sobrescrever no `detekt.yml` mencionado acima.

---

## Como rodar o Detekt no repositório

Na **raiz do projeto**, o comando abaixo dispara a análise em todos os módulos que aplicam o plugin (cada um com seu próprio task `detekt`):

```bash
./gradlew detekt
```

Para analisar **só um módulo**, use o caminho Gradle do módulo (exemplo):

```bash
./gradlew :features:composeApp:detekt
```

Os relatórios (HTML, SARIF, etc.) ficam nos diretórios de build de cada módulo, conforme a configuração do plugin.

---

## Como configurar as regras (YAML compartilhado e `detektGenerateConfig`)

No dia a dia, as regras e os limites do monorepo vivem nos arquivos **centralizados** em `build-logic`:

- [../analysis/detekt/detekt.yml](../analysis/detekt/detekt.yml) — configuração principal (inclui o bloco **FoundationDetekt** das regras deste
  módulo).
- [../analysis/detekt/detekt-compose.yml](../analysis/detekt/detekt-compose.yml) — camada extra para regras relacionadas a Compose.

O plugin `foundation.detekt` aponta para esses dois arquivos para todos os módulos; alterações ali passam a valer no próximo `./gradlew detekt`.

---

## Baseline (`detektBaseline`)

A **baseline** é um arquivo XML que lista achados já conhecidos para o Detekt **ignorar** até você corrigi-los ou limpar a lista. No projeto, o
caminho está definido no plugin como `analysis/detekt-baseline.xml` **em cada módulo** que aplica o Detekt.

Para **regenerar** a baseline de um módulo (por exemplo, depois de introduzir uma regra nova e aceitar temporariamente o estado atual):

```bash
./gradlew detektBaseline
```

Exemplo com módulo real:

```bash
./gradlew :features:composeApp:detektBaseline
```

**Boas práticas:** use baseline com critério (dívida técnica explícita), revise o diff do XML no commit e prefira corrigir o código ou afinar o
`detekt.yml` em vez de só inflar a baseline.

---

## Como rodar os testes deste módulo

Na **raiz do repositório**:

```bash
./gradlew -p build-logic :detekt-rules:test
```

---

## Incluir uma nova regra (passo a passo)

1. Criar uma classe de regra em `src/main/kotlin/com/eferraz/pokedex/detekt/rules/` (seguindo o padrão das existentes).
2. Registrar essa regra no `RuleSet` dentro de `FoundationDetektRuleSetProvider`.
3. Documentar no `detekt.yml` (nome da regra, parâmetros, `active`, etc.) e, se precisar, atualizar `config.excludes` para o conjunto *
   *FoundationDetekt**.
4. Adicionar testes em `src/test/kotlin/` com a API de testes do Detekt (`dev.detekt.test`), para o comportamento não regredir.

