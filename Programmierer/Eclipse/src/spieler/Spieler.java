package spieler;

import spielobjekte.Bogenschuetze;
import spielobjekte.Figur;
import spielobjekte.Lanzentraeger;
import spielobjekte.Magier;
import spielobjekte.Reiter;
import spielobjekte.Schwertkaempfer;

public abstract class Spieler {
    private int nummer;
    private Figur[] helden = { new Schwertkaempfer(this), new Bogenschuetze(this), new Lanzentraeger(this),
            new Reiter(this), new Magier(this) };

    public Spieler(int nummer) {
        this.setNummer(nummer);
    }

    public void bewegungsphase() {

    }

    public int getNummer() {
        return this.nummer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Spieler other = (Spieler) obj;
//        if (!Arrays.equals(helden, other.helden))
//            return false;
        if (nummer != other.nummer)
            return false;
        return true;
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

    public boolean hatNochNichtBewegteFiguren() {
        boolean hatNoch = false;
        for (Figur f : helden) {
            if (!f.istBewegt()) {
                hatNoch = true;
                break;
            }
        }
        return hatNoch;
    }

    public boolean istBesiegt() {

        boolean besiegt = true;

        for (Figur f : helden)
            besiegt = f.istTot() ? besiegt : false;

        return besiegt;
    }

}
