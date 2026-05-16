# AI_USAGE.md

## 1. Verwendete KI-Werkzeuge

Verwendete Tools:

- GitHub Copilot
- ChatGPT
- Claude

## 2. Wichtige Prompts

Prompt 1:

Erkläre mir das State Pattern anhand eines Spring-Boot-Bestellsystems mit Zuständen wie Created, Paid, Packed, Shipped und Returned.

Prompt 2:

Analysiere eine Bestellung, die ihren Status über viele `if`-Abfragen in einer Service-Klasse verwaltet. Welche Designprobleme entstehen daraus?

Prompt 3:

Erstelle einen Refactoring-Plan, um ein switch-basiertes Bestellsystem in Spring Boot auf das State Pattern umzustellen. Berücksichtige REST Controller, Services, Entity, Repository und Tests.

Prompt 4:

Schlage sinnvolle Unit- und Integrationstests für ein State-Pattern-Bestellsystem vor, inklusive ungültiger Zustandsübergänge.

Prompt 5:

Vergleiche eine `without-pattern`-Lösung mit einer `with-pattern`-Lösung für ein Spring-Boot-Bestellsystem. Erkläre Unterschiede bei Erweiterbarkeit, Testbarkeit und Verantwortungstrennung.

Prompt 6:

Welche Erweiterung eignet sich gut, um den Vorteil des State Patterns zu demonstrieren, ohne das Gesamtsystem künstlich zu verkomplizieren?

## 3. Bewertung der KI-Antworten

### Antwort A

Was war hilfreich?

- Die KI hat die Grundidee des State Patterns am Bestellprozess sehr klar erklärt.
- Die Zustände `Created`, `Paid`, `Packed` und `Shipped` waren eine gute fachliche Basis.

Was war falsch oder unpassend?

- Teilweise wurden nur theoretische UML-Beschreibungen geliefert, aber keine sinnvolle Einbettung in Spring Boot.

Was haben wir geändert?

- Wir haben die Lösung konkret auf REST-Endpunkte, DTOs, JPA und Fehlerbehandlung übertragen.

Was haben wir selbst entschieden?

- Wir haben die fachliche Erweiterung `RETURNED` bewusst ergänzt, um den Vorteil des Patterns praktisch zu zeigen.

### Antwort B

Was war hilfreich?

- Die KI hat vorgeschlagen, eine zentrale Factory für die State-Objekte zu verwenden.
- Dadurch blieb die Service-Klasse schlanker.

Was war falsch oder unpassend?

- Ein Vorschlag arbeitete mit manueller Objekt-Erzeugung statt mit Spring-Beans, was nicht gut zur Spring-Architektur gepasst hätte.

Was haben wir geändert?

- Wir haben die Zustände als Spring-Komponenten registriert und über eine Factory per Status aufgelöst.

Was haben wir selbst entschieden?

- Die Service-Klasse sollte nur noch orchestrieren und keine fachlichen `if`-Abfragen enthalten.

### Antwort C

Was war hilfreich?

- Die KI lieferte gute Testideen für positive und negative Zustandswechsel.

Was war falsch oder unpassend?

- Einige vorgeschlagene Tests prüften nur HTTP-Statuscodes, aber nicht die konkrete Statusänderung im Fachmodell.

Was haben wir geändert?

- Wir haben zusätzlich direkte Unit-Tests für einzelne State-Klassen erstellt.

Was haben wir selbst entschieden?

- Sowohl Integrationstests als auch Unit-Tests sind nötig, um das Pattern fachlich sauber nachzuweisen.

## 4. Reflexion

Hat euch KI geholfen, das Pattern besser zu verstehen?

- Ja. Besonders hilfreich war die Aufteilung zwischen Zustandsklassen, zentralem Service und REST-API.

Wo hat KI oberflächliche oder falsche Vorschläge gemacht?

- Bei zu generischen Architekturvorschlägen ohne konkrete Spring-Boot-Einbettung.
- Bei einzelnen Ideen, die zu viele Verantwortungen wieder im Service gesammelt hätten.

Welche Teile des Codes könnt ihr selbst erklären?

- die Zustandsübergänge zwischen `CREATED`, `PAID`, `PACKED`, `SHIPPED`, `CANCELLED` und `RETURNED`
- die Rolle der `OrderStateFactory`
- die Service-Orchestrierung in `KitOrderService`
- die REST-Endpunkte im Controller
- die Unterschiede zwischen Unit-Tests und Integrationstests