# Оператор "Перейти" не должен использоваться

| Тип | Поддерживаются<br/>языки | Важность | Включена<br/>по умолчанию | Время на<br/>исправление (мин) | Тэги |
| :-: | :-: | :-: | :-: | :-: | :-: |
| `Дефект кода` | `BSL`<br/>`OS` | `Критичный` | `Нет` | `5` | `standard`<br/>`badpractice` |

<!-- Блоки выше заполняются автоматически, не трогать -->
## Описание диагностики

```goto``` является неструктурированным оператором потока управления. Это делает код менее читаемым и обслуживаемым.  
Вместо этого следует использовать структурированные операторы управления потоком, например
```Если```, ```Пока```, ```Для``` ```Продолжить``` или ```Прервать```.

## Примеры

Плохо:

```bsl
Сч = 0;
~Петля: Сообщить(СтрШаблон("Сч = %1", Сч));
Сч = Сч + 1;

Если Сч < 10 Тогда

    Перейти ~Петля;

КонецЕсли;
```

Хорошо:

```bsl
Для Сч = 0 По 10 Цикл

   Сообщить(СтрШаблон("Сч = %1", Сч))

КонецЦикла;
```
