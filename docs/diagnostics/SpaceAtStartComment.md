# Пробел в начале комментария

| Тип | Поддерживаются<br/>языки | Важность | Включена<br/>по умолчанию | Время на<br/>исправление (мин) | Тэги |
| :-: | :-: | :-: | :-: | :-: | :-: |
| `Дефект кода` | `BSL`<br/>`OS` | `Информационный` | `Нет` | `1` | `standard` |

## Параметры 

| Имя | Тип | Описание | Значение по умолчанию |
| :-: | :-: | :-- | :-: |
| `commentsAnnotation` | `Регулярное выражение` | Пропускать комментарии-аннотации, начинающиеся с указанных подстрок. Список через запятую. Например: //@,//(c) | `"//@,//(c),//©"` |

<!-- Блоки выше заполняются автоматически, не трогать -->
## Описание диагностики

Между символами комментария "//" и текстом комментария должен быть пробел.  

Исключением из правила являются _**комментарии-аннотации**_, т.е. комментарии начинающиеся с определенной последовательности символов.

## Источники

* [Стандарт: Тексты модулей, пункт 7.3](https://its.1c.ru/db/v8std#content:456:hdoc)
