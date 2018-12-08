package spieler;

import java.util.ArrayList;

import spielobjekte.Bogenschuetze;
import spielobjekte.Figur;
import spielobjekte.Lanzentraeger;
import spielobjekte.Magier;
import spielobjekte.Reiter;
import spielobjekte.Schwertkaempfer;
import spielphasen.Kampf;

public abstract class Spieler {
    private int nummer;
    private Figur[] helden = { new Schwertkaempfer(this), new Bogenschuetze(this), new Lanzentraeger(this),
	    new Reiter(this), new Magier(this) };

    public Spieler(int nummer) {
	this.setNummer(nummer);
    }

    public void bewegungsphase() {

    }

    public ArrayList<Kampf> angriffsphase() {
	ArrayList<Kampf> phasen = new ArrayList<>();
	return phasen;
    }

    public int getNummer() {
	return this.nummer;
    }

    private void setNummer(int nummer) {
	this.nummer = nummer;
    }

    public Figur[] getHelden() {
	return this.helden;
    }

    @SuppressWarnings("unused")
    private void setHelden(Figur[] helden) {
	this.helden = helden;
    }

    public boolean istBesiegt() {

	boolean besiegt = true;

	for (Figur f : helden)
	    besiegt = f.istTot() ? besiegt : false;

	return besiegt;
    }

}
