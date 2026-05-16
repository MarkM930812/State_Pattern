# Code-Erklärung für den Branch with-pattern

Diese Datei erklärt den kompletten Code des Branches `with-pattern`.

## Ziel dieses Branches

Dieser Branch zeigt die refaktorierte Lösung mit State Pattern. Die Zustandslogik der Bestellung ist nicht mehr zentral in einer einzigen Service-Klasse gebündelt, sondern auf mehrere State-Klassen verteilt.

## Zentrale Idee des Branches

Die Bestellung bleibt weiterhin das zentrale Fachobjekt. Der Unterschied zur Ausgangslösung besteht darin, dass Aktionen wie Bezahlen, Verpacken, Versenden oder Retournieren an zustandsspezifische Klassen delegiert werden.

## Paketübersicht

### `at.itkolleg.statepattern`

- `StatePatternKitOrdersApplication.java`
  Startklasse der Spring-Boot-Anwendung.

### `at.itkolleg.statepattern.order.controller`

- `KitOrderController.java`
  Stellt die REST-Endpunkte bereit und delegiert die Verarbeitung an den Service.

### `at.itkolleg.statepattern.order.service`

- `KitOrderService.java`
  Schlanke Orchestrierungsschicht. Der Service lädt Bestellungen, führt allgemeine Aufgaben wie Mapping oder Persistenz aus und delegiert Statusaktionen an den aktuellen State.

- `OrderStateFactory.java`
  Baut eine Zuordnung von `OrderStatus` zu `OrderState` auf. Dadurch muss der Service nicht selbst wissen, welche Klasse zu welchem Status gehört.

### `at.itkolleg.statepattern.order.state`

- `OrderState.java`
  Gemeinsames Interface aller Zustandsklassen.

- `AbstractOrderState.java`
  Basisklasse mit Standardverhalten für ungültige Übergänge.

- `CreatedOrderState.java`
  Erlaubt Bezahlen und Stornieren einer neu angelegten Bestellung.

- `PaidOrderState.java`
  Erlaubt Verpacken und Stornieren nach erfolgreicher Zahlung.

- `PackedOrderState.java`
  Erlaubt Versand und Stornierung einer verpackten Bestellung.

- `ShippedOrderState.java`
  Erlaubt die Retoure einer versendeten Bestellung.

- `CancelledOrderState.java`
  Endzustand ohne weitere erlaubte Aktionen.

- `ReturnedOrderState.java`
  Endzustand nach einer Retoure.

### `at.itkolleg.statepattern.order.model`

- `KitOrder.java`
  Entity für Bestellungen mit Status und Zeitstempeln.

- `OrderStatus.java`
  Enum aller fachlichen Zustände.

### `at.itkolleg.statepattern.order.dto`

- `CreateKitOrderRequest.java`
  Eingabe-DTO für neue Bestellungen.

- `ShipOrderRequest.java`
  Eingabe-DTO für Tracking-Nummern beim Versand.

- `ReasonRequest.java`
  Eingabe-DTO für Storno- und Retourgründe.

- `KitOrderResponse.java`
  Ausgabe-DTO mit allen wichtigen Bestelldaten.

### `at.itkolleg.statepattern.order.repository`

- `KitOrderRepository.java`
  Spring-Data-Repositorium für Bestellungen.

### `at.itkolleg.statepattern.order.exception`

- `ApiErrorResponse.java`
  Standardisiertes Fehlerformat der API.

- `OrderNotFoundException.java`
  Wird geworfen, wenn eine Bestellung nicht existiert.

- `InvalidOrderStateTransitionException.java`
  Signalisiert ungültige Fachübergänge.

- `RestExceptionHandler.java`
  Globale Fehlerbehandlung und Mapping auf HTTP-Antworten.

### Testpakete

- `KitOrderControllerIntegrationTest.java`
  Prüft die REST-API und den gesamten Bestellablauf im integrierten Test.

- `OrderStateUnitTest.java`
  Prüft die State-Klassen direkt und isoliert.

- `StatePatternKitOrdersApplicationTests.java`
  Prüft, ob der Spring-Kontext startet.

## Ablauf einer Bestellung in diesem Branch

1. `POST /api/orders` legt eine Bestellung mit `CREATED` an.
2. Der Service holt bei jeder Statusaktion über die `OrderStateFactory` die passende State-Klasse.
3. Der aktuelle State entscheidet, ob die Aktion erlaubt ist und wie die Bestellung verändert wird.
4. Ungültige Übergänge werden über die Basisklasse oder konkrete State-Klassen abgefangen.

## Warum ist das eine with-pattern-Lösung?

Die Zustandslogik ist auf spezialisierte Klassen verteilt. Dadurch gilt:

- weniger zentrale Fallunterscheidungen
- bessere Trennung der Verantwortlichkeiten
- einfachere Erweiterung um neue Zustände
- klarere Unit-Tests auf Klassenebene

## Wichtigster Unterschied zum without-pattern-Branch

Im `without-pattern`-Branch kennt `KitOrderService` alle Statusregeln selbst. Im `with-pattern`-Branch kennt der Service nur noch den Delegationsmechanismus. Die eigentlichen Regeln leben in den State-Klassen.