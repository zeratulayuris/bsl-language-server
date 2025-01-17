# Ternary operator usage

| Type | Scope | Severity | Activated<br/>by default | Minutes<br/>to fix | Tags |
| :-: | :-: | :-: | :-: | :-: | :-: |
| `Code smell` | `BSL`<br/>`OS` | `Minor` | `Нет` | `3` | `brainoverload` |

<!-- Блоки выше заполняются автоматически, не трогать -->
## Description

Use If-Else instead of ternary operator

## Examples

Bad:

```bsl
Result = ?(X%15 <> 0, ?(X%5 <> 0, ?(X%3 <> 0, x, "Fizz"), "Buzz"), "FizzBuzz");
```

Good:

```bsl
If x% 15 = 0 Then
    Result = "FizzBuzz";
ElseIf, if x% 3 = 0 Then
    Result = "Fizz";
ElseIf, if x% 5 = 0 Then
    Result = "Buzz";
Else
    Result = x;
EndIf;
```

Bad:

```bsl
If ?(P.Emp_emptype = Null, 0, PageEmp_emptype) = 0 Then
      Status = "Done";
EndIf;
```

Good:

```bsl
If PageEmp_emptype = Null OR PageEmp_emptype = 0 Then
      Status = "Done";
End If;
```
