# Java Grund – Laboration 3

## Enterprise Warehouse Management System

Detta projekt är en Spring Boot-applikation för lagerhantering.

Laborationen genomfördes som ett grupparbete och fokuserar på enhetstester, Java Streams, funktionell programmering och REST API.

Applikationen hanterar produkter i ett lager och använder en skiktad arkitektur med Controller, Service, Repository och Model.

---

## Tekniker

Projektet använder bland annat:

- Java
- Spring Boot
- Maven
- Java Collections
- Java Streams
- REST API
- JUnit 5
- Mockito
- Git
- GitHub

---

## Projektstruktur

Applikationen är uppdelad i flera lager.

### Model

`Product` representerar en produkt i lagret och innehåller bland annat:

- id
- namn
- kategori
- pris
- lagersaldo
- registreringsdatum

### Repository

`ProductRepository` ansvarar för lagring och hämtning av produkter.

Lagringen använder en trådsäker datastruktur för att fungera i en webbapplikation där flera anrop kan ske samtidigt.

### Service

`WarehouseService` innehåller applikationens affärslogik.

Här finns funktionalitet för bland annat:

- hämtning av alla produkter
- filtrering efter kategori
- filtrering efter lågt lagersaldo
- sortering efter pris
- sortering efter lagersaldo/popularitet
- beräkning av totalt lagervärde
- beräkning av medelpris per kategori

### Controller

Controller-lagret exponerar applikationens funktionalitet genom ett REST API.

Applikationen innehåller CRUD-funktionalitet för produkter samt endpoints för sökning, filtrering, sortering och analys.

### Exception handling

Applikationen använder en global exception handler för att hantera ogiltiga parametrar.

Exempelvis returneras:

`HTTP 400 Bad Request`

om användaren skickar ett ogiltigt värde, exempelvis ett negativt threshold-värde.

---

## CRUD-funktionalitet

Applikationen stödjer CRUD-operationer:

- **Create** – skapa en produkt
- **Read** – hämta produkter
- **Update** – uppdatera en produkt
- **Delete** – ta bort en produkt

REST API används för kommunikationen mellan klient och applikation.

---

## Sökning och filtrering med Java Streams

Java Streams används för att bearbeta produktdata på ett deklarativt sätt.

### Filtrering efter kategori

Metoden:

`findByCategory()`

använder `stream()` och `filter()` för att hitta produkter inom en angiven kategori.

Sökningen är inte känslig för stora och små bokstäver.

### Filtrering efter lågt lagersaldo

Metoden:

`findLowStockProducts()`

filtrerar fram produkter vars lagersaldo ligger under en angiven gräns.

Ett negativt threshold-värde accepteras inte och resulterar i ett `IllegalArgumentException`, som API:t mappar till HTTP 400.

---

## Sortering

Java Streams och `sorted()` används för att sortera produkter.

Projektet innehåller funktionalitet för att bland annat hitta:

- de dyraste produkterna
- de billigaste produkterna
- produkter med högst lagersaldo
- produkter med lägst lagersaldo

Metoderna kan begränsa resultatet till ett angivet antal produkter.

---

## Analys och aggregering

### Totalt lagervärde

Metoden:

`calculateTotalWarehouseValue()`

beräknar det totala värdet för lagret.

För varje produkt beräknas:

`quantity × price`

Resultaten summeras sedan med Java Streams.

### Medelpris per kategori

Metoden:

`getAveragePricePerCategory()`

grupperar produkter efter kategori och beräknar medelpriset för varje kategori.

Produkter med `null` eller tom kategori filtreras bort.

---

## Enhetstester

Affärslogiken testas med JUnit 5 och Mockito.

Mockito används för att mocka `ProductRepository`, vilket gör att `WarehouseService` kan testas isolerat utan att vara beroende av den riktiga lagringen.

Tester omfattar både vanliga fall och edge cases.

Exempel på tester:

- hämtning av alla produkter
- sökning efter kategori
- kategori som inte finns
- sökning med stora och små bokstäver
- tom kategori
- filtrering efter lågt lagersaldo
- negativ threshold
- sortering efter pris och lagersaldo
- totalt lagervärde
- tomt lager
- medelpris per kategori
- produkter med `null` eller tom kategori
- HTTP 400-hantering

I den senaste lokala testkörningen kördes 22 tester med:

- 0 failures
- 0 errors
- 0 skipped

och Maven avslutade med:

`BUILD SUCCESS`

---

## HTTP 400 och felhantering

Projektet använder:

`GlobalExceptionHandler`

för att fånga `IllegalArgumentException`.

Tidigare kunde exempelvis:

`/api/products/low-stock?threshold=-1`

resultera i:

`HTTP 500 Internal Server Error`

Efter förbättringen returnerar API:t i stället:

`HTTP 400 Bad Request`

med ett tydligt felmeddelande.

Exempel:

```json
{
  "error": "Threshold cannot be negative"
}
```

Det gör att klientfel skiljs från interna serverfel och ger tydligare återkoppling till den som använder API:t.

---

## Exempel på API-anrop

### Filtrera produkter efter kategori

```text
GET /api/products/category/Electronics
```

### Hämta produkter med lågt lagersaldo

```text
GET /api/products/low-stock?threshold=5
```

### Ogiltigt threshold-värde

```text
GET /api/products/low-stock?threshold=-1
```

Det sista anropet returnerar:

```text
HTTP 400 Bad Request
```

---

## Köra projektet

### Förutsättningar

För att köra projektet behövs:

- Java
- Maven

### Kör alla tester

Öppna terminalen i projektets rotmapp och kör:

```bash
mvn clean test
```

Om testerna lyckas visas bland annat:

```text
BUILD SUCCESS
```

### Starta Spring Boot-applikationen

Kör:

```bash
mvn spring-boot:run
```

När applikationen har startat kan den nås lokalt via:

```text
http://localhost:8080
```

Servern stoppas med:

```text
Ctrl + C
```

---

## Reflektion – Spring Boot

Spring Boot förenklar utvecklingen av Java-baserade webbapplikationer genom att mycket konfiguration hanteras automatiskt.

Annoteringar som:

- `@SpringBootApplication`
- `@RestController`
- `@Service`
- `@Repository`
- `@RestControllerAdvice`

gör det tydligt vilken funktion olika klasser har i applikationen.

En viktig del i Spring Boot är dependency injection. I vårt projekt injiceras exempelvis `ProductRepository` i `WarehouseService` genom konstruktorn. Det minskar kopplingen mellan klasserna och gör koden enklare att testa.

Detta blev särskilt tydligt när vi använde Mockito. Eftersom `ProductRepository` kunde mockas gick det att testa `WarehouseService` isolerat utan att använda den riktiga lagringen.

Spring Boot innehåller också en inbyggd webbserver. Det innebär att applikationen kan startas direkt med Maven utan att en separat applikationsserver först behöver installeras och konfigureras.

Under laborationen blev det även tydligt att Spring Boots struktur hjälper till att separera olika ansvarsområden. Controller hanterar HTTP-anrop, Service innehåller affärslogiken och Repository ansvarar för datahantering.

Denna uppdelning gör applikationen enklare att förstå, testa, underhålla och vidareutveckla.

Spring Boot kan kännas omfattande i början eftersom ramverket innehåller många annoteringar och koncept. När strukturen blir tydligare ger det däremot ett organiserat arbetssätt som passar bra för större backendapplikationer.

---

## Java/Spring Boot jämfört med Node.js

Java/Spring Boot och Node.js kan båda användas för att utveckla backendapplikationer och REST API:er, men de skiljer sig i arbetssätt och struktur.

### Java och Spring Boot

Java är statiskt typat. Många typer av fel kan därför upptäckas redan vid kompilering.

Spring Boot erbjuder en tydlig och etablerad struktur med exempelvis:

- Controller
- Service
- Repository
- dependency injection
- centraliserad felhantering

Den strukturen passar bra i större projekt där tydlig arkitektur och långsiktig underhållbarhet är viktiga.

En nackdel är att Spring Boot kan upplevas som mer omfattande i början eftersom utvecklaren behöver lära sig flera koncept, annoteringar och ramverksfunktioner.

### Node.js

Node.js använder JavaScript och har ofta en mer flexibel struktur.

Det kan gå snabbt att komma igång med Node.js och det passar bra för händelsestyrda och asynkrona applikationer.

En fördel är också att samma programmeringsspråk kan användas både på frontend och backend om resten av projektet använder JavaScript.

Den friare strukturen kan samtidigt innebära att utvecklingsteamet själv behöver vara mer noggrant med hur projektet organiseras.

### Likheter

Både Spring Boot och Node.js kan användas för att:

- skapa REST API:er
- hantera HTTP-anrop
- arbeta med JSON
- bygga backendtjänster
- implementera CRUD
- skapa felhantering
- skriva automatiserade tester

### Skillnader

Spring Boot bygger på Java och erbjuder stark typning och en tydlig ramverksstruktur.

Node.js bygger vanligtvis på JavaScript och erbjuder större flexibilitet och ett enklare sätt att snabbt komma igång.

Vilken teknik som passar bäst beror på projektets krav, storlek och utvecklingsteamets erfarenhet.

---

## Grupparbete och GitHub

Laborationen genomfördes som ett grupparbete.

Vi använde Git och GitHub för versionshantering och för att kunna arbeta parallellt med olika delar av projektet.

Arbetet delades upp i olika områden, bland annat:

- Spring Boot och grundstruktur
- sökning och filtrering
- sortering
- analys och aggregering
- enhetstester
- felhantering
- dokumentation

Arbetsflödet såg huvudsakligen ut så här:

1. Uppdatera `main`.
2. Skapa en separat branch för en funktion, ett test eller en fix.
3. Implementera ändringen.
4. Köra tester lokalt.
5. Göra commit.
6. Pusha branchen till GitHub.
7. Skapa en Pull Request.
8. Granska ändringarna.
9. Merga Pull Request till `main`.
10. Uppdatera den lokala `main`.
11. Köra hela testsviten igen efter merge.

Detta arbetssätt minskade risken för konflikter och gjorde det möjligt att kontrollera ändringar innan de blev en del av den gemensamma versionen.

Under arbetet användes även automatiska kontroller i Pull Requests för att upptäcka möjliga problem och förbättringar.

Ett konkret exempel var felhanteringen för negativa threshold-värden. Ett anrop som tidigare resulterade i HTTP 500 ändrades så att det i stället returnerar HTTP 400.

---

## Sammanfattning

Laborationen visar hur Spring Boot, Java Streams och enhetstester kan kombineras i en lagerhanteringsapplikation.

Projektet innehåller bland annat:

- skiktad arkitektur
- REST API
- CRUD
- sökning och filtrering
- sortering
- analys och aggregering
- Java Streams
- JUnit 5
- Mockito
- global felhantering
- HTTP-statuskoder
- Git
- GitHub
- branches
- Pull Requests

Laborationen har gett praktisk erfarenhet av att utveckla, testa och integrera flera delar av en Spring Boot-applikation i ett gemensamt gruppprojekt.