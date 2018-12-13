package spieler;

import spielobjekte.Bogenschuetze;
import spielobjekte.Figur;
import spielobjekte.Lanzentraeger;
import spielobjekte.Magier;
import spielobjekte.Reiter;
import spielobjekte.Schwertkaempfer;

public abstract class Spieler {
    private int nummer;
    private Figur[] helden;

    public Spieler(final int nummer) {
        this.setNummer(nummer);
        this.setHelden(this.generateHelden(this));
    }

    private Figur[] generateHelden(final Spieler spieler) {
        final Figur[] helden = { new Schwertkaempfer(this), new Bogenschuetze(this), new Lanzentraeger(this),
                new Reiter(this), new Magier(this) };
        return helden;
    }

    public void bewegungsphase() {

    }

    public int getNummer() {
        return this.nummer;
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
        final Spieler other = (Spieler) obj;
//        if (!Arrays.equals(helden, other.helden))
//            return false;
        if (this.nummer != other.nummer) {
            return false;
        }
        return true;
    }

    private void setNummer(final int nummer) {
        this.nummer = nummer;
    }

    public Figur[] getHelden() {
        return this.helden;
    }

    private void setHelden(final Figur[] helden) {
        this.helden = helden;
    }

    public boolean hatNochNichtBewegteFiguren() {
        boolean hatNoch = false;
        for (final Figur f : this.helden) {
            if (!f.istBewegt()) {
                hatNoch = true;
            }
        }
        return hatNoch;
    }

    public boolean istBesiegt() {

        boolean besiegt = true;

        for (final Figur f : this.helden) {
            besiegt = f.istTot() ? besiegt : false;
        }

        return besiegt;
    }

}
