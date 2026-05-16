# Analyse einer Lösung ohne Pattern

## Ausgangslage

Eine naive Lösung für dasselbe Problem würde alle Statuswechsel zentral in einer Service-Klasse behandeln. Typisch wären Methoden wie:

- `payOrder(orderId)`
- `packOrder(orderId)`
- `shipOrder(orderId)`
- `cancelOrder(orderId)`
- `returnOrder(orderId)`

## Typische Umsetzung ohne Pattern

```java
if (order.getStatus() == OrderStatus.CREATED) {
    order.setStatus(OrderStatus.PAID);
} else {
    throw new IllegalStateException("Cannot pay order");
}
```

Oder noch zentraler:

```java
switch (order.getStatus()) {
    case CREATED -> ...
    case PAID -> ...
    case PACKED -> ...
    case SHIPPED -> ...
    default -> ...
}
```

## Designprobleme

- Die Service-Klasse wird schnell zu groß.
- Die fachlichen Regeln sind verstreut oder in langen Methoden gebündelt.
- Neue Zustände erzwingen Änderungen an bestehender Logik.
- Die Open-Closed-Regel wird verletzt.
- Die Testfälle orientieren sich eher an Verzweigungen als an klaren Verantwortlichkeiten.

## Typischer Erweiterungsfall

Wenn nachträglich der Zustand `RETURNED` eingeführt wird, müssen meist mehrere Methoden angepasst werden:

- Versandlogik
- Rückgabeprüfung
- Statusausgabe
- Validierungslogik
- Fehlermeldungen

Gerade diese Mehrfachänderung macht eine Lösung ohne Pattern fehleranfälliger.