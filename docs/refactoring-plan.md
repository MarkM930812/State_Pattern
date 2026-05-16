# Refactoring-Plan von without-pattern zu with-pattern

## Ziel

Die zentrale Zustandslogik aus `KitOrderService` soll in eigenständige Zustandsklassen ausgelagert werden, damit neue Status und Übergänge einfacher ergänzt werden können.

## Ausgangspunkt im without-pattern-Branch

Die Service-Klasse enthält direkte Fallunterscheidungen für:

- `pay(...)`
- `pack(...)`
- `ship(...)`
- `cancel(...)`
- `returnOrder(...)`

Dadurch hängt die gesamte Fachlogik an einer Klasse.

## Geplante Refaktorisierungsschritte

1. Gemeinsames Interface `OrderState` einführen.
2. Für jeden Status eine eigene Klasse anlegen.
3. Statusabhängige Aktionen aus `KitOrderService` in diese Klassen verschieben.
4. Eine `OrderStateFactory` hinzufügen, die den aktuellen Status auf die passende Klasse abbildet.
5. `KitOrderService` auf Orchestrierung reduzieren.
6. Zusätzliche Unit-Tests für die Zustandsklassen ergänzen.

## Zielklassen im with-pattern-Branch

- `OrderState`
- `CreatedOrderState`
- `PaidOrderState`
- `PackedOrderState`
- `ShippedOrderState`
- `CancelledOrderState`
- `ReturnedOrderState`
- `OrderStateFactory`

## Erwartete Vorteile

- weniger Fallunterscheidungen in der Service-Klasse
- bessere Trennung von Verantwortlichkeiten
- höhere Erweiterbarkeit
- klarere Tests auf Zustandsniveau

## Erwartete Nachteile

- mehr Klassen
- höherer Strukturaufwand
- etwas mehr Einarbeitung für kleine Teams