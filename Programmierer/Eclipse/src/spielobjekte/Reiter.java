package spielobjekte;

import java.awt.Point;

import prototypen.Spiel;
import prototypen.Spielbrett;
import spieler.Spieler;

public final class Reiter extends Figur {

    private static boolean[][] bewegungsRaster = { { false, false, false, true, false, false, false },
	    { false, false, false, true, false, false, false }, { false, false, false, true, false, false, false },
	    { true, true, true, false, true, true, true }, { false, false, false, true, false, false, false },
	    { false, false, false, true, false, false, false }, { false, false, false, true, false, false, false }, };

    private static boolean[][] angriffsRaster = { { true, true, true }, { true, false, true }, { true, true, true } };

    private static char symbol = 'R';
    private static String name = "Reiter";

    private static int lebenspunkte = 2;

    public Reiter(final Spieler team) {
	super(Reiter.name, Reiter.lebenspunkte, Reiter.bewegungsRaster, Reiter.angriffsRaster, Reiter.symbol, team);
    }

    // angepasste bewegungsabfrage für den reiter, da er hindernisse nicht
    // überspringen darf (wie bei figur, plus abfrage auf kein hindernis auf dem
    // weg)
    @Override
    public boolean bewegungMoeglich(final Spielbrett spielbrett, final Point ziel, final boolean figurenZaehlen,
	    final boolean setMessage) {

	if (figurenZaehlen) {
	    if (!this.istBewegt(setMessage)
		    && spielbrett.bewegungMoeglichSpielfeld(this.getPosition(), ziel, setMessage)
		    && spielbrett.bewegungMoeglichBelegt(ziel, setMessage)
		    && this.bewegungMoeglichRaster(ziel, setMessage) && this.keinHindernisAufWeg(ziel)) {
		return true;
	    } else {
		return false;
	    }
	} else {
	    if (!this.istBewegt(setMessage)
		    && spielbrett.bewegungMoeglichSpielfeld(this.getPosition(), ziel, setMessage)
		    && !(spielbrett.getFeld(ziel) instanceof Hindernis) && this.bewegungMoeglichRaster(ziel, setMessage)
		    && this.keinHindernisAufWeg(ziel)) {
		return true;
	    } else {
		return false;
	    }
	}
    }

    // überprüft alle felder zwischen der figur und dem zielfeld, ob dort ein
    // hindernis liegt, wenn ja ist die bewegung nicht möglich
    private boolean keinHindernisAufWeg(final Point ziel) {

	boolean keinHindernis = true;

	final Spielobjekt[][] brett = Spiel.getSpielbrett().copySpielobjekte();

	final int startX = this.getPosition().x;
	final int startY = this.getPosition().y;
	final int zielX = ziel.x;
	final int zielY = ziel.y;

	// Überprüfen Y-Achse
	if (startX == zielX) {
	    // nach unten
	    if ((startY - zielY) < 0) {
		for (int y = startY + 1; y < zielY; y++) {
		    if (brett[y][startX] instanceof Hindernis) {
			keinHindernis = false;
		    }
		}
		// nach oben
	    } else {
		for (int y = zielY + 1; y < startY; y++) {
		    if (brett[y][startX] instanceof Hindernis) {
			keinHindernis = false;
		    }
		}
	    }

	    // Überprüfen X-Achse
	} else if (startY == zielY) {
	    // nach rechts
	    if ((startX - zielX) < 0) {
		for (int x = startX + 1; x < zielX; x++) {
		    if (brett[startY][x] instanceof Hindernis) {
			keinHindernis = false;
		    }
		}
		// nach links
	    } else {
		for (int x = zielX + 1; x < startX; x++) {
		    if (brett[startY][x] instanceof Hindernis) {
			keinHindernis = false;
		    }
		}
	    }
	}

	return keinHindernis;
    }
}