# Запятые без указания параметра в конце вызова метода

| Тип | Поддерживаются<br/>языки | Важность | Включена<br/>по умолчанию | Время на<br/>исправление (мин) | Тэги |
| :-: | :-: | :-: | :-: | :-: | :-: |
| `Дефект кода` | `BSL`<br/>`OS` | `Важный` | `Нет` | `1` | `standard`<br/>`badpractice` |

<!-- Блоки выше заполняются автоматически, не трогать -->
## Описание диагностики

Не следует указывать запятую в конце вызова метода без указания параметра. Это затрудняет восприятие и не несет важной информации.  
Необязательные параметры попадают под принцип Бритва Оккама "Не следует множить сущности без необходимости", так как "висящая" запятая малоинформативна.

Плохо:

```bsl
Результат = Действие(П1, П2,,);
```

Хорошо:

```bsl
Результат = Действие(П1, П2);
```

## Источники

* [Соглащения о написании кода. Параметры процедур и функций. Пункт 7](https://its.1c.ru/db/v8std#content:640:hdoc).
