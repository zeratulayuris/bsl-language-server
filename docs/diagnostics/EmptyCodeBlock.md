# Пустой блок кода

| Тип | Поддерживаются<br/>языки | Важность | Включена<br/>по умолчанию | Время на<br/>исправление (мин) | Тэги |
| :-: | :-: | :-: | :-: | :-: | :-: |
| `Дефект кода` | `BSL`<br/>`OS` | `Важный` | `Нет` | `5` | `badpractice`<br/>`suspicious` |

## Параметры 

| Имя | Тип | Описание | Значение по умолчанию |
| :-: | :-: | :-- | :-: |
| `commentAsCode` | `Булево` | Считать комментарий в блоке кодом | `false` |

<!-- Блоки выше заполняются автоматически, не трогать -->
## Описание диагностики

Пустые блоки являются признаком возможной ошибки:

- Забыли реализовать
- Удалили содержимое

Пустые блоки кода должны быть наполнены либо удалены.
