package spieler;

import java.util.ArrayList;
import java.util.List;

import spielobjekte.Bogenschuetze;
import spielobjekte.Figur;
import spielobjekte.Hindernis;
import spielobjekte.Lanzentraeger;
import spielobjekte.Magier;
import spielobjekte.Reiter;
import spielobjekte.Schwertkaempfer;
import spielobjekte.Spielobjekt;

public abstract class KI extends Spieler {

    private Team1 t = new Team1();

    private Spielobjekt[][] spielfeld;

    private Spieler anderer = null;

    public KI(int nummer) {
	super(nummer);
//	spielfeld = spielfeldInterpreter();
    }

//    private ArrayList<Spielobjekt> berechneFeindBewegung() {
//	ArrayList<Spielobjekt> bewegung = new ArrayList<>();
//	return bewegung;
//    }
//
//    private ArrayList<Spielobjekt> berechneFeindAngriff() {
//	ArrayList<Spielobjekt> angriff = new ArrayList<>();
//	return angriff;
//    }
//
//    private void waehleBewegung() {
//
//    }
//
//    private void waehleAngriff() {
//
//    }

    @Override
    public void bewegungsphase() {

	List<String> bewegungen = new ArrayList<>();

	for (Figur f : this.getHelden()) {
	    bewegungen.add(waehleBewegung(f));
	}

	while (t.canPlayerMove("Team1")) {

	}
    }

    private Spielobjekt[][] spielfeldInterpreter() {
	char[][] feld = t.getSpielfeld();
	Spielobjekt[][] spielfeld = new Spielobjekt[10][10];

	for (int i = 0; i < 10; i++) {
	    for (int j = 0; j < 10; j++) {
		Spielobjekt objekt;
		switch (feld[i][j]) {
		case 'B':
		    objekt = new Bogenschuetze(welcherSpieler(i));
		    break;
		case 'L':
		    objekt = new Lanzentraeger(welcherSpieler(i));
		    break;
		case 'M':
		    objekt = new Magier(welcherSpieler(i));
		    break;
		case 'R':
		    objekt = new Reiter(welcherSpieler(i));
		    break;
		case 'S':
		    objekt = new Schwertkaempfer(welcherSpieler(i));
		    break;
		case '#':
		    objekt = new Hindernis();
		    break;
		default:
		    objekt = new Spielobjekt(' ');
		    break;
		}

		spielfeld[i][j] = objekt;
	    }
	}

	return spielfeld;

    }

    private Spieler welcherSpieler(int spalte) {
	if (nummer == 1) {
	    return spalte == 0 ? this : anderer;
	} else {
	    return spalte == 0 ? anderer : this;
	}
    }

    private String waehleBewegung(final Figur held) {
	Figur zuBewegenderHeld = held;
	List<String> moeglicheZiele = new ArrayList<>();
	List<String> moeglicheAngegriffeneFelder = new ArrayList<>();
	List<Figur> gegner = t.gegner();

	int scores;

	String potentiellesOpferPosition;

	for (Figur potentiellesOpfer : gegner) {

	    // Iterieren über das Spielfeld und mögliche Felder speichern, auf die sich ein
	    // Gegner bewegen könnte
	    for (int i = 65; i <= 75; i++) {
		for (int j = 1; j <= 10; j++) {

		    // TODO position von potentiellesOpfer
		    potentiellesOpferPosition = "position";

		    String zielPosition = i + "" + j;
		    String gegnerClass = t.getGegnerClass(potentiellesOpferPosition);

		    if (t.isValidMove(potentiellesOpferPosition, zielPosition, gegnerClass)) {

			moeglicheZiele.add(zielPosition);

			// Erneutes Iterieren über das Spielfeld und Speichern der möglichen
			// Angriffsziele von der aktuellen Position aus
			for (int k = 65; k <= 75; k++) {

			    for (int l = 1; l <= 10; l++) {

				String moeglichesAngriffsziel = k + "" + l;

				if (t.isValidAttack(zielPosition, moeglichesAngriffsziel, gegnerClass)) {
				    moeglicheAngegriffeneFelder.add(moeglichesAngriffsziel);
				}
			    }
			}

		    }
		}
	    }
	}

	return null;
    }

}
