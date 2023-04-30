# GorillaGo Android alkalmazás

## Kezdőképernyő (MainActivity)
Ahogy a felhasználó megnyitja az alkalmazást, egy majom animáció kíséretében betölt a kezdőképernyő. Itt tudja kiválasztani a felhasználó, hogy regisztrálni vagy bejelentkezni kíván.
Ezen a két felületen található egy visszagomb is a bal felső sarokban, amivel vissza lehet navigálni a kezdőképernyőre.
Ha a felhasználó már bevolt jelentkezve akkor az alkalmazás egyből a főoldalra dob be.

## Regisztráció (RegistrationActivity)
Regisztráció kitöltése során a felhasználónak ki kell töltenie minden mezőt az alábbi kritériumok alapján: email címben szerepelnie kell @ karakternek, ne legyen foglalt az adatbázisban, a jelszónak legalább 8 karakternek kell lennie, illetve a két jelszónak egyeznie kell. Sikeres regisztrációt követően az alkalmazás átdob a bejelentkezési felületre, egyéb esetben kiírja a megfelelő hibaüzenet-et/eket.

## Bejelentkezés (LoginActivity)
Ezen felület kitöltése során helyes adatok megadása esetén mehetünk tovább a főoldalra. (ha nem szerepel az email cím adatbázisban, kiírásra kerül, illetve egyéb hibás adat esetén is van hibaüzenet)

## Főoldal (GorillaGoActivity)
Ezen az oldalon találhatók az éttermek: van egy ajánlott éttermek lista, ahol 4db étterem található illetve egy lista ahol az összes étterem szerepel.
Az adott éttermekre kattintva, az adott éttermek oldalára lehet navigáni

### Toolbar
A toolbarban két gomb/ikon szerepel. Az egyik egy toggle gomb, amivel a DrawerLayoutot lehet előhozni, a másik egy kosár ikon, amivel a Kosarat lehet előhozni (CartFragment).

### Drawerlayout
A drawerlayoutban 4 opció közül választhatunk:
- Főoldal: betölti a főoldalt (GorillaGoActivity)
- Fiók: betölti a fiók oldalt (AccountFragment)
- Keresés: Betölti a keresés oldalt (SearchFragment)
- Logut: Feldob egy üzenet, hogy a felhasználó biztosan kiszeretne e jelentkezni. Amennyiben igent válaszol, a kosara tartalma törlődik, illetve következő indításnál újból be kell jelentkeznie.

# Felhasználói oldal (AccountFragment)

Ezen az oldalon tudja a felhasználó beállítani, vagy módosítani a nevét, telefonszámát és email címét. A meglévő adatokat (pl új felhasználónál az email címét) beírja adatbázisból a mezőkbe. Ezek mellett a jelszavát is módosíthatja a felhasználó. Ehhez be kell írni a meglévő jelszót és az újat. Amennyiben nem jó a meglévő jelszó vagy az új nem felel meg a regisztrációnál is szereplő kritériumnak, hibaüzenet kíséretében nem történik meg a jelszócsere.

# Keresési oldal (SearchFragment)

Ezen az oldalon a felhasználó éttermekre tud rákeresni az adatbázisból. Ezeket fogja kilistázni ez az oldal, illetve a főoldalhoz hasonlóan, itt is kattintásra átdob az adott étterem oldalára.

# Étterem oldal (RestaurantFragment)

Ezen az oldal mindig annak az étteremnek lesz az oldala, amelyikre a felhasználó rámegy. Itt található az adott étterem menüje. A menü elemeinél hozzá lehet hozzáadni a kívánt mennyiségeket a kosárhoz. Ha olyan elemet adunk hozzá, ami már szerepel a kosárban, akkor értelemszerűen a mennyiség hozzáadódik a meglévőhöz, illetve az árak is frissülnek.

# Kosár (CartFragment)

Ez az oldal magárért beszél. Itt látható a felhasználó kosarának tartalma. Itt leírásház már nem tartozik az elemekhez, csak kép, név és ár. Az összár is felvan itt tüntetve. Ha a kosárban lévő elemek mennyiségén lehet változtani is. A tovább gombra kattintva átdob a szállítási adatok oldalra (OrderAddressFragment).

# Szállítási adatok (OrderAddressFragment)

Itt tölthetjük ki a rendeléshez tartozó szállítási adatokat. Fizetési opció nincs külön, mivel csak készpénzzel lehet fizetni. A rendelés gomb lenyomásakor, ha a rendelés sikeres, feltölti adatbázisba és visszadob a főoldalra.

