package spieler;

import java.awt.Point;
import java.util.ArrayList;

import prototypen.Spiel;
import spielobjekte.Bogenschuetze;
import spielobjekte.Figur;
import spielobjekte.Lanzentraeger;
import spielobjekte.Magier;
import spielobjekte.Reiter;
import spielobjekte.Schwertkaempfer;
import spielobjekte.Spielobjekt;

public abstract class Spieler {
    protected int nummer;
    private final ArrayList<Figur> helden = new ArrayList<>();

    public Spieler(final int nummer) {
        this.setNummer(nummer);
        this.generateHelden();
    }

    private void generateHelden() {
        this.helden.add(new Schwertkaempfer(this));
        this.helden.add(new Bogenschuetze(this));
        this.helden.add(new Lanzentraeger(this));
        this.helden.add(new Reiter(this));
        this.helden.add(new Magier(this));
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

    public ArrayList<Figur> getHelden() {
        return this.helden;
    }

    public boolean hatNochNichtBewegteFiguren(final Spielobjekt[][] brettAlt) {
        boolean hatNoch = false;
        for (final Figur f : this.helden) {
            if (!f.istBewegt(false)) {
                ((Figur) (brettAlt[f.getPosition().y][f.getPosition().x])).symbolAddMarkActive();
                hatNoch = true;
            }
        }
        return hatNoch;
    }

    public void addMovementMarks(final Spielobjekt[][] brettAlt) {
        for (final Figur f : this.helden) {
            if (!f.istBewegt(false)) {
                for (int y = 0; y < brettAlt.length; y++) {
                    for (int x = 0; x < brettAlt[y].length; x++) {
                        if (f.bewegungMoeglich(new Point(x, y), false, false)) {
                            brettAlt[y][x].symbolAddMarkMovementPossible();
                        }
                    }
                }
                Spiel.setNachrichtTemporaerKurz("");
            }
        }
    }

    public boolean istBesiegt() {

        if (this.helden.size() == 0) {
            return true;
        }
        return false;
    }

    public void entferneHeld(final Figur figur) {
        this.helden.remove(figur);
    }

}
