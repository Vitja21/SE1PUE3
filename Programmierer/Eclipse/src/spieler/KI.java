package spieler;

import java.util.ArrayList;
import java.util.List;

import spielobjekte.Figur;

public abstract class KI extends Spieler {

    Team1 t = new Team1();

    public KI(int nummer) {
	super(nummer);
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

    public void bewegungsphase() {

	List<String> bewegungen = new ArrayList<>();

	for (Figur f : this.getHelden()) {
	    bewegungen.add(waehleBewegung(f));
	}

	while (t.canPlayerMove("Team1")) {

	}
    }

    private String waehleBewegung(final Figur held) {
	Figur zuBewegenderHeld = held;
	List<String> moeglicheZiele = new ArrayList<>();
	List<String> moeglicheAngegriffeneFelder = new ArrayList<>();
	List<Figur> gegner = t.gegner();

	String potentiellesOpferPosition;

	for (Figur potentiellesOpfer : gegner) {

	    for (int i = 65; i <= 75; i++) {
		for (int k = 1; k <= 10; k++) {

		    // TODO position von potentiellesOpfer
		    potentiellesOpferPosition = "position";

		    String zielPosition = i + "" + k;
		    String gegnerClass = t.getGegnerClass(potentiellesOpferPosition);

		    if (t.isValidMove(potentiellesOpferPosition, zielPosition, gegnerClass)) {

			moeglicheZiele.add(zielPosition);

			for (int l = 65; i <= 75; i++) {

			    for (int m = 1; k <= 10; k++) {

				String moeglichesAngriffsziel = l + "" + m;

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
