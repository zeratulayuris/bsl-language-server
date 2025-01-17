# Canonical keyword writing

| Type | Scope | Severity | Activated<br/>by default | Minutes<br/>to fix | Tags |
| :-: | :-: | :-: | :-: | :-: | :-: |
| `Code smell` | `BSL`<br/>`OS` | `Info` | `Нет` | `1` | `standard` |

<!-- Блоки выше заполняются автоматически, не трогать -->
## Description

A built-in language constructs, keywords must be written canonically.

**Keywords**

| RU                 | EN            |
|--------------------|---------------|
| ВызватьИсключение  | Raise         |
| Выполнить          | Execute       |
| ДобавитьОбработчик | AddHandler    |
| Для                | For           |
| Если               | If            |
| Знач               | Val           |
| И                  | AND, and      |
| Из                 | In            |
| ИЛИ, Или           | OR, Or        |
| Иначе              | Else          |
| ИначеЕсли          | ElsIf         |
| Исключение         | Except        |
| Истина             | True          |
| Каждого, каждого   | Each, each    |
| КонецЕсли          | EndIf         |
| КонецПопытки       | EndTry        |
| КонецПроцедуры     | EndProcedure  |
| КонецФункции       | EndFunction   |
| КонецЦикла         | EndDo         |
| НЕ, Не             | NOT, Not      |
| Неопределено       | Undefined     |
| Перейти            | Goto          |
| Перем              | Var           |
| По                 | For           |
| Пока               | WHile         |
| Попытка            | Try           |
| Процедура          | Procedure     |
| Прервать           | Break         |
| Продолжить         | Continue      |
| Тогда              | Then          |
| Цикл               | Do            |
| УдалитьОбработчик  | RemoveHandler |
| Функция            | Function      |
| Экспорт            | Export        |

**Preprocessor Instructions**

| RU                                 | EN                             |
|------------------------------------|--------------------------------|
| ВебКлиент                          | WebClient                      |
| ВнешнееСоединение                  | ExternalConnection             |
| Если                               | If                             |
| И                                  | AND, And                       |
| ИЛИ, Или                           | OR, Or                         |
| Иначе                              | Else                           |
| ИначеЕсли                          | ИначеЕсли                      |
| КонецЕсли                          | EndIf                          |
| КонецОбласти                       | EndRegion                      |
| Клиент                             | Client                         |
| МобильноеПриложениеКлиент          | MobileAppClient                |
| МобильноеПриложениеСервер          | MobileAppServer                |
| МобильныйКлиент                    | MobileClient                   |
| НаКлиенте                          | AtClient                       |
| НаСервере                          | AtServer                       |
| НЕ, Не                             | NOT, Not                       |
| Область                            | Region                         |
| Сервер                             | Server                         |
| Тогда                              | Then                           |
| ТолстыйКлиентОбычноеПриложение     | ThickClientOrdinaryApplication |
| ТолстыйКлиентУправляемоеПриложение | ThickClientManagedApplication  |
| ТонкийКлиент                       | ThinClient                     |

**Compilation directives**

| RU                             | EN                        |
|--------------------------------|---------------------------|
| НаКлиенте                      | AtClient                  |
| НаСервере                      | AtServer                  |
| НаСервереБезКонтекста          | AtServerNoContext         |
| НаКлиентеНаСервереБезКонтекста | AtClientAtServerNoContext |
| НаКлиентеНаСервере             | AtClientAtServer          |

## Sources

* [Standart: Modules texts (RU)](https://its.1c.ru/db/v8std#content:456:hdoc)
