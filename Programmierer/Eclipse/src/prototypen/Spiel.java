/**
  * Hauptklasse des Spiels. Hier wird das Spiel initialisiert, 
  * verwaltet und es geschehen alle Interaktionen des Spielers.
  */
package prototypen;

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
import spielphasen.Kampf;

// TODO: Auto-generated Javadoc
/**
 * The Class Spiel.
 */
public class Spiel {

    /** The spieler 1. */
    private static Spieler spieler1;

    /** The spieler 2. */
    private static Spieler spieler2 = null;

    /** The spielmodus. */
    private static String spielmodus;

    /** The spielbrett. */
    private static Spielbrett spielbrett;

    /** The Constant schwierigkeitsgrade. */
    private static final Map<String, KI> schwierigkeitsgrade = new HashMap<>();

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
	Scanner in = new Scanner(System.in);

	initialisiereSpiel(in);

	spieleRunde(in);

	System.out.println("Gewonnen hat " + andTheWinnerIs());

	// Spiel hält an dieser Stelle, bis der Benutzer einen Input tätigt
	in.nextLine();

	in.close();
    }

    /**
     * Initialisiert das Spiel. Fragt den Spieler nach verschiedenen
     * Auswahlmoeglichkeiten und setzt diese um.
     *
     * @param in the in
     */
    private static void initialisiereSpiel(Scanner in) {

	setup();

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

	spielbrett = Spielbrett.getInstance(spieler1.getHelden(), spieler2.getHelden());
    }

    /**
     * Vorbereitungen, die vor der ersten Spielereingabe geschehen muessen.
     */
    private static void setup() {
	schwierigkeitsgrade.put("leicht", new EasyKI(2));
	schwierigkeitsgrade.put("mittel", new MediumKI(2));
	schwierigkeitsgrade.put("schwer", new HardKI(2));
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
	    ArrayList<Kampf> kaempfe;
	    bewegungsphase(in);
	    kaempfe = angriffsphase(in);
	    kampfphase(kaempfe);
	}
    }

    // macht am besten florian, im großen und ganzen ist es ja eine umstrukturierung
    // der bewegung aus spielbrett nach hier. spielbrett sollte dadurch automatisch
    // übersichtlicher werden.
    private static void bewegungsphase(Scanner in) {

    }

    // macht am besten der, der sich um kampf generell kümmert
    private static ArrayList<Kampf> angriffsphase(Scanner in) {
	return null;
    }

    // siehe angriffsphase
    private static void kampfphase(ArrayList<Kampf> kampf) {

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
	    gewinner = "niemand! So ist das im Krieg, es gibt nur Verlierer.";
	} else {
	    gewinner = "noch keiner (sollte nicht eintreten)";
	}
	return gewinner;
    }

}
