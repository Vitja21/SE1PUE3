package spielobjekte;

import java.awt.Point;

import prototypen.Spiel;
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

    @Override
    /**
     * Bewegungsbeschränkung für Reiter.
     */
    public boolean bewegungMoeglichRaster(final Point ziel, final boolean setMessage) {

	if ((this.getBewegungsRaster().length > 0) && (this.getBewegungsRaster()[0].length > 0)) {

	    final int relativX = ziel.x - this.getPosition().x;
	    final int relativY = ziel.y - this.getPosition().y;

	    final int bewegungsRasterStartY = this.getBewegungsRaster().length / 2;
	    final int bewegungsRasterStartX = this.getBewegungsRaster()[0].length / 2;

	    if (((bewegungsRasterStartY + relativY) >= 0)
		    && ((bewegungsRasterStartY + relativY) < this.getBewegungsRaster().length)) {
		if (((bewegungsRasterStartX + relativX) >= 0) && ((bewegungsRasterStartX
			+ relativX) < this.getBewegungsRaster()[bewegungsRasterStartY + relativY].length)) {
		    if (!hindernisAufWeg(ziel)) {
			return this.getBewegungsRaster()[bewegungsRasterStartY + relativY][bewegungsRasterStartX
				+ relativX];
		    } else {
			if (setMessage)
			    Spiel.setNachrichtTemporaerKurz("Zug nicht möglich, Hindernis im Weg!");
		    }
		}
	    }
	}

	if (setMessage) {
	    Spiel.setNachrichtTemporaerKurz("Zug auf dieses Feld nicht moeglich. Auserhalb der Reichweite.");
	}

	return false;
    }

    private boolean hindernisAufWeg(Point ziel) {

	boolean aufWeg = false;

	Spielobjekt[][] brett = Spiel.getSpielbrett().copySpielobjekte();

	int startX = this.getPosition().x;
	int startY = this.getPosition().y;
	int zielX = ziel.x;
	int zielY = ziel.y;

	if (startX == zielX) {

	    if (startY - zielY < 0) {
		for (int y = startY + 1; y < zielY; y++) {
		    if (brett[startX][y].getClass().isInstance(Hindernis.class))
			aufWeg = true;

		}
	    } else {
		for (int y = zielY + 1; y < startY; y++) {
		    if (brett[startX][y].getClass().isInstance(Hindernis.class))
			aufWeg = true;
		}
	    }

	} else if (startY == zielY) {

	    if (startX - zielX < 0) {
		for (int x = startX + 1; x < zielX; x++) {
		    if (brett[x][startY].getClass().isInstance(Hindernis.class))
			;
		    aufWeg = true;
		}
	    } else {
		for (int x = zielX + 1; x < startX; x++) {
		    if (brett[x][startY].getClass().isInstance(Hindernis.class))
			aufWeg = true;
		}
	    }

	}

	return aufWeg;
    }
}