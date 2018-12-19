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

//    @Override
//    /**
//     * Bewegungsbeschränkung für Reiter.
//     */
//    public boolean bewegungMoeglichRaster(final Point ziel, final boolean setMessage) {
//
//	if ((this.getBewegungsRaster().length > 0) && (this.getBewegungsRaster()[0].length > 0)) {
//
//	    final int relativX = ziel.x - this.getPosition().x;
//	    final int relativY = ziel.y - this.getPosition().y;
//
//	    final int bewegungsRasterStartY = this.getBewegungsRaster().length / 2;
//	    final int bewegungsRasterStartX = this.getBewegungsRaster()[0].length / 2;
//
//	    if (((bewegungsRasterStartY + relativY) >= 0)
//		    && ((bewegungsRasterStartY + relativY) < this.getBewegungsRaster().length)) {
//		if (((bewegungsRasterStartX + relativX) >= 0) && ((bewegungsRasterStartX
//			+ relativX) < this.getBewegungsRaster()[bewegungsRasterStartY + relativY].length)) {
//		    if (!hindernisAufWeg(ziel)) {
//			return this.getBewegungsRaster()[bewegungsRasterStartY + relativY][bewegungsRasterStartX
//				+ relativX];
//		    } else {
//			if (setMessage)
//			    Spiel.setNachrichtTemporaerKurz("Zug nicht möglich, Hindernis im Weg!");
//		    }
//		}
//	    }
//	}
//
//	if (setMessage) {
//	    Spiel.setNachrichtTemporaerKurz("Zug auf dieses Feld nicht moeglich. Auserhalb der Reichweite.");
//	}
//
//	return false;
//    }

    @Override
    public boolean bewegungMoeglich(final Point ziel, final boolean setMessage) {

	if (Spielbrett.getInstance().bewegungMoeglichSpielfeld(this.getPosition(), ziel, setMessage)
		&& Spielbrett.getInstance().bewegungMoeglichBelegt(ziel, setMessage)
		&& this.bewegungMoeglichRaster(ziel, setMessage) && !this.istBewegt) {

	    return (!hindernisAufWeg(ziel)) ? true : false;
	} else {

	    return false;
	}
    }

    private boolean hindernisAufWeg(Point ziel) {

	boolean aufWeg = false;

	Spielobjekt[][] brett = Spiel.getSpielbrett().copySpielobjekte();

	int startX = this.getPosition().x;
	int startY = this.getPosition().y;
	int zielX = ziel.x;
	int zielY = ziel.y;

	// Überprüfen Y-Achse
	if (startX == zielX) {
	    // nach unten
	    if (startY - zielY < 0) {
		for (int y = startY + 1; y < zielY; y++) {

		    Spielobjekt so = brett[startX][y];
		    boolean b1 = so.getClass().equals(Hindernis.class);
		    boolean b2 = Hindernis.class.isInstance(so.getClass());
		    boolean b3 = so.getClass().isInstance((Hindernis.class));
		    boolean b4 = Hindernis.class.equals(so.getClass());
		    boolean b5 = so instanceof Hindernis;

		    if (brett[startX][y].getClass().equals(Hindernis.class))
//		    if (Hindernis.class.isInstance(brett[startX][y]))
			aufWeg = true;

		}
		// nach oben
	    } else {
		for (int y = zielY + 1; y < startY; y++) {

		    Spielobjekt so = brett[startX][y];
		    boolean b1 = so.getClass().equals(Hindernis.class);
		    boolean b2 = Hindernis.class.isInstance(so.getClass());
		    boolean b3 = so.getClass().isInstance((Hindernis.class));
		    boolean b4 = Hindernis.class.equals(so.getClass());
		    boolean b5 = so instanceof Hindernis;

		    if (brett[startX][y].getClass().equals(Hindernis.class))
//		    if (Hindernis.class.isInstance(brett[startX][y]))
			aufWeg = true;
		}
	    }

	    // Überprüfen X-Achse
	} else if (startY == zielY) {
	    // nach rechts
	    if (startX - zielX < 0) {
		for (int x = startX + 1; x < zielX; x++) {

		    Spielobjekt so = brett[x][startY];
		    boolean b1 = so.getClass().equals(Hindernis.class);
		    boolean b2 = Hindernis.class.isInstance(so.getClass());
		    boolean b3 = so.getClass().isInstance((Hindernis.class));
		    boolean b4 = Hindernis.class.equals(so.getClass());
		    boolean b5 = so instanceof Hindernis;

		    if (brett[x][startY].getClass().equals(Hindernis.class))
//		    if (Hindernis.class.isInstance(brett[x][startY]))
			aufWeg = true;
		}
		// nach links
	    } else {
		for (int x = zielX + 1; x < startX; x++) {

		    Spielobjekt so = brett[x][startY];
		    boolean b1 = so.getClass().equals(Hindernis.class);
		    boolean b2 = Hindernis.class.isInstance(so.getClass());
		    boolean b3 = so.getClass().isInstance((Hindernis.class));
		    boolean b4 = Hindernis.class.equals(so.getClass());
		    boolean b5 = so instanceof Hindernis;

		    if (brett[x][startY].getClass().equals(Hindernis.class))
//		    if (Hindernis.class.isInstance(brett[x][startY]))
			aufWeg = true;
		}
	    }

	}

	return aufWeg;
    }
}