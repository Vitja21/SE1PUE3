package spielobjekte;

import java.awt.Point;

import prototypen.Spiel;
import prototypen.Spielbrett;
import spieler.Spieler;

public abstract class Figur extends Spielobjekt {

    private String name;
    private int lebenspunkte;
    private Spieler team;
    private boolean[][] bewegungsRaster;
    private boolean[][] angriffsRaster;
    private boolean istBewegt = false;

    public Figur(final String name, final int lebenspunkte, final boolean[][] bewegungsRaster,
	    final boolean[][] angriffsRaster, final char[][] symbol, final Spieler team) {
	super(symbol);
	this.name = name;
	this.setLebenspunkte(lebenspunkte);
	this.setBewegungsRaster(bewegungsRaster);
	this.setAngriffsRaster(angriffsRaster);
	this.setTeam(team);
    }

    public void bewegen(Point start, Point ziel) {

	if (Spiel.getSpielbrett().getFeld(start).bewegungMoeglich(ziel)) {
	    // Bewegendes Objekt von alt auf neu schieben.
	    Spiel.getSpielbrett().setFeld(ziel, Spiel.getSpielbrett().getFeld(start));
	    this.istBewegt = true;
	    // Bewegtes Objekt von alter Position entfernen.
	    Spiel.getSpielbrett().setFeld(start, new Spielobjekt(" "));
	}
	Spiel.updateConsole();
    }

    public void angreifen() {
	// TODO
    }

    public int getLebenspunkte() {
	return this.lebenspunkte;
    }

    private void setLebenspunkte(final int lebenspunkte) {
	this.lebenspunkte = lebenspunkte;
    }

    public boolean[][] getBewegungsRaster() {
	return this.bewegungsRaster;
    }

    private void setBewegungsRaster(final boolean[][] bewegungsRaster) {
	this.bewegungsRaster = bewegungsRaster;
    }

    public boolean[][] getAngriffsRaster() {
	return this.angriffsRaster;
    }

    private void setAngriffsRaster(final boolean[][] angriffsRaster) {
	this.angriffsRaster = angriffsRaster;
    }

    private boolean bewegungMoeglichRaster(Point ziel) {

	if (this.getBewegungsRaster().length > 0 && this.getBewegungsRaster()[0].length > 0) {

	    final int relativX = ziel.x - this.getPosition().x;
	    final int relativY = ziel.y - this.getPosition().y;

	    final int bewegungsRasterStartY = this.getBewegungsRaster().length / 2;
	    final int bewegungsRasterStartX = this.getBewegungsRaster()[0].length / 2;

	    if (bewegungsRasterStartY + relativY >= 0
		    && bewegungsRasterStartY + relativY < this.getBewegungsRaster().length) {
		if (bewegungsRasterStartX + relativX >= 0 && bewegungsRasterStartX
			+ relativX < this.getBewegungsRaster()[bewegungsRasterStartY + relativY].length) {
		    return this.getBewegungsRaster()[bewegungsRasterStartY + relativY][bewegungsRasterStartX
			    + relativX];
		}
	    }
	}
	Spiel.setNachrichtTemporaerKurz("Zug auf dieses Feld nicht moeglich. Auserhalb der Reichweite.");
	return false;
    }

    @Override
    public boolean bewegungMoeglich(Point ziel) {

	if (Spielbrett.getInstance().bewegungMoeglichSpielfeld(this.getPosition(), ziel)
		&& Spielbrett.getInstance().bewegungMoeglichBelegt(ziel) && this.bewegungMoeglichRaster(ziel)
		&& !this.istBewegt) {
	    return true;
	} else {
	    return false;
	}
    }

    public Spieler getTeam() {
	return team;
    }

    public void setTeam(Spieler team) {
	this.team = team;
    }

    public boolean istTot() {
	if (this.getLebenspunkte() <= 0) {
	    return true;
	} else {
	    return false;
	}
    }

    public boolean istBewegt() {
	return istBewegt;
    }

    public void setIstBewegt(boolean istBewegt) {
	this.istBewegt = istBewegt;
    }

    public String getName() {
	return name;
    }

    @SuppressWarnings("unused")
    private void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return this.name;
    }
}
