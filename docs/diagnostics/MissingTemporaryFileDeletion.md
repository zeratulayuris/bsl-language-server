# Отсутствует удаление временного файла после использования

| Тип | Поддерживаются<br/>языки | Важность | Включена<br/>по умолчанию | Время на<br/>исправление (мин) | Тэги |
| :-: | :-: | :-: | :-: | :-: | :-: |
| `Ошибка` | `BSL`<br/>`OS` | `Важный` | `Нет` | `5` | `badpractice`<br/>`standard` |

## Параметры 

| Имя | Тип | Описание | Значение по умолчанию |
| :-: | :-: | :-- | :-: |
| `searchDeleteFileMethod` | `Регулярное выражение` | Ключевые слова поиска методов удаления / перемещения файлов | `"УдалитьФайлы|DeleteFiles|ПереместитьФайл|MoveFile"` |

<!-- Блоки выше заполняются автоматически, не трогать -->
## Описание диагностики

После окончания работы с временным файлом или каталогом, его необходимо удалить самостоятельно. 
Нельзя рассчитывать на автоматическое удаление файлов и каталогов при следующем запуске платформы, 
это может привести к исчерпанию свободного места в каталоге временных файлов.

## Примеры

Неправильно:

```bsl
ИмяПромежуточногоФайла = ПолучитьИмяВременногоФайла("xml");
Данные.Записать(ИмяПромежуточногоФайла);
// далее нет удаления файла
```

Правильно:

```bsl
ИмяПромежуточногоФайла = ПолучитьИмяВременногоФайла("xml");
Данные.Записать(ИмяПромежуточногоФайла);

// Работа с файлом
...

// Удаляем временный файл
Попытка
   УдалитьФайлы(ИмяПромежуточногоФайла);
Исключение
   ЗаписьЖурналаРегистрации(НСтр("ru = 'Мой механизм.Действие'"), УровеньЖурналаРегистрации.Ошибка, , , ПодробноеПредставлениеОшибки(ИнформацияОбОшибке()));
КонецПопытки;
```

## Источники

* [Доступ к файловой системе из кода конфигурации](https://its.1c.ru/db/v8std#content:542:hdoc)
