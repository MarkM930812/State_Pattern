# Code-Erklärung für den Branch without-pattern

Diese Datei erklärt den kompletten Code des Branches `without-pattern`.

## Ziel dieses Branches

Dieser Branch zeigt eine lauffähige Ausgangslösung ohne gezielten Einsatz des State Patterns. Die Zustandslogik der Bestellung ist funktional korrekt, aber zentral in einer Service-Klasse gebündelt. Genau das ist später der Ausgangspunkt für das Refactoring.

## Paketübersicht

### `at.itkolleg.statepattern`

- `StatePatternKitOrdersApplication.java`
  Startklasse von Spring Boot. Von hier aus wird die Anwendung hochgefahren und der gesamte Spring-Kontext aufgebaut.

### `at.itkolleg.statepattern.order.controller`

- `KitOrderController.java`
  Stellt die REST-Endpunkte bereit. Der Controller enthält bewusst nur dünne HTTP-Logik und delegiert die eigentliche Fachverarbeitung an den Service.

### `at.itkolleg.statepattern.order.service`

- `KitOrderService.java`
  Zentrale Fachklasse des Branches. Hier liegt die komplette Zustandslogik für `create`, `pay`, `pack`, `ship`, `cancel` und `returnOrder`.
  Genau diese Bündelung ist das typische Merkmal der Lösung ohne Pattern.

### `at.itkolleg.statepattern.order.model`

- `KitOrder.java`
  JPA-Entity für eine Bestellung. Sie speichert Kundendaten, Produktdaten, Status und Zeitstempel für die wichtigsten Fachereignisse.

- `OrderStatus.java`
  Enum mit allen fachlichen Zuständen der Bestellung.

### `at.itkolleg.statepattern.order.dto`

- `CreateKitOrderRequest.java`
  Request-DTO für das Anlegen einer Bestellung. Enthält auch Bean-Validation-Regeln.

- `ShipOrderRequest.java`
  Request-DTO für die Versandaktion. Enthält die Tracking-Nummer.

- `ReasonRequest.java`
  Request-DTO für Storno- und Retourgründe.

- `KitOrderResponse.java`
  Response-DTO der API. Diese Klasse mappt die Entity auf ein stabiles Antwortformat für REST.

### `at.itkolleg.statepattern.order.repository`

- `KitOrderRepository.java`
  Spring-Data-JPA-Repository für Datenbankzugriffe. Neben den Standardmethoden gibt es eine Abfrage nach Status.

### `at.itkolleg.statepattern.order.exception`

- `ApiErrorResponse.java`
  Einheitliches Fehlerformat für REST-Antworten.

- `OrderNotFoundException.java`
  Fachliche Exception, wenn eine Bestellung nicht existiert.

- `InvalidOrderStateTransitionException.java`
  Fachliche Exception für ungültige Statuswechsel.

- `RestExceptionHandler.java`
  Globale Fehlerbehandlung für die API. Sie wandelt Exceptions in klare HTTP-Antworten um.

### `at.itkolleg.statepattern.order` im Testbereich

- `KitOrderControllerIntegrationTest.java`
  Integrationstest für die REST-Endpunkte und den kompletten Bestellablauf.

### `at.itkolleg.statepattern` im Testbereich

- `StatePatternKitOrdersApplicationTests.java`
  Minimaler Spring-Boot-Kontexttest, der prüft, ob die Anwendung grundsätzlich startet.

## Ablauf einer Bestellung in diesem Branch

1. `POST /api/orders` erzeugt eine Bestellung im Status `CREATED`.
2. `POST /api/orders/{id}/pay` prüft im Service per `if`, ob der Status `CREATED` ist.
3. `POST /api/orders/{id}/pack` prüft per `if`, ob der Status `PAID` ist.
4. `POST /api/orders/{id}/ship` prüft per `if`, ob der Status `PACKED` ist.
5. `POST /api/orders/{id}/cancel` prüft mehrere Statuskombinationen zentral.
6. `POST /api/orders/{id}/return` prüft per `if`, ob der Status `SHIPPED` ist.

## Warum ist das eine without-pattern-Lösung?

Die Zustandsregeln sind vollständig in `KitOrderService.java` konzentriert. Das ist für kleine Beispiele noch verständlich, hat aber typische Nachteile:

- viele zentrale Fallunterscheidungen
- hohe Kopplung zwischen Status und Aktion
- steigender Änderungsaufwand bei neuen Zuständen
- größere Gefahr, bestehende Logik bei Erweiterungen zu beschädigen

## Was wird im with-pattern-Branch besser?

Im `with-pattern`-Branch werden diese Regeln in eigene Zustandsklassen ausgelagert. Dadurch muss die zentrale Service-Klasse nicht mehr alle Übergänge selbst kennen.