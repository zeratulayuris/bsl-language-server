# Ограничение на количество параметров метода

| Тип | Поддерживаются<br/>языки | Важность | Включена<br/>по умолчанию | Время на<br/>исправление (мин) | Тэги |
| :-: | :-: | :-: | :-: | :-: | :-: |
| `Дефект кода` | `BSL`<br/>`OS` | `Незначительный` | `Нет` | `30` | `standard`<br/>`brainoverload` |

## Параметры 

| Имя | Тип | Описание | Значение по умолчанию |
| :-: | :-: | :-- | :-: |
| `maxParamsCount` | `Число` | Допустимое количество параметров метода | `7` |

<!-- Блоки выше заполняются автоматически, не трогать -->
## Описание диагностики

Не рекомендуется объявлять в функциях много параметров (нужно ориентироваться на количество не более семи параметров), при этом не должно быть много параметров со значениями по умолчанию (нужно ориентироваться на количество не более трех таких параметров). В противном случае, читаемость вызывающего кода сильно снижается. Например, можно легко ошибиться в количестве запятых при передаче необязательных параметров.  

При необходимости передавать в функцию большое число параметров рекомендуется группировать однотипные параметры в один или несколько составных параметров типа Структура.

## Примеры

Неправильно:

```bsl
// Создает элемент справочника "Номенклатура"
Процедура СоздатьЭлементНоменклатуры(Наименование, ТоварУслуга, ЕдиницаИзмерения, ВесНетто, ПроверятьУникальность = Истина)

КонецПроцедуры
```

Правильно:  
Cгруппировать параметры, описывающие значения реквизитов номенклатуры, в структуру ЗначенияРеквизитов.

```bsl
// Создает элемент справочника "Номенклатура"
Процедура СоздатьЭлементНоменклатуры(ЗначенияРеквизитов, ПроверятьУникальность = Истина)
КонецПроцедуры
```

## Источники

* [Стандарт: Параметры процедур и функций](https://its.1c.ru/db/v8std#content:640:hdoc)
