# Повторяющиеся условия в синтаксической конструкции Если...Тогда...ИначеЕсли...

| Тип | Поддерживаются<br/>языки | Важность | Включена<br/>по умолчанию | Время на<br/>исправление (мин) | Тэги |
| :-: | :-: | :-: | :-: | :-: | :-: |
| `Дефект кода` | `BSL`<br/>`OS` | `Важный` | `Нет` | `10` | `suspicious` |

<!-- Блоки выше заполняются автоматически, не трогать -->
## Описание диагностики

Синтаксическая конструкция **Если...Тогда...ИначеЕсли...** не должна иметь одинаковых условий.

## Примеры

```bsl
Если п = 0 Тогда
    т = 0;
ИначеЕсли п = 1 Тогда
    т = 1;
ИначеЕсли п = 1 Тогда
    т = 2;
Иначе
    т = -1;
КонецЕсли;
```
