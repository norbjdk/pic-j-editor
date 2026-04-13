# Projekt — Analizator Obrazów

**Autor:** Norbert Dominkiewicz  
**Repozytorium:** [@norbjdk](https://github.com/norbjdk)  
**Środowisko:** Java 17+, JavaFX, AWT (`javax.imageio`, `java.awt.image`)

---

## 1. Źródła obrazów

Wszystkie obrazy użyte w projekcie są **autorstwa własnego** (Norbert Dominkiewicz).  
Zbiór zawiera 12–16 obrazów rastrowych kolorowych zróżnicowanych pod względem:

> Obrazy zostały wykonane i przetworzone wyłącznie na potrzeby niniejszego projektu dydaktycznego. </br>
> Obrazy do testów znajdują się w folderze images

---

## 2. Standaryzacja zbioru

### 2.1 Strategia

Przed analizą wszystkie obrazy zostały sprowadzone do wspólnego formatu:

- **Format wyjściowy:** PNG (bezstratny, brak artefaktów kompresji)
- **Wymiary:** 512 × 512 px
- **Metoda:** skalowanie proporcjonalne + centrowany crop

**Uzasadnienie wyboru strategii:**  
Proste skalowanie do kwadratu zniekształca proporcje obrazu (stretching), co fałszuje charakterystyki geometryczne i kolorystyczne. Skalowanie z zachowaniem proporcji do krótszego boku, a następnie centrowany crop, minimalizuje zniekształcenia i zachowuje centralną treść obrazu — najistotniejszą dla analizy. Minimalny rozmiar krótszego boku spełnia wymóg ≥ 256 px.

### 2.2 Kod — standaryzacja

```java
// PictureService.java — metoda standardize()

private static final int STD_SIZE = 512;

private BufferedImage standardize(BufferedImage src) {
    int srcW = src.getWidth();
    int srcH = src.getHeight();

    // Skalowanie proporcjonalne — krótszy bok = STD_SIZE
    double scale = (double) STD_SIZE / Math.min(srcW, srcH);
    int scaledW = (int) Math.round(srcW * scale);
    int scaledH = (int) Math.round(srcH * scale);

    BufferedImage scaled = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = scaled.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                       RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.drawImage(src, 0, 0, scaledW, scaledH, null);
    g.dispose();

    // Centrowany crop do STD_SIZE x STD_SIZE
    int cropX = Math.max(0, (scaledW - STD_SIZE) / 2);
    int cropY = Math.max(0, (scaledH - STD_SIZE) / 2);

    BufferedImage result = new BufferedImage(STD_SIZE, STD_SIZE, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = result.createGraphics();
    g2.drawImage(scaled.getSubimage(cropX, cropY,
                 Math.min(STD_SIZE, scaledW - cropX),
                 Math.min(STD_SIZE, scaledH - cropY)),
                 0, 0, STD_SIZE, STD_SIZE, null);
    g2.dispose();

    return result;
}
```

---

## 3. Analiza charakterystyk zbioru

### 3.1 Podpunkt A — Średnie R, G, B vs H, S, V na obraz

Dla każdego ustandaryzowanego obrazu obliczane są średnie wartości kanałów RGB oraz HSV iterując po wszystkich pikselach.

```java
// PictureService.java — fragment computeColorStats()

for (int y = 0; y < h; y++) {
    for (int x = 0; x < w; x++) {
        int rgb = img.getRGB(x, y);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8)  & 0xFF;
        int b =  rgb        & 0xFF;

        sumR += r;  sumG += g;  sumB += b;

        float[] hsv = Color.RGBtoHSB(r, g, b, null);
        sumH += hsv[0];  sumS += hsv[1];  sumV += hsv[2];
    }
}

stats.setMeanR(sumR / totalPixels);   // wartość 0–255
stats.setMeanG(sumG / totalPixels);
stats.setMeanB(sumB / totalPixels);
stats.setMeanH(sumH / totalPixels);   // H: 0.0–1.0 (odpowiada 0°–360°)
stats.setMeanS(sumS / totalPixels);   // S: 0.0–1.0
stats.setMeanV(sumV / totalPixels);   // V: 0.0–1.0
```

**Interpretacja:**  
Porównanie RGB vs HSV pozwala ocenić, czy zbiór jest zróżnicowany kolorystycznie (wysoka wariancja H), czy nasycony (wysokie S), oraz jak jasny jest przeciętnie (V). Zdjęcia mają tendencję do wyższego S i V niż schematy i szkice, które dominują w szarościach (niskie S).

Zbiorcze średnie dla całego zbioru wyznaczane są metodą `computeAverageColorSummary(List<ImageData>)`.

---

### 3.2 Podpunkt B — Entropia vs rozkłady intensywności (skala szarości)

Entropia Shannona mierzy „złożoność informacyjną" obrazu w skali szarości — im bardziej równomierny histogram, tym wyższa entropia.

```java
// PictureService.java — obliczenie entropii

double entropy = 0;
for (int i = 0; i < 256; i++) {
    double p = histGray[i];   // znormalizowana częstość (0.0–1.0)
    if (p > 0) entropy -= p * (Math.log(p) / Math.log(2));
}
stats.setEntropy(entropy);   // jednostka: bity, zakres praktyczny ~0–8
```

Histogramy szarości są obliczane jednocześnie z analizą pikseli:

```java
int gray = (r + g + b) / 3;
grayCount[gray]++;
// ...
histGray[i] = grayCount[i] / (double) totalPixels;
```

**Interpretacja:**  
Obrazy o entropii < 4 to zazwyczaj schematy blokowe i rysunki z dużymi jednobarwnymi obszarami (mało informacji). Zdjęcia i złożone grafiki osiągają entropię 6–8. Metoda `sortByEntropy()` sortuje zbiór rosnąco, co pozwala szybko zidentyfikować skrajne przypadki.

---

### 3.3 Podpunkt C — Wariancja jasności vs udział ciemnych / jasnych pikseli

```java
// PictureService.java — wariancja jasności (kanał V w HSV)

double sumBrightnessSq = 0;
int dark = 0, bright = 0;

// ... w pętli po pikselach:
float[] hsv = Color.RGBtoHSB(r, g, b, null);
double v = hsv[2];
sumBrightnessSq += v * v;
if (v < 0.3) dark++;    // piksel ciemny
if (v > 0.7) bright++;  // piksel jasny

// Po pętli:
double variance = (sumBrightnessSq / totalPixels) - (meanV * meanV);
stats.setBrightnessVariance(variance);
stats.setDarkPixelRatio(dark  / (double) totalPixels);
stats.setBrightPixelRatio(bright / (double) totalPixels);
```

**Interpretacja:**  
Wysoka wariancja jasności świadczy o dużym kontraście statystycznym (np. zdjęcia z cieniami i światłami). Niski udział ciemnych i jasnych pikseli przy niskiej wariancji sugeruje obraz „szary" — mało kontrastowy, np. pochmurne niebo. Schematy mają często skrajny rozkład: dominuje biel tła (wysoki `brightPixelRatio`) z cienkimi czarnymi liniami (niski `darkPixelRatio`).

---

## 4. Przetwarzanie wybranych obrazów

Do każdego z trzech poniższych przekształceń wybrano obraz, dla którego dana technika jest najbardziej uzasadniona merytorycznie.

### 4.1 Binaryzacja

**Technika:** progowanie stałe (threshold = 128) z luminancją ITU-R BT.601  
**Uzasadnienie wyboru obrazu:** schemat blokowy / rysunek techniczny — czytelne krawędzie i tekst po binaryzacji.  
**Format zapisu: JPG** — obraz czarno-biały po binaryzacji ma bardzo prostą strukturę, kompresja stratna JPEG jest efektywna przy tak niskiej entropii i nie powoduje widocznych artefaktów.

```java
// PictureService.java — applyBinarization()

for (int y = 0; y < src.getHeight(); y++) {
    for (int x = 0; x < src.getWidth(); x++) {
        int rgb = src.getRGB(x, y);
        // Luminancja wg ITU-R BT.601 (dokładniejsza niż (r+g+b)/3)
        int gray = ((rgb >> 16) & 0xFF) * 77
                 + ((rgb >>  8) & 0xFF) * 150
                 +  (rgb        & 0xFF) * 29;
        gray >>= 8;
        result.setRGB(x, y, gray > threshold ? 0xFFFFFF : 0x000000);
    }
}
```

> **Wynik:** plik `processed_binary_<nazwa>.jpg` + rozmiar w KB wyświetlony w EditorView.

---

### 4.2 Przekształcenie geometryczne — obrót

**Technika:** obrót o 45° z `AffineTransform`, interpolacja dwuliniowa  
**Uzasadnienie wyboru obrazu:** zdjęcie z wyraźną strukturą kierunkową (linie, krawędzie) — obrót uwidacznia asymetrię kompozycji.  
**Format zapisu: PNG** — rotacja z interpolacją wprowadza subtelne różnice jasności przy krawędziach; kompresja stratna JPEG tworzyłaby tu widoczne artefakty blokowe. PNG zachowuje pełną jakość.

```java
// PictureService.java — applyRotation()

double rad = Math.toRadians(angleDeg);
int newW = (int) Math.floor(w * Math.abs(Math.cos(rad)) + h * Math.abs(Math.sin(rad)));
int newH = (int) Math.floor(h * Math.abs(Math.cos(rad)) + w * Math.abs(Math.sin(rad)));

AffineTransform at = new AffineTransform();
at.translate((newW - w) / 2.0, (newH - h) / 2.0);
at.rotate(rad, w / 2.0, h / 2.0);

Graphics2D g = result.createGraphics();
g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                   RenderingHints.VALUE_INTERPOLATION_BILINEAR);
g.drawImage(src, at, null);
```

> **Wynik:** plik `processed_rotated_45deg_<nazwa>.png` + rozmiar w KB wyświetlony w EditorView.

---

### 4.3 Zmiana kontrastu — rozciąganie histogramu

**Technika:** liniowe rozciąganie histogramu (linear contrast stretch) — remapowanie zakresu [min, max] → [0, 255] dla każdego kanału RGB niezależnie  
**Uzasadnienie wyboru obrazu:** zdjęcie o niskim kontraście (np. zrobione w zachmurzeniu lub w cieniu) — rozciąganie ujawnia szczegóły ukryte w wąskim zakresie tonalnym.  
**Format zapisu: JPG** — zdjęcie z bogatymi gradientami barw kompresuje się efektywnie przez JPEG; straty wizualne są niezauważalne przy jakości > 85%.

```java
// PictureService.java — applyContrastStretch()

// Wyznaczenie globalnego min/max na szarości
int min = 255, max = 0;
for (int y = 0; y < src.getHeight(); y++) {
    for (int x = 0; x < src.getWidth(); x++) {
        int rgb  = src.getRGB(x, y);
        int gray = ((rgb >> 16) & 0xFF + (rgb >> 8) & 0xFF + (rgb & 0xFF)) / 3;
        if (gray < min) min = gray;
        if (gray > max) max = gray;
    }
}

// Rozciąganie każdego kanału
private int stretchChannel(int val, int min, int max) {
    if (max == min) return 128;
    return Math.min(255, Math.max(0, (val - min) * 255 / (max - min)));
}
```

> **Wynik:** plik `processed_contrast_<nazwa>.jpg` + rozmiar w KB wyświetlony w EditorView.

---

## 5. Podsumowanie narzędzi i uzasadnienie

| Narzędzie / biblioteka | Zastosowanie | Uzasadnienie |
|------------------------|--------------|--------------|
| `javax.imageio.ImageIO` | Wczytywanie i zapis obrazów | Standardowa biblioteka Java, obsługa JPG/PNG/BMP bez zależności zewnętrznych |
| `java.awt.image.BufferedImage` | Pikselowe obliczenia (histogramy, transformacje) | Bezpośredni dostęp do pikseli przez `getRGB()` / `setRGB()`, pełna kontrola nad algorytmami |
| `java.awt.Color.RGBtoHSB()` | Konwersja RGB → HSV | Wbudowana, dokładna konwersja; HSV intuicyjniejszy do analizy jasności i nasycenia |
| `java.awt.geom.AffineTransform` | Obrót geometryczny | Standardowe, numerycznie stabilne przekształcenie z obsługą interpolacji |
| `javafx.embed.swing.SwingFXUtils` | BufferedImage ↔ JavaFX Image | Mostek między logiką AWT a warstwą UI JavaFX |
| **PNG** (zapis standaryzacji i obrotu) | Bezstratny format wyjściowy | Brak artefaktów kompresji; wymagany przez polecenie dla standaryzacji; konieczny przy transformacjach geometrycznych |
| **JPEG** (zapis binaryzacji i kontrastu) | Kompresja stratna | Mały rozmiar pliku przy akceptowalnej jakości wizualnej dla tych typów przekształceń |
| Model kolorów **HSV** | Analiza jasności, nasycenia, odcienia | Bardziej intuicyjny niż RGB do statystycznej analizy obrazów; separuje chrominancję od luminancji |

---
