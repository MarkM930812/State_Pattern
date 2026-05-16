# Retro Kit Order Workflow

## Projektname

Retro Kit Order Workflow

## Kurzbeschreibung

Dieses Projekt ist ein eigenständiges Spring-Boot-System zur Verwaltung von Retro-Fußballtrikot-Bestellungen. In diesem Branch wird die Bestelllogik noch ohne gezielten Einsatz des State Patterns umgesetzt. Die Statuswechsel werden zentral in einer Service-Klasse behandelt.

## Verwendetes Pattern

Geplantes Pattern für die Refaktorisierung: State Pattern.

In diesem Branch ist das Pattern noch nicht umgesetzt. Der Branch dient als `without-pattern`-Ausgangsbasis.

## Problem ohne Pattern

Die Zustandslogik liegt in einer zentralen Service-Klasse und verwendet mehrere direkte Fallunterscheidungen für Aktionen wie Bezahlen, Verpacken, Versenden, Stornieren und Retournieren.

Typische Merkmale dieser Lösung:

- mehrere `if`-Abfragen in einer zentralen Klasse
- enge Kopplung zwischen Statusprüfung und Fachaktion
- neue Zustände erfordern Änderungen an bestehendem Code
- die Service-Klasse wächst schnell mit jeder neuen Anforderung

## Lösung mit Pattern

Im späteren `with-pattern`-Branch wird dieselbe Fachlogik in einzelne Zustandsklassen ausgelagert. Dadurch werden Statuswechsel nicht mehr zentral entschieden, sondern über zustandsspezifische Klassen gesteuert.

Geplante Zustände:

- `CREATED`
- `PAID`
- `PACKED`
- `SHIPPED`
- `CANCELLED`
- `RETURNED`

## REST-Endpunkte

- `POST /api/orders` erstellt eine neue Bestellung
- `GET /api/orders` liefert alle Bestellungen
- `GET /api/orders/{orderId}` liefert eine Bestellung nach ID
- `GET /api/orders/status/{status}` filtert Bestellungen nach Status
- `POST /api/orders/{orderId}/pay` bezahlt eine Bestellung
- `POST /api/orders/{orderId}/pack` verpackt eine Bestellung
- `POST /api/orders/{orderId}/ship` versendet eine Bestellung
- `POST /api/orders/{orderId}/cancel` storniert eine Bestellung
- `POST /api/orders/{orderId}/return` retourniert eine Bestellung

## Erweiterung, die im Pattern-Branch einfacher umsetzbar ist

Die Erweiterung `RETURNED` eignet sich gut als Vergleichspunkt. In dieser Variante ohne Pattern muss die zentrale Service-Klasse erweitert werden. Im `with-pattern`-Branch wird diese Änderung lokaler und sauberer in den Zustandsklassen gekapselt.

## Technische Umsetzung

- Java 21
- Spring Boot 3.3.5
- Maven
- Spring Web
- Spring Data JPA
- H2-Datenbank
- JUnit 5
- MockMvc

## Kurzer KI-Reflexionsabschnitt

Generative KI half bei der Analyse der Designprobleme dieser Ausgangslösung, bei der Formulierung eines Refactoring-Plans und bei der Auswahl passender Testfälle. Die fachliche Bewertung der Fallunterscheidungen und die konkrete Struktur des Zielsystems wurden manuell überprüft.