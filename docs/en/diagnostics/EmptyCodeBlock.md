# Empty code block

| Type | Scope | Severity | Activated<br/>by default | Minutes<br/>to fix | Tags |
| :-: | :-: | :-: | :-: | :-: | :-: |
| `Code smell` | `BSL`<br/>`OS` | `Major` | `Нет` | `5` | `badpractice`<br/>`suspicious` |

## Parameters 

| Name | Type | Description | Default value |
| :-: | :-: | :-- | :-: |
| `commentAsCode` | `boolean` | Считать комментарий в блоке кодом | `false` |

<!-- Блоки выше заполняются автоматически, не трогать -->
## Description

Empty blocks are a sign of a possible error:

- Forgot to implement
- Deleted content

Empty blocks of code must be filled or removed.
