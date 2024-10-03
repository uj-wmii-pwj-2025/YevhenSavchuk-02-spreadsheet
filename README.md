## Zadanie do wykładu 2: Arkusz Kalkulacyjny

Zadanie polega na zaimplementowaniu obliczeń w prostym arkuszu kalkulacyjnym. Arkusz operuje tylko na liczbach całkowitych, w zakresie ograniczonym typem `int`.

W klase `uj.wmii.pwj.spreadsheet.Spreadsheet` Należy zaimplementować metodę `calculate` tak, aby operując na źródłowym arkuszu (przekazanym w paramtrze) zwróciła arkusz wynikowy, z wykonanymi obliczeniami. Tablica wejściowa zawiera kolejno: wiersze i kolumny.

Arkusz powinien obsługiwać następujące operacje:
- wartość: jeśli komórka zawiera liczbę, należy ją po prostu zostawić.
- referencja: jeśli komórka zaczyna się od znaku `$`, zawiera referencję do innej komórki. Referencje są zbudowane podobnie jak w Excelu, np zapis: `$A1` oznacza pierwszą kolumnę (`A`), pierwszy wiersz (`1`). Zapis `$C7` oznacza trzecią kolumnę, pierwszy wiersz.
- formuła: jeśli komórka zaczyna się od znaku `=`, zawiera formułę. Każda formuła składa się ze znaku `=`, nazwy formuły, oraz dwóch oddzielonych przecinkiem parametrów w zwykłych nawiasach. Parametrem formuły może być tylko wartość lub referencja (ale nie inna formuła).

Istniejące formuły:
- `ADD` - dodawanie obu parametrów,
- `SUB` - odejmowanie parametrów,
- `MUL` - mnożenie parametrów,
- `DIV` - dzielenie całkowite parametrów,
- `MOD` - reszta z dzielenia parametrów.

Arkusz nie będzie zawierał referencji cyklicznych (np komórka `A1` odwołuje się do `B2`, a `B2` znów do `A1`).

Przykład:

Dla arkusza:
```$xslt
1,2,3
4,5,6
$A1,$C1,$B3
=ADD(10,$A1),=SUB($C3,$A1),0
```
Wynik powinien wyglądać:
```$xslt
1,2,3
4,5,6
1,3,3
11,2,0
```
