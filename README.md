# Retro Kit Order Workflow

## Projektname

Retro Kit Order Workflow

## Kurzbeschreibung

Dieses Projekt ist ein eigenständiges Spring-Boot-System zur Verwaltung von Retro-Fußballtrikot-Bestellungen. Der Bestellprozess wird mit dem State Pattern modelliert, damit zustandsabhängiges Verhalten nicht in einer großen Service-Klasse mit `if`- oder `switch`-Blöcken landet.

## Verwendetes Pattern

Verwendet wird das State Pattern.

Die Zustände der Bestellung sind:

- `CREATED`
- `PAID`
- `PACKED`
- `SHIPPED`
- `CANCELLED`
- `RETURNED`

Für jeden relevanten Zustand existiert eine eigene Klasse. Diese Klassen kapseln die erlaubten Übergänge und verhindern ungültige Aktionen direkt an der fachlichen Stelle.

## Problem ohne Pattern

Ohne State Pattern würde die Logik typischerweise in einer zentralen Service-Klasse landen, zum Beispiel:

- `if (status == CREATED) { ... }`
- `else if (status == PAID) { ... }`
- `switch (status) { ... }`

Nachteile davon:

- viele Fallunterscheidungen in einer einzigen Klasse
- hohe Kopplung zwischen Status und Aktion
- neue Zustände erfordern Änderungen an bestehendem Code
- höhere Gefahr für Regressionen
- Tests werden schwerer lesbar, weil viele Sonderfälle in einer Methode stecken

## Lösung mit Pattern

Im aktuellen Projekt wird jede Zustandslogik in eine eigene Klasse ausgelagert:

- `CreatedOrderState`
- `PaidOrderState`
- `PackedOrderState`
- `ShippedOrderState`
- `CancelledOrderState`
- `ReturnedOrderState`

Die zentrale Service-Klasse fragt nur noch den passenden State ab und delegiert die Aktion dorthin. Dadurch bleibt die Orchestrierung im Service schlank, während die Fachregeln in den Zustandsklassen liegen.

## Architekturüberblick

- `KitOrderController`: REST-Endpunkte
- `KitOrderService`: zentrale Service-Klasse
- `OrderStateFactory`: liefert die passende State-Implementierung
- `OrderState`: gemeinsames Interface aller Zustände
- `KitOrder`: Entity und Datenmodell
- `KitOrderRepository`: Persistenz über Spring Data JPA
- `RestExceptionHandler`: konsistente API-Fehlerantworten

## REST-Endpunkte

### Grundfunktionen

- `POST /api/orders` erstellt eine neue Bestellung
- `GET /api/orders` liefert alle Bestellungen
- `GET /api/orders/{orderId}` liefert eine Bestellung nach ID
- `GET /api/orders/status/{status}` filtert Bestellungen nach Status

### Zustandsübergänge

- `POST /api/orders/{orderId}/pay`
- `POST /api/orders/{orderId}/pack`
- `POST /api/orders/{orderId}/ship`
- `POST /api/orders/{orderId}/cancel`
- `POST /api/orders/{orderId}/return`

## Beispielablauf

1. Bestellung anlegen
2. Bestellung bezahlen
3. Bestellung verpacken
4. Bestellung versenden
5. Optional: Bestellung retournieren

Zulässige Übergänge:

- `CREATED -> PAID`
- `CREATED -> CANCELLED`
- `PAID -> PACKED`
- `PAID -> CANCELLED`
- `PACKED -> SHIPPED`
- `PACKED -> CANCELLED`
- `SHIPPED -> RETURNED`

## Erweiterung, die im Pattern-Branch einfacher ist

Die Erweiterung `RETURNED` zeigt den Vorteil des Patterns sehr gut. Um die Retoure zu ergänzen, mussten nur folgende Stellen angepasst werden:

- `OrderStatus` um `RETURNED` erweitern
- `ShippedOrderState` um `returnOrder(...)` ergänzen
- `ReturnedOrderState` anlegen
- REST-Endpunkt für `/return` bereitstellen

Bei einer switch-basierten Lösung müsste dieselbe Erweiterung meist an mehreren Stellen in Service-Methoden nachgezogen werden.

## Technische Umsetzung

- Java 21
- Spring Boot 3.3.5
- Maven
- Spring Web
- Spring Data JPA
- H2-Datenbank
- JUnit 5
- MockMvc

## Tests

Im Projekt sind mehr als 6 Tests enthalten.

Abgedeckt werden unter anderem:

- erfolgreiche Erstellung einer Bestellung
- gültige Zustandsübergänge
- ungültige Zustandsübergänge
- Filterung nach Status
- Validierungsfehler
- 404 bei nicht vorhandenen Bestellungen
- direkte Unit-Tests einzelner State-Klassen

## Projekt starten

```bash
mvn spring-boot:run
```

Tests ausführen:

```bash
mvn test
```

## Kurzer KI-Reflexionsabschnitt

Generative KI war hilfreich beim Strukturieren des Projekts, beim Formulieren einer sauberen State-Architektur und beim Ableiten sinnvoller Testfälle. Die endgültige Umsetzung wurde aber fachlich überarbeitet, insbesondere bei den Zustandsübergängen, den API-Endpunkten und der Dokumentation für die Präsentation.