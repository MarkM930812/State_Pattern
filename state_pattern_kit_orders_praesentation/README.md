# LaTeX-Präsentation

Dieser Ordner enthält eine LaTeX-Beamer-Präsentation und einen ausformulierten Sprechtext zum Projekt `state_pattern_kit_orders`.

Dateien:

- `praesentation.tex`: Folien als Beamer-Präsentation
- `sprechtext.tex`: ausformulierter Sprechtext zur Präsentation
- `praesentation.pdf`: kompilierte Folien
- `sprechtext.pdf`: kompilierter Sprechtext

Lokaler Build:

```bash
latexmk -pdf praesentation.tex
latexmk -pdf sprechtext.tex
```