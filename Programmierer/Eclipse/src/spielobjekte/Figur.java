package spielobjekte;

import java.awt.Point;
import java.util.Arrays;

import prototypen.Spiel;
import prototypen.Spielbrett;
import spieler.Spieler;

public abstract class Figur extends Spielobjekt {

    private String name;
    private int lebenspunkte;
    private Spieler team;
    private boolean[][] bewegungsRaster;
    private boolean[][] angriffsRaster;
    protected boolean istBewegt = false;

    public Figur(final String name, final int lebenspunkte, final boolean[][] bewegungsRaster,
	    final boolean[][] angriffsRaster, final char symbol, final Spieler team) {
	super(symbol);
	this.name = name;
	this.setLebenspunkte(lebenspunkte);
	this.setBewegungsRaster(bewegungsRaster);
	this.setAngriffsRaster(angriffsRaster);
	this.setTeam(team);
	this.symbolAddTeamNumber();
	this.symbolAddCurrentLives();
    }

    private void symbolAddTeamNumber() {
	this.symbol[1][4] = (char) ((this.getTeam().getNummer() + '0'));
    }

    public void symbolAddMarkActive() {
	this.symbol[0][0] = '╔';
	this.symbol[0][6] = '╗';
	this.symbol[2][0] = '╚';
	this.symbol[2][6] = '╝';
    }

    public void symbolRemoveMarkActive() {
	this.symbol[0][0] = ' ';
	this.symbol[0][6] = ' ';
	this.symbol[2][0] = ' ';
	this.symbol[2][6] = ' ';
    }

    public void symbolAddMarkCanBeAttacked() {
	this.symbol[0][0] = '┌';
	this.symbol[0][6] = '┐';
	this.symbol[2][0] = '└';
	this.symbol[2][6] = '┘';
	this.symbol[1][0] = '>';
	this.symbol[1][6] = '<';
	this.symbol[2][3] = '^';
    }

    public void symbolRemoveMarkCanBeAttacked() {
	this.symbolRemoveMarkActive();
	this.symbol[1][0] = ' ';
	this.symbol[1][6] = ' ';
	this.symbol[2][3] = ' ';
    }

    public void symbolAddCurrentLives() {
	for (int l = 2; l < 5; l++) {
	    if ((this.getLebenspunkte() + 2) > l) {
		this.symbol[0][l] = 'o';
	    } else {
		this.symbol[0][l] = ' ';
	    }
	}
    }

    public void bewegen(final Spielbrett spielbrett, final Point ziel) {
	spielbrett.bewege(this.getPosition(), ziel);
	this.istBewegt = true;
    }

    public int getLebenspunkte() {
	return this.lebenspunkte;
    }

    public void setLebenspunkte(final int lebenspunkte) {
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

    protected void setAngriffsRaster(final boolean[][] angriffsRaster) {
	this.angriffsRaster = angriffsRaster;
    }

    // überprüft, ob das zielfeld im bewegungsmuster der figur liegt
    protected boolean bewegungMoeglichRaster(final Point ziel, final boolean setMessage) {

	if ((this.getBewegungsRaster().length > 0) && (this.getBewegungsRaster()[0].length > 0)) {

	    final int relativX = ziel.x - this.getPosition().x;
	    final int relativY = ziel.y - this.getPosition().y;

	    final int bewegungsRasterStartY = this.getBewegungsRaster().length / 2;
	    final int bewegungsRasterStartX = this.getBewegungsRaster()[0].length / 2;

	    if (((bewegungsRasterStartY + relativY) >= 0)
		    && ((bewegungsRasterStartY + relativY) < this.getBewegungsRaster().length)) {
		if (((bewegungsRasterStartX + relativX) >= 0) && ((bewegungsRasterStartX
			+ relativX) < this.getBewegungsRaster()[bewegungsRasterStartY + relativY].length)) {
		    return this.getBewegungsRaster()[bewegungsRasterStartY + relativY][bewegungsRasterStartX
			    + relativX];
		}
	    }
	}

	if (setMessage) {
	    Spiel.setNachrichtTemporaerKurz("Zug auf dieses Feld nicht moeglich. Auserhalb der Reichweite.");
	}
	return false;
    }

    // überprüft, ob das angriffsziel im angriffsmuster der figur liegt
    protected boolean angriffMoeglichRaster(final Point ziel) {

	if ((this.getAngriffsRaster().length > 0) && (this.getAngriffsRaster()[0].length > 0)) {

	    final int relativX = ziel.x - this.getPosition().x;
	    final int relativY = ziel.y - this.getPosition().y;
	    final int angriffsRasterStartY = this.getAngriffsRaster().length / 2;
	    final int angriffsRasterStartX = this.getAngriffsRaster()[0].length / 2;

	    if (((angriffsRasterStartY + relativY) >= 0)
		    && ((angriffsRasterStartY + relativY) < this.getAngriffsRaster().length)) {
		if (((angriffsRasterStartX + relativX) >= 0) && ((angriffsRasterStartX
			+ relativX) < this.getAngriffsRaster()[angriffsRasterStartY + relativY].length)) {
		    return this.getAngriffsRaster()[angriffsRasterStartY + relativY][angriffsRasterStartX + relativX];
		}
	    }
	}
	return false;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (this.getClass() != obj.getClass()) {
	    return false;
	}
	final Figur other = (Figur) obj;
	if (!Arrays.deepEquals(this.angriffsRaster, other.angriffsRaster)) {
	    return false;
	}
	if (!Arrays.deepEquals(this.bewegungsRaster, other.bewegungsRaster)) {
	    return false;
	}
	if (this.istBewegt != other.istBewegt) {
	    return false;
	}
	if (this.lebenspunkte != other.lebenspunkte) {
	    return false;
	}
	if (this.name == null) {
	    if (other.name != null) {
		return false;
	    }
	} else if (!this.name.equals(other.name)) {
	    return false;
	}
	if (this.team == null) {
	    if (other.team != null) {
		return false;
	    }
	} else if (!this.team.equals(other.team)) {
	    return false;
	}
	return true;
    }

    // überprüft, ob der angriff auf das ausgewählte ziel möglich ist
    @Override
    public boolean angriffMoeglich(final Point ziel) {

	// überprüft, ob das angriffsziel im angriffsmuster der figur liegt
	if (this.angriffMoeglichRaster(ziel)) {
	    // überprüft, ob das angriffsziel im gegnerischen team ist
	    if (Spiel.getSpielbrett().getFeld(ziel).istAngreifbar(this)) {
		// überprüft, ob es mögliche kämpfe gibt
		if (Spiel.getKaempfe().size() > 0) {
		    for (final Kampf k : Spiel.getKaempfe()) {
			if (this.equals(k.getAngreifer())) {
			    return false;
			}
		    }
		    return true;
		} else {
		    return true;
		}
	    }
	}
	return false;
    }

    // überprüft, ob das angriffsziel im gegnerischen team ist
    @Override
    public boolean istAngreifbar(final Figur f) {
	if (this.getTeam() == f.getTeam()) {
	    return false;
	} else {
	    return true;
	}
    }

    // überprüft, ob diese figur sich auf ein zielfeld bewegen kann
    @Override
    public boolean bewegungMoeglich(final Spielbrett spielbrett, final Point ziel, final boolean figurenZaehlen,
	    final boolean setMessage) {

	if (figurenZaehlen) {
	    if (!this.istBewegt(setMessage)
		    // erklärungen bei den jeweiligen methoden
		    && spielbrett.bewegungMoeglichSpielfeld(this.getPosition(), ziel, setMessage)
		    && spielbrett.bewegungMoeglichBelegt(ziel, setMessage)
		    && this.bewegungMoeglichRaster(ziel, setMessage)) {
		return true;
	    } else {
		return false;
	    }
	} else {
	    if (!this.istBewegt(setMessage)
		    // erklärungen bei den jeweiligen methoden
		    && spielbrett.bewegungMoeglichSpielfeld(this.getPosition(), ziel, setMessage)
		    && !(spielbrett.getFeld(ziel) instanceof Hindernis)
		    && this.bewegungMoeglichRaster(ziel, setMessage)) {
		return true;
	    } else {
		return false;
	    }
	}
    }

    public Spieler getTeam() {
	return this.team;
    }

    public void setTeam(final Spieler team) {
	this.team = team;
    }

    public boolean istTot() {
	if (this.getLebenspunkte() <= 0) {
	    return true;
	} else {
	    return false;
	}
    }

    public boolean istBewegt(final boolean setMessage) {
	if (!this.istBewegt && setMessage) {
	    Spiel.setNachrichtTemporaerKurz("Bewegung nicht möglich: Figur wurde bereits bewegt.");
	}
	return this.istBewegt;
    }

    public void setIstBewegt(final boolean istBewegt) {
	this.istBewegt = istBewegt;
    }

    public String getName() {
	return this.name;
    }

    @SuppressWarnings("unused")
    private void setName(final String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return this.name;
    }

    public String printPosition() {
	return String.format("%s%02d", String.valueOf((char) (this.getPosition().y + 'A')), this.getPosition().x + 1);
    }
}
