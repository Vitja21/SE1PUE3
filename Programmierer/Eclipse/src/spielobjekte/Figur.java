package spielobjekte;

import spieler.Spieler;

public abstract class Figur extends Spielobjekt {

    private int lebenspunkte;
    private int schrittweite;
    private Spieler team;
    private boolean istTot = false;

    public Figur(int lebenspunkte, int schrittweite, String symbol, Spieler team) {
	super(symbol);
	this.lebenspunkte = lebenspunkte;
	this.schrittweite = schrittweite;
	this.team = team;
    }

    public Figur(int lebenspunkte2, int schrittweite2, char[][] symbol, Spieler team) {
	super(symbol);
	this.lebenspunkte = lebenspunkte2;
	this.schrittweite = schrittweite2;
	this.team = team;
    }

    public void bewegen() {

    }

    public void angreifen() {

    }

    public int getLebenspunkte() {
	return lebenspunkte;
    }

    public void setLebenspunkte(int lebenspunkte) {
	this.lebenspunkte = lebenspunkte;
    }

    public int getSchrittweite() {
	return schrittweite;
    }

    @SuppressWarnings("unused")
    private void setSchrittweite(int schrittweite) {
	this.schrittweite = schrittweite;
    }

    public boolean istTot() {
	return istTot;
    }

    public void setIstTot(boolean istTot) {
	this.istTot = istTot;
    }

    public Spieler getTeam() {
	return team;
    }

    @SuppressWarnings("unused")
    private void setTeam(Spieler team) {
	this.team = team;
    }

}
