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
    E --> G[Profilak]
    E --> H[Albaranak]
    E --> I[Inbentarioa]
```

---


## 🔐 Sartzeko Fluxua eta Segurtasuna

* **Hasierako Pantaila:** Aplikazioa piztean konpainiaren logoa erakusten da karga-prozesuan.
* **Profil Hautaketa:** Erabiltzaile zerrenda bat agertuko da; profil bat hautatu arte aplikazioa blokeatuta egongo da.

> 🔑 **GARRANTZITSUA (Admin Segurtasuna)**
> Atal pribatuetara (Inbentarioa, Albaranak, Edizioa) sartzeko pasahitza beharko da. Behin sartuta, gogoratu egingo da profil aldaketa egon arte.

---
