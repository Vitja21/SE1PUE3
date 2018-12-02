package spielobjekte;

public abstract class Figur extends Spielobjekt {

    private int lebenspunkte;
    private int schrittweite;

    public Figur(int lebenspunkte, int schrittweite, String symbol) {
	super(symbol);
	this.lebenspunkte = lebenspunkte;
	this.schrittweite = schrittweite;
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

}
