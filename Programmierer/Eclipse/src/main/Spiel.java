/**
  * Hauptklasse des Spiels. Hier wird das Spiel initialisiert,
  * verwaltet und es geschehen alle Interaktionen des Spielers.
  */
package main;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import akteure.EasyKI;
import akteure.HardKI;
import akteure.KI;
import akteure.MediumKI;
import akteure.Mensch;
import akteure.Spieler;
import spielobjekte.Figur;
import spielobjekte.Kampf;
import spielobjekte.Spielbrett;

public class Spiel {

    private static Spieler spieler1;
    private static Spieler spieler2 = null;

    private static ArrayList<Kampf> kaempfe = new ArrayList<>();
    private static String spielmodus;
    private static Spielbrett spielbrett;

    // erster index für phasenanzeigen, zweiter für fehlermeldungen, dritter für
    // meldungen wie 'Reiter wartet'
    private static String[] nachricht = { "", "" };

    private static final Map<String, KI> schwierigkeitsgrade = new HashMap<>();

    public static void main(final String[] args) {
        final Scanner in = new Scanner(System.in);

        Spiel.setupStart(args);

        Spiel.initialisiereSpiel(in, args);

        Spiel.spieleRunde(in);

        System.out.println("Gewonnen hat " + Spiel.andTheWinnerIs());

        // Spiel hält an dieser Stelle, bis der Benutzer einen Input t�tigt
        in.nextLine();

        in.close();
    }

    /**
     * Initialisiert das Spiel. Fragt den Spieler nach verschiedenen
     * Auswahlmoeglichkeiten und setzt diese um.
     *
     * @param in
     *            the in
     */
    private static void initialisiereSpiel(final Scanner in, final String[] parameter) {

        System.out.println("Willkommen zu Arbeitstitel\n");
        System.out.println("Wähle den Spielmodus [PvP/PvE]");

        Spiel.spielmodusAuswaehlen(in);

        Spiel.spieler1 = new Mensch(1);

        if (Spiel.spielmodus.equals("pve")) {
            System.out.println("Wähle den Schwierigkeitsgrad [leicht/mittel/schwer]");
            Spiel.schwierigkeitsgradWaehlen(in);
        } else {
            Spiel.spieler2 = new Mensch(2);
        }

        Spiel.setSpielbrett(new Spielbrett());
    }

    /**
     * Vorbereitungen, die vor der ersten Spielereingabe geschehen muessen.
     */
    private static void setupStart(final String... parameter) {
        Spiel.setParameter(parameter);
        Spiel.schwierigkeitsgrade.put("leicht", new EasyKI(2));
        Spiel.schwierigkeitsgrade.put("mittel", new MediumKI(2));
        Spiel.schwierigkeitsgrade.put("schwer", new HardKI(2));
    }

    private static void setParameter(final String[] parameter) {

        if (parameter != null) {
            for (final String s : parameter) {
                if (s != null) {
                    switch (s.toUpperCase()) {
                    // Zukünftige Parameter hier hinzufügen (z.B. Cheat mode on)
                    }
                }
            }
        }

    }

    /**
     * Spielmodus auswaehlen. Ueberprueft, ob der eingegebene Spielmodus korrekt ist
     * und gibt dementsprechend Rueckmeldung.
     *
     * @param Scanner
     *            in
     */
    private static void spielmodusAuswaehlen(final Scanner in) {
        while (Spiel.spielmodus == null) {
            switch (in.nextLine().toLowerCase().replaceAll(".*?(pvp|pve).*", "$1")) {
            case "pvp":
                Spiel.spielmodus = "pvp";
                break;
            case "pve":
                Spiel.spielmodus = "pve";
                break;
            default:
                System.out.println("Kein korrekter Spielmodus, bitte 'PvP' oder 'PvE' eingeben.");
            }
        }
    }

    /**
     * Schwierigkeitsgrad waehlen. Ueberprueft, ob der eingegebene
     * Schwierigkeitsgrad korrekt ist und gibt dementsprechend Rueckmeldung.
     *
     * @param Scanner
     *            in
     */
    private static void schwierigkeitsgradWaehlen(final Scanner in) {
        while (Spiel.spieler2 == null) {
            Spiel.spieler2 = Spiel.schwierigkeitsgrade
                    .get(in.nextLine().toLowerCase().replaceAll(".*?(leicht|mittel|schwer).*", "$1"));
            if (Spiel.spieler2 == null) {
                System.out
                        .println("Kein korrekter Schwierigkeitsgrad, bitte 'leicht', 'mittel' oder 'schwer' eingeben.");
            }
        }
    }

    /**
     * Das Spiel an sich. Ueberprueft, ob das Spiel beendet ist, falls nicht, leitet
     * es eine neue Runde ein.
     *
     * @param Scanner
     *            in
     */
    private static void spieleRunde(final Scanner in) {
        while (!Spiel.spielZuEnde()) {
            Spiel.kaempfe = new ArrayList<>();

            final Spielbrett brettAlt = new Spielbrett(Spiel.getSpielbrett().copySpielobjekte());

            Spiel.bewegungsphase(in, Spiel.spieler1, brettAlt);
            Spiel.bewegungsphase(in, Spiel.spieler2, brettAlt);

            Spiel.updateConsole(Spiel.getSpielbrett());

            Spiel.angriffsphase(in, Spiel.spieler1);
            Spiel.angriffsphase(in, Spiel.spieler2);

            Spiel.kampfphase(in);

        }
    }

    private static void bewegungsphase(final Scanner in, final Spieler spieler, final Spielbrett brettAlt) {

        brettAlt.addMovementMarks(spieler);
        Spiel.setNachrichtTemporaerLang(
                String.format("Spieler %d: Bewegungsphase - Erwarte Eingabe [warten/Figur Position - Ziel Position]%n",
                        spieler.getNummer()));

        while (spieler.hatNochNichtBewegteFiguren(brettAlt)) {

            Spiel.updateConsole(brettAlt);
            brettAlt.removeSymbolMarks(Spielbrett.AKTIV | Spielbrett.ANGREIFBAR | Spielbrett.BEWEGUNG_FIGUR);
            // MENSCH ODER KI
            String eingabe = null;
            if (spieler instanceof KI) {
                // Methode zum Aufrufen der Bewegung von KI
                eingabe = ((KI) spieler).bewegungsphase(brettAlt);
            } else {
                eingabe = in.nextLine();
            }

            if (eingabe.contains("warte")) {
                spieler.warten();
            } else {
                Spiel.bewegen(brettAlt, Spiel.bewegungBefehleInterpretieren(eingabe), spieler);
            }

        }

        spieler.resetHeldenIstBewegt();

        // Entfernt Bewegungsmarkierungen vom Spielbrett (Vorbereitung für
        // den nächsten Spieler, der andere Bewegungsmarkierungen braucht)
        brettAlt.removeSymbolMarks(Spielbrett.BEWEGUNG);
    }

    public static int[] bewegungBefehleInterpretieren(String eingabe) {

        // Entfernt alle nicht alpha-numerischen Zeichen.
        eingabe = eingabe.toLowerCase().replaceAll("[\\W_]+", "");

        final int[] eingabeKoordinaten = { -1, -1, -1, -1 };
        int i = 0;

        for (final String s : eingabe.split("[^0-9]+")) {
            if (!s.isEmpty() && (i < 4)) {
                eingabeKoordinaten[i] = Integer.parseInt(s) - 1;
                i += 2;
            }
        }
        i = 1;
        for (final String s : eingabe.split("[^a-z]+")) {
            if (!s.isEmpty() && (i < 4)) {
                eingabeKoordinaten[i] = s.toCharArray()[0] - 'a';
                i += 2;
            }
        }

        return eingabeKoordinaten;
    }

    private static void bewegen(final Spielbrett brettAlt, final int[] eingabeKoordinaten, final Spieler spieler) {
        final Point start = new Point(eingabeKoordinaten[0], eingabeKoordinaten[1]);
        final Point ziel = new Point(eingabeKoordinaten[2], eingabeKoordinaten[3]);

        // Start und Ziel Punkt sind auf dem Spielbrett
        if (Spiel.getSpielbrett().isInBounds(start) && Spiel.getSpielbrett().isInBounds(ziel)) {
            // Start ist eine Figur
            if ((Spiel.getSpielbrett().getFeld(start) instanceof Figur)) {
                // Figur kann sich nach Ziel bewegen
                if (Spiel.getSpielbrett().getFeld(start).bewegungMoeglich(Spiel.getSpielbrett(), ziel, true, true)) {
                    // Figur gehört Spieler
                    if (((Figur) Spiel.getSpielbrett().getFeld(start)).getTeam().equals(spieler)) {
                        // Bewege Figur
                        ((Figur) Spiel.getSpielbrett().getFeld(start)).bewegen(Spiel.getSpielbrett(), ziel);
                        Spiel.setNachrichtTemporaerKurz("");
                    } else {
                        Spiel.setNachrichtTemporaerKurz("Zug nicht möglich, da ausgewähltes Figur nicht Spieler "
                                + spieler.getNummer() + " gehört.");
                    }
                }
            } else {
                Spiel.setNachrichtTemporaerKurz(
                        "Bewegung nicht möglich, da ausgewähltes Objekt keine bewegbare Spielfigur ist.");
            }
        } else if (Spiel.getSpielbrett().isInBounds(start)) {
            if ((Spiel.getSpielbrett().getFeld(start) instanceof Figur)) {
                brettAlt.addMovementMarksFigur((Figur) (Spiel.getSpielbrett().getFeld(start)));
            }
        } else {
            Spiel.setNachrichtTemporaerKurz("Zug nicht möglich, die eingegebenen Koordinaten waren nicht korrekt.");
        }
    }

    private static void angriffsphase(final Scanner in, final Spieler spieler) {

        Spiel.setNachrichtTemporaerLang(String.format(
                "Spieler %d: Angriffssphase - Erwartete Eingabe: [Startkoordinaten, Zielkoordinaten, Schere/Stein/Papier]:%n",
                spieler.getNummer()));

        while (Spiel.hatOffeneAngriffsMoeglichkeiten(spieler)) {

            Spiel.updateConsole(Spiel.getSpielbrett());
            String eingabe;
            if (spieler instanceof KI) {
                eingabe = ((KI) spieler).random();
            } else {
                eingabe = in.nextLine();
            }
            Spiel.angreifen(Spiel.angriffsBefehleInterpretieren(eingabe), eingabe, spieler);
            Spiel.getSpielbrett().removeSymbolMarks(Spielbrett.AKTIV | Spielbrett.ANGREIFBAR);
        }

    }

    private static boolean hatOffeneAngriffsMoeglichkeiten(final Spieler spieler) {

        boolean hatNoch = false;

        for (final Figur f : spieler.getHelden()) {
            for (int yZiel = 0; yZiel < Spiel.getSpielbrett().getYLaenge(); yZiel++) {
                for (int xZiel = 0; xZiel < Spiel.getSpielbrett().getXLaenge(); xZiel++) {
                    if (f.angriffMoeglich(new Point(xZiel, yZiel))) {
                        ((Figur) (Spiel.getSpielbrett().getFeld(new Point(xZiel, yZiel)))).symbolAddMarkCanBeAttacked();
                        f.symbolAddMarkActive();
                        hatNoch = true;
                    }
                }
            }
        }

        return hatNoch;
    }

    private static void kampfphase(final Scanner in) {
        if (Spiel.kaempfe.size() > 0) {
            Spiel.setNachrichtTemporaerKurz(String.format("Ausgetragene Kämpfe:%n"));

            Kampf.kaempfen(Spiel.kaempfe);
            Spiel.kaempfe.clear();

            Spiel.getSpielbrett().toteEntfernen();
        }
    }

    public static int[] angriffsBefehleInterpretieren(String eingabe) {

        // Entfernt alle nicht alpha-numerischen Zeichen.
        eingabe = eingabe.toLowerCase().replaceAll("[\\W_]+", "");

        final int[] eingabeKoordinaten = { -1, -1, -1, -1 };
        int i = 0;

        for (final String s : eingabe.split("[^0-9]+")) {
            if (!s.isEmpty() && (i < 4)) {
                eingabeKoordinaten[i] = Integer.parseInt(s) - 1;
                i += 2;
            }
        }
        i = 1;
        for (final String s : eingabe.toLowerCase().split("[^a-z]+")) {
            if (!s.isEmpty() && (i < 4)) {
                eingabeKoordinaten[i] = s.toCharArray()[0] - 'a';
                i += 2;
            }
        }

        return eingabeKoordinaten;

    }

    private static void angreifen(final int[] eingabeKoordinaten, final String eingabe, final Spieler spieler) {
        final Point start = new Point(eingabeKoordinaten[0], eingabeKoordinaten[1]);
        final Point ziel = new Point(eingabeKoordinaten[2], eingabeKoordinaten[3]);

        // Start und Ziel wurden eingegeben und sind auf dem Spielbrett
        if (Spiel.getSpielbrett().isInBounds(start) && Spiel.getSpielbrett().isInBounds(ziel)) {
            // Start ist eine Figur
            if (Figur.class.isInstance(Spiel.getSpielbrett().getFeld(start))) {
                // Start Figur gehört Spieler
                if (((Figur) (Spiel.getSpielbrett().getFeld(start))).getTeam().equals(spieler)) {
                    // Ziel ist eine Figur
                    if (Figur.class.isInstance(Spiel.getSpielbrett().getFeld(ziel))) {
                        // Figur kann Ziel angreifen
                        if (((Figur) (Spiel.getSpielbrett().getFeld(start))).angriffMoeglich(ziel)) {
                            // Sucht nach schere/stein/papier in der Eingabe und
                            // setzt ssp auf den Wert.
                            final String ssp = eingabe.toLowerCase().replaceAll(".*?(schere|stein|papier).*", "$1");
                            if (ssp.contains("schere") || ssp.contains("stein") || ssp.contains("papier")) {
                                // Füge neuen Kampf zu Kämpfe hinzu
                                Spiel.kaempfe.add(new Kampf((Figur) (Spiel.getSpielbrett().getFeld(start)),
                                        (Figur) (Spiel.getSpielbrett().getFeld(ziel)), ssp));
                                Spiel.setNachrichtTemporaerKurz("");
                            } else {
                                Spiel.setNachrichtTemporaerKurz(
                                        "Angriff nicht möglich: Angriffsart wurde nicht korrekt eingegeben.");
                            }

                        } else {
                            Spiel.setNachrichtTemporaerKurz(
                                    "Angriff nicht möglich: Startfigur kämpft bereits oder Zielfigur ist außer Reichweite.");
                        }
                    } else {
                        Spiel.setNachrichtTemporaerKurz("Angriff nicht möglich: Zielkoordinate hat keine Figur.");
                    }
                } else {
                    Spiel.setNachrichtTemporaerKurz(String
                            .format("Angriff nicht möglich: Figur gehört nicht Spieler %d.", spieler.getNummer()));
                }
            } else {
                Spiel.setNachrichtTemporaerKurz("Angriff nicht möglich: Startkoordinate hat keine Figur.");
            }
        } else {
            Spiel.setNachrichtTemporaerKurz("Angriff nicht möglich: Koordinaten waren nicht korrekt.");
        }
    }

    public static boolean figurGreiftAn(final Figur f) {

        for (final Kampf k : Spiel.kaempfe) {
            if (k.getAngreifer() == f) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ueberprueft, ob das Spiel beendet ist.
     *
     * @return true, falls einer der Spieler keine Helden mehr hat.
     */
    private static boolean spielZuEnde() {
        return (Spiel.spieler1.istBesiegt() || Spiel.spieler2.istBesiegt());
    }

    /**
     * Wertet den Gewinner aus und gibt diesen als String zurueck.
     *
     * @return gewinner
     */
    private static String andTheWinnerIs() {
        String gewinner;
        if (Spiel.spieler1.istBesiegt() && !Spiel.spieler2.istBesiegt()) {
            gewinner = Spiel.spieler2.toString() + "!";
        } else if (!Spiel.spieler1.istBesiegt() && Spiel.spieler2.istBesiegt()) {
            gewinner = Spiel.spieler1.toString() + "!";
        } else if (Spiel.spieler1.istBesiegt() && Spiel.spieler2.istBesiegt()) {
            gewinner = "niemand! Alle tot! So ist das im Krieg, es gibt nur Verlierer.";
        } else {
            gewinner = "noch keiner (sollte nicht eintreten)";
        }
        return gewinner;
    }

    /**
     * Zeichnet das Spielbrett und die Nachrichten neu.
     *
     * @param brett
     *            ein Spielobjekt[][], das das zu zeichnende Spielbrett darstellt
     */
    public static void updateConsole(final Spielbrett brett) {

        if (System.getProperty("java.class.path").toLowerCase().contains("eclipse")) {
            for (int i = 0; i < 20; i++) {
                System.out.println();
            }
        } else {
            Spiel.clearConsole();
        }

        brett.printBoard();

        for (final String s : Spiel.nachricht) {
            Spiel.printNachricht(s);
        }
    }

    private static void clearConsole() {

        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Fehler beim Neuzeichnen des Spielfeldes: " + e);
        }

    }

    private static void printNachricht(final String nachricht) {
        if ((nachricht != null) && !nachricht.equals("")) {
            System.out.println(nachricht);
        }
    }

    public static Spieler getSpieler1() {
        return Spiel.spieler1;
    }

    public static void setSpieler1(final Spieler spieler1) {
        Spiel.spieler1 = spieler1;
    }

    public static Spieler getSpieler2() {
        return Spiel.spieler2;
    }

    public static void setSpieler2(final Spieler spieler2) {
        Spiel.spieler2 = spieler2;
    }

    public static Spielbrett getSpielbrett() {
        return Spiel.spielbrett;
    }

    public static void setSpielbrett(final Spielbrett spielbrett) {
        Spiel.spielbrett = spielbrett;
    }

    public static String getNachrichtTemporaerLang() {
        return Spiel.nachricht[0];
    }

    public static String getNachrichtTemporaerKurz() {
        return Spiel.nachricht[1];
    }

    public static void setNachrichtTemporaerLang(final String nachricht) {
        Spiel.nachricht[0] = nachricht;
    }

    public static void setNachrichtTemporaerKurz(final String nachricht) {
        Spiel.nachricht[1] = nachricht;
    }

    public static ArrayList<Kampf> getKaempfe() {
        return Spiel.kaempfe;
    }

}