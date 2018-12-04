# Beschreibung Spiel Anwendungsfalldiagramm

 * Anwendungsfall: Spiel
 * Akteure: Spieler, KI, Spiellogik
 * Ziel: Spieler spielt das Spiel gegen die KI oder eine anderen Spieler
 * Vorbedingung: Spiel gestartet, Regeln bekannt
 * Nachbedingung: Spieler hat/haben Spiel zu Ende gespielt
 * Qualitätsanforderung: Siehe Unterlagen

## Ablauf
 1. Spieler wählt KI-Schwierigkeit oder PvP
 2. Spielfeld wird generiert
 3. Runde startet
    3a Beide Spieler bzw. KI führen Bewegungsphase durch  
    3b Beide Spieler bzw. KI führen Angriffphase durch  
    3c Spiellogik führt Kampfphase durch  
    3d Spiel ist nach der Runde nicht beendet, weiter mit 3  
 4. Spiel ist beendet
