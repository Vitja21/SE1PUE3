/**
  * Hauptklasse des Spiels. Hier wird das Spiel initialisiert, 
  * verwaltet und es geschehen alle Interaktionen des Spielers.
  */
package prototypen;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import spieler.EasyKI;
import spieler.HardKI;
import spieler.KI;
import spieler.MediumKI;
import spieler.Mensch;
import spieler.Spieler;
import spielobjekte.Figur;
import spielobjekte.Kampf;
import spielobjekte.Magier;
import spielobjekte.Spielobjekt;

public class Spiel {

    private static Spieler spieler1;
    private static Spieler spieler2 = null;

    private static ArrayList<Kampf> kaempfe = new ArrayList<>();
    private static String spielmodus;
    private static Spielbrett spielbrett;

    // erster index für phasenanzeigen, zweiter für fehlermeldungen, dritter für
    // meldungen wie 'Reiter wartet'
    private static String[] nachricht = { "", "" };
    private static boolean DEBUG = false;

    private static final Map<String, KI> schwierigkeitsgrade = new HashMap<>();

    public static void main(String[] args) {
	Scanner in = new Scanner(System.in);

	setupStart(args);

	initialisiereSpiel(in, args);

	spieleRunde(in);

	System.out.println("Gewonnen hat " + andTheWinnerIs());

	// Spiel hält an dieser Stelle, bis der Benutzer einen Input t�tigt
	in.nextLine();

	in.close();
    }

    /**
     * Initialisiert das Spiel. Fragt den Spieler nach verschiedenen
     * Auswahlmoeglichkeiten und setzt diese um.
     *
     * @param in the in
     */
    private static void initialisiereSpiel(Scanner in, String[] parameter) {

	System.out.println("Willkommen zu Arbeitstitel\n");
	System.out.println("Wähle den Spielmodus [PvP/PvE]");

	spielmodusAuswaehlen(in);

	spieler1 = new Mensch(1);

	if (spielmodus.equals("pve")) {
	    System.out.println("Wähle den Schwierigkeitsgrad [leicht/mittel/schwer]");
	    schwierigkeitsgradWaehlen(in);
	} else {
	    spieler2 = new Mensch(2);
	}

	spielbrett = Spielbrett.getInstance();
    }

    /**
     * Vorbereitungen, die vor der ersten Spielereingabe geschehen muessen.
     */
    private static void setupStart(String... parameter) {
	setParameter(parameter);
	schwierigkeitsgrade.put("leicht", new EasyKI(2));
	schwierigkeitsgrade.put("mittel", new MediumKI(2));
	schwierigkeitsgrade.put("schwer", new HardKI(2));
    }

    private static void setParameter(String[] parameter) {

	if (parameter != null) {
	    for (final String s : parameter) {
		if (s != null) {
		    switch (s.toUpperCase()) {
		    case "DEBUG":
			DEBUG = true;
			break;
		    }
		}
	    }
	}

    }

    /**
     * Spielmodus auswaehlen. Ueberprueft, ob der eingegebene Spielmodus korrekt ist
     * und gibt dementsprechend Rueckmeldung.
     *
     * @param Scanner in
     */
    private static void spielmodusAuswaehlen(Scanner in) {
	while (spielmodus == null) {
	    switch (in.nextLine().toLowerCase()) {
	    case "pvp":
		spielmodus = "pvp";
		break;
	    case "pve":
		spielmodus = "pve";
		break;
	    }
	    if (spielmodus == null)
		System.out.println("Kein korrekter Spielmodus, bitte 'PvP' oder 'PvE' eingeben.");
	}
    }

    /**
     * Schwierigkeitsgrad waehlen. Ueberprueft, ob der eingegebene
     * Schwierigkeitsgrad korrekt ist und gibt dementsprechend Rueckmeldung.
     *
     * @param Scanner in
     */
    private static void schwierigkeitsgradWaehlen(Scanner in) {
	while (spieler2 == null) {
	    spieler2 = schwierigkeitsgrade.get(in.nextLine().toLowerCase());
	    if (spieler2 == null)
		System.out
			.println("Kein korrekter Schwierigkeitsgrad, bitte 'leicht', 'mittel' oder 'schwer' eingeben.");
	}
    }

    /**
     * Das Spiel an sich. Ueberprueft, ob das Spiel beendet ist, falls nicht, leitet
     * es eine neue Runde ein.
     *
     * @param Scanner in
     */
    private static void spieleRunde(Scanner in) {
	while (!spielZuEnde()) {
	    kaempfe = new ArrayList<Kampf>();

	    Spielobjekt[][] brettAktuell = spielbrett.copySpielobjekte();

	    setupBewegungsphase();
	    bewegungsphase(in, spieler1, brettAktuell);
	    bewegungsphase(in, spieler2, brettAktuell);

	    updateConsole(spielbrett.copySpielobjekte());

	    angriffsphase(in, spieler1);
	    angriffsphase(in, spieler2);
	    kampfphase(); // nicht implementiert.
	}
    }

    private static void setupBewegungsphase() {
	setNachrichtTemporaerLang("Spieler 1 Bewegungsphase\n");
	setNachrichtTemporaerKurz("Erwarte Eingabe [warten/Figur Position - Ziel Position]");
	for (Figur f : spieler1.getHelden())
	    f.setIstBewegt(false);
	for (Figur f : spieler2.getHelden())
	    f.setIstBewegt(false);
	updateConsole(spielbrett.copySpielobjekte());
    }

    private static void bewegungsphase(Scanner in, Spieler spieler, Spielobjekt[][] brettAktuell) {

	while (spieler.hatNochNichtBewegteFiguren()) {

	    setNachrichtTemporaerLang("Spieler " + spieler.getNummer() + ": Bewegungsphase\n");
	    setNachrichtTemporaerKurz("Erwarte Eingabe [warten / Figur Position - Ziel Position]");

	    String eingabe = in.nextLine();

	    if (eingabe.contains("warte")) {
		warten(spieler, brettAktuell);
	    } else {
		bewegungBefehleInterpretieren(eingabe, spieler, brettAktuell);
	    }
	}
	bewegungsphaseSpielerAnzeige(spieler);
	updateConsole(brettAktuell);
    }

    private static void warten(Spieler spieler, Spielobjekt[][] brettAktuell) {
	for (Figur f : spieler.getHelden())
	    f.setIstBewegt(true);
	setNachrichtTemporaerKurz("Spieler " + spieler.getNummer() + " beendet seine Bewegung.");
    }

    private static void bewegungsphaseSpielerAnzeige(Spieler spieler) {
	int nummer;
	if (spieler.getNummer() == 1)
	    nummer = 2;
	else
	    nummer = 1;

	setNachrichtTemporaerLang("Spieler " + nummer + ": Bewegungsphase\n");
    }

    public static void bewegungBefehleInterpretieren(String eingabe, Spieler spieler, Spielobjekt[][] brettAktuell) {

	// Entfernt alle nicht alpha-numerischen Zeichen.
	eingabe = eingabe.toLowerCase().replaceAll("[\\W_]+", "");

	final int[] eingabeInt = { -1, -1, -1, -1 };
	int i = 0;

	for (final String s : eingabe.split("[^0-9]+")) {
	    if (!s.isEmpty() && i < 4) {
		eingabeInt[i] = Integer.parseInt(s) - 1;
		i += 2;
	    }
	}
	i = 1;
	for (final String s : eingabe.split("[^a-z]+")) {
	    if (!s.isEmpty() && i < 4) {
		eingabeInt[i] = s.toCharArray()[0] - 'a';
		i += 2;
	    }
	}

	Point start = new Point(eingabeInt[0], eingabeInt[1]);
	Point ziel = new Point(eingabeInt[2], eingabeInt[3]);

	if (start.x >= 0 && start.y >= 0 && ziel.x >= 0 && ziel.y >= 0) {

	    if (spielbrett.getFeld(start).bewegungMoeglich(ziel)) {

		if (!((Figur) spielbrett.getFeld(start)).istBewegt()) {

		    if (((Figur) spielbrett.getFeld(start)).getTeam().equals(spieler)) {
			((Figur) spielbrett.getFeld(start)).bewegen(ziel);
		    } else {
			setNachrichtTemporaerKurz("Zug nicht möglich, da ausgewähltes Figur nicht Spieler "
				+ spieler.getNummer() + " gehört.");
		    }
		} else {
		    setNachrichtTemporaerKurz(
			    "Zug nicht möglich, da ausgewähltes Figur diese Runde bereits bewegt wurde.");
		}
	    }
	    updateConsole(brettAktuell);
	} else {
	    // TODO: Bewegungsradius muss angepasst werden, da bei brettAktuell die
	    // Bewegungsangaben nicht enthalten sind
	    spielbrett.printBewegen(start, brettAktuell);
	}
    }

    private static void angriffsphase(Scanner in, Spieler spieler) {

	setNachrichtTemporaerLang(String.format("Spieler %d: Angriffssphase%n", spieler.getNummer()));
	setNachrichtTemporaerKurz("Erwartete Eingabe: [Startkoordinaten, Zielkoordinaten, Schere/Stein/Papier]: ");
	updateConsole(Spiel.getSpielbrett().copySpielobjekte());

	while (hatOffeneAngriffsMoeglichkeiten(spieler)) {
	    String eingabe = in.nextLine();
	    angriffsBefehleInterpretieren(eingabe, spieler);
	    updateConsole(Spiel.getSpielbrett().copySpielobjekte());
	}

    }

    private static String printKaempfe() {
	StringBuilder output = new StringBuilder();
	for (Kampf k : kaempfe) {
	    output.append(String.format("%s%n", k.printKampf()));
	}
	return output.toString();
    }

    private static boolean hatOffeneAngriffsMoeglichkeiten(Spieler spieler) {

	for (Figur f : spieler.getHelden()) {
	    if (!figurGreiftAn(f) || Magier.class.isInstance(f)) {
		for (int yZiel = 0; yZiel < spielbrett.getYLaenge(); yZiel++) {
		    for (int xZiel = 0; xZiel < spielbrett.getXLaenge(); xZiel++) {
			if (spielbrett.getFeld(new Point(xZiel, yZiel)).istAngreifbar(spieler)
				&& f.angriffMoeglich(new Point(xZiel, yZiel))) {
			    if (Magier.class.isInstance(f)) {
				if (kaempfe.size() > 0) {
				    for (Kampf k : kaempfe) {
					if (f.equals(k.getAngreifer())) {
					    System.out.printf("war Angreifer, ");
					    if (!(spielbrett.getFeld(new Point(xZiel, yZiel))
						    .equals(k.getVerteidiger()))) {
						return true;
					    }
					}
				    }
				} else {
				    return true;
				}

			    } else {
				return true;
			    }
			}
		    }
		}
	    }
	}

	return false;
    }

    private static void kampfphase() {
	// Kämpfe austragen, der Printline hinzufügen, Tote entfernen,
	// Spielbrett anzeigen.
	setNachrichtTemporaerLang(String.format("Ausgetragene Kämpfe:%n"));
	setNachrichtTemporaerKurz(String.format("%s", printKaempfe()));

	updateConsole(spielbrett.copySpielobjekte());
    }

    public static void angriffsBefehleInterpretieren(String eingabe, Spieler spieler) {

	// Entfernt alle nicht alpha-numerischen Zeichen.
	eingabe = eingabe.toLowerCase().replaceAll("[\\W_]+", "");

	final int[] eingabeInt = { -1, -1, -1, -1 };
	int i = 0;

	for (final String s : eingabe.split("[^0-9]+")) {
	    if (!s.isEmpty() && i < 4) {
		eingabeInt[i] = Integer.parseInt(s) - 1;
		i += 2;
	    }
	}
	i = 1;
	for (final String s : eingabe.split("[^a-z]+")) {
	    if (!s.isEmpty() && i < 4) {
		eingabeInt[i] = s.toCharArray()[0] - 'a';
		i += 2;
	    }
	}

	Point start = new Point(eingabeInt[0], eingabeInt[1]);
	Point ziel = new Point(eingabeInt[2], eingabeInt[3]);

	// Start und Ziel wurden eingegeben
	if (start.x >= 0 && start.y >= 0 && ziel.x >= 0 && ziel.y >= 0) {
	    // Start ist eine Figur, dann Angriffsart auslesen.
	    if (Figur.class.isInstance(spielbrett.getFeld(start))) {
		// Start Figur gehört Spieler
		if (((Figur) (spielbrett.getFeld(start))).getTeam().equals(spieler)) {

		    // Start Figur ist noch kein Angreifer. (Oder Magier mit
		    // offenen Angriffsmöglichkeiten.
		    if (!figurGreiftAn((Figur) (spielbrett.getFeld(start)))) {
			// Ziel ist eine Figur
			if (Figur.class.isInstance(spielbrett.getFeld(ziel))) {
			    // Ziel Figur gehört nicht Spieler
			    if (!(((Figur) (spielbrett.getFeld(ziel))).getTeam().equals(spieler))) {
				// Ziel Figur ist in Reichweite der Start Figur
				if (spielbrett.getFeld(start).angriffMoeglich(ziel)) {

				    String ssp = eingabe.replaceAll("([a-z]+[0-9]+){2}", "");
				    if (ssp.contains("schere") || ssp.contains("stein") || ssp.contains("papier")) {
					// Füge neuen Kampf zu Kämpfe hinzu
					kaempfe.add(new Kampf((Figur) (spielbrett.getFeld(start)),
						(Figur) (spielbrett.getFeld(ziel)), ssp));
				    } else {
					setNachrichtTemporaerKurz(
						"Angriff nicht möglich: Angriffsart wurde nicht korrekt eingegeben.");
				    }

				} else {
				    setNachrichtTemporaerKurz(
					    "Angriff nicht möglich: Zielfigur ist außerhalb der Angriffsreichweite.");
				}
			    } else {
				setNachrichtTemporaerKurz(String.format(
					"Angriff nicht möglich: Figur gehört Spieler %d.", spieler.getNummer()));
			    }

			} else {
			    setNachrichtTemporaerKurz("Angriff nicht möglich: Zielkoordinate hat keine Figur.");
			}
		    } else {
			setNachrichtTemporaerKurz(
				"Angriff nicht möglich: Figur greift bereits an und ist kein Magier.");
		    }
		} else {
		    setNachrichtTemporaerKurz(String.format("Angriff nicht möglich: Figur gehört nicht Spieler %d.",
			    spieler.getNummer()));
		}
	    } else {
		setNachrichtTemporaerKurz("Angriff nicht möglich: Startkoordinate hat keine Figur.");
	    }
	} else {
	    setNachrichtTemporaerKurz("Angriff nicht möglich: Koordinaten waren nicht korrekt.");
	}
    }

    private static boolean figurGreiftAn(Figur f) {

	for (Kampf k : kaempfe) {
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
	return (spieler1.istBesiegt() || spieler2.istBesiegt());
    }

    /**
     * Wertet den Gewinner aus und gibt diesen als String zurueck.
     *
     * @return gewinner
     */
    private static String andTheWinnerIs() {
	String gewinner;
	if (spieler1.istBesiegt() && !spieler2.istBesiegt()) {
	    gewinner = spieler2.toString() + "!";
	} else if (!spieler1.istBesiegt() && spieler2.istBesiegt()) {
	    gewinner = spieler1.toString() + "!";
	} else if (spieler1.istBesiegt() && spieler2.istBesiegt()) {
	    gewinner = "niemand! Alle tot! So ist das im Krieg, es gibt nur Verlierer.";
	} else {
	    gewinner = "noch keiner (sollte nicht eintreten)";
	}
	return gewinner;
    }

    public static void updateConsole(Spielobjekt[][] brett) {
	if (DEBUG) {
	    for (int i = 0; i < 20; i++) {
		System.out.println();
	    }
	} else {
	    clearConsole();
	}

	spielbrett.printBoard(brett);

	for (String s : nachricht)
	    printNachricht(s);
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
	if (nachricht != null && !nachricht.equals("")) {
	    System.out.println(nachricht);
	}
    }

    public static Spieler getSpieler1() {
	return spieler1;
    }

    public static void setSpieler1(Spieler spieler1) {
	Spiel.spieler1 = spieler1;
    }

    public static Spieler getSpieler2() {
	return spieler2;
    }

    public static void setSpieler2(Spieler spieler2) {
	Spiel.spieler2 = spieler2;
    }

    public static Spielbrett getSpielbrett() {
	return spielbrett;
    }

    public static void setSpielbrett(Spielbrett spielbrett) {
	Spiel.spielbrett = spielbrett;
    }

    public static String getNachrichtTemporaerLang() {
	return nachricht[0];
    }

    public static String getNachrichtTemporaerKurz() {
	return nachricht[1];
    }

    public static void setNachrichtTemporaerLang(String nachricht) {
	Spiel.nachricht[0] = nachricht;
    }

    public static void setNachrichtTemporaerKurz(String nachricht) {
	Spiel.nachricht[1] = nachricht;
    }

}
