# Vergleich zwischen without-pattern und with-pattern

## without-pattern

Merkmale der Lösung ohne Pattern:

- Zustandslogik in einer großen Service-Klasse
- viele `if`- oder `switch`-Abfragen
- neue Zustände führen zu Änderungen an mehreren Stellen
- schwieriger zu testen, weil viele Fälle in wenigen Methoden stecken

## with-pattern

Merkmale der aktuellen Lösung:

- jeder Status besitzt eine eigene Klasse
- der zentrale Service delegiert nur noch an den aktuellen State
- ungültige Übergänge werden direkt im jeweiligen Zustand verhindert
- das Hinzufügen neuer Zustände wird lokaler und kontrollierter

## Wichtige Änderungen bei der Refaktorisierung

Statt einer zentralen Fallunterscheidung wurden folgende Bausteine eingeführt:

- `OrderState` als gemeinsames Interface
- konkrete Zustandsklassen wie `CreatedOrderState` und `ShippedOrderState`
- `OrderStateFactory` zur Auswahl des passenden States
- `KitOrderService` als schlanke Orchestrierungsschicht

## Warum verbessert das die Erweiterbarkeit?

- Neue Zustände benötigen vor allem neue Klassen statt Änderungen in vielen alten Methoden.
- Fachlogik ist näher an der Stelle, an der sie semantisch hingehört.
- Bestehender Code muss seltener geöffnet und verändert werden.

## Welche Nachteile entstehen durch das Pattern?

- Mehr Klassen und etwas mehr Strukturaufwand
- Für kleine Systeme zunächst höherer Einstiegsoverhead
- Das Zusammenspiel zwischen Service, Factory und State-Klassen muss verstanden werden

## Fazit

Das State Pattern lohnt sich dann, wenn Objekte einen klaren Lebenszyklus mit mehreren Zuständen und zustandsabhängigen Regeln besitzen. Bei genau diesem Bestellsystem ist das Pattern sinnvoll, weil die Bestellungen nicht nur Daten speichern, sondern sich fachlich unterschiedlich verhalten.