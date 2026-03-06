# 📦 Inbentario Kudeatzailea (Tablet Edition)

![Kotlin](https://img.shields.io/badge/Hizkuntza-Kotlin-7F52FF?logo=kotlin) ![Platform](https://img.shields.io/badge/Gailua-Tablet-blue)

Enpresa txikientzako inbentarioa kudeatzeko aplikazio natiboa, Android tabletetarako optimizatua. Sistema honek produktuen kontrola, albaranen kudeaketa eta erabiltzaileen administrazioa modu intuitiboan ahalbidetzen du.

---

## 🗺️ Aplikazioaren Fluxua

Hona hemen aplikazioaren fluxu diagrama bat:

```mermaid
graph TD
    A[Splash Screen: Logo] --> B[Profil Hautaketa]
    B --> C{Profila hautatuta?}
    C -- Bai --> D[Produktu Zerrenda Nagusia]
    C -- Ez --> B

    D --> E[Menu Desplegarria]

    E --> F[Produktua Editatu]
    F --> F1[Produktuaren datuak aldatu / Gorde]

    E --> G[Profilak]
    G --> G1[Ikusi profil guztiak]
    G1 --> G2[Profila Editatu]
    G1 --> G3[Profil Berria Sortu]

    E --> H[Albaranak]
    H --> H1[Zerrenda ikusi]
    H1 --> H2[Albaran berria igo]
    H2 --> H3[Argazkia atera / Kamera]
    H3 --> H4[Gorde eta Zerrendara itzuli]

    E --> I[Inbentarioa]
    I --> I1[Produktu guztien ikuspegia]
```

---


## 🔐 Sartzeko Fluxua eta Segurtasuna

* **Hasierako Pantaila:** Aplikazioa piztean konpainiaren logoa erakusten da karga-prozesuan.
* **Profil Hautaketa:** Erabiltzaile zerrenda bat agertuko da; profil bat hautatu arte aplikazioa blokeatuta egongo da.
* **Produktuen Zerrenda** Profila hautatuz gero, duela gutxi elkarreragin diren 10 produktuak agertuko dira.
 

> 🔑 **GARRANTZITSUA (Admin Segurtasuna)**
> Atal pribatuetara (Inbentarioa, Albaranak, Edizioa) sartzeko pasahitza beharko da. Behin sartuta, gogoratu egingo da profil aldaketa egon arte.

---

## 📋 Produktuen Zerrenda (Pantaila Nagusia)

Profila hautatu ondoren, azken 10 interakzioak erakusten dituen panela agertuko da.
* **Bilaketa Eraginkorra:** Produktuak dataren edo motaren arabera filtratu daitezke.
* **Stock Kontrol Azkarra:** Produktu bakoitzak + eta - botoiak ditu kantitatea erraz aldatzeko.
* **Alboko Menua:** Menu zabalgarri baten bidez hainbat aukera ageriko dira.
* **Alerta Bisualak:** Produktu bat gutxieneko kantitatera iristen denean, ohar bat agertuko da egoeraz ohartarazteko.

---

## 🛠️ Administrazio Moduluak


