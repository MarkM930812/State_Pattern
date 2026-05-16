# Präsentationsleitfaden

## 1. Ausgangsproblem

- Eine Bestellung durchläuft mehrere Zustände.
- Ohne Pattern landet die Logik schnell in `if`- oder `switch`-Blöcken.

## 2. Kurze Erklärung des State Patterns

- Verhalten hängt vom aktuellen Zustand ab.
- Jeder Zustand wird als eigene Klasse modelliert.

## 3. Demo without-pattern

- zeigen, wie eine zentrale Service-Klasse mit Fallunterscheidungen aussehen würde
- Probleme bei Erweiterung und Testbarkeit nennen

## 4. Demo with-pattern

- REST-Endpunkte kurz zeigen
- Zustandsklassen und Factory vorstellen
- Beispielablauf: `CREATED -> PAID -> PACKED -> SHIPPED`

## 5. Vergleich der beiden Branches

- weniger zentrale Fallunterscheidungen
- bessere Trennung der Verantwortlichkeiten
- einfachere Erweiterung um `RETURNED`

## 6. Rolle der generativen KI

- KI half bei Pattern-Verständnis, Testideen und Refactoring-Struktur
- Fachliche Entscheidungen wurden manuell überprüft und angepasst

## 7. Was wurde von der KI übernommen?

- erste Strukturideen
- Vorschläge für Tests und Zustandsmodelle

## 8. Was wurde selbst verbessert?

- konkrete API-Struktur
- Wahl der Zustandsübergänge
- Fehlerbehandlung
- Doku und Präsentationslogik

## 9. Fazit

- Das State Pattern ist sinnvoll bei klaren Lebenszyklen.
- Für sehr kleine Systeme wäre es eventuell zu aufwendig.