package akteure;

import java.util.ArrayList;

import spielobjekte.Bogenschuetze;
import spielobjekte.Figur;
import spielobjekte.Lanzentraeger;
import spielobjekte.Magier;
import spielobjekte.Reiter;
import spielobjekte.Schwertkaempfer;
import spielobjekte.Spielbrett;

public abstract class Spieler {
    protected int nummer;
    private final ArrayList<Figur> helden = new ArrayList<>();

    public Spieler(final int nummer) {
        this.setNummer(nummer);
        this.generateHelden();
    }

    /**
     * Erstellt und f�gt alle Helden, die ein Spieler hat dem Spieler hinzu.
     */
    private void generateHelden() {
        this.helden.add(new Schwertkaempfer(this));
        this.helden.add(new Bogenschuetze(this));
        this.helden.add(new Lanzentraeger(this));
        this.helden.add(new Reiter(this));
        this.helden.add(new Magier(this));
    }

    /**
     * Gibt die Nummer des Spielers wieder
     *
     * @return ein int, das die Nummer des Spielers darstellt
     */
    public int getNummer() {
        return this.nummer;
    }

    /**
     * �berpr�ft {@code this} auf Gleichheit mit einem beliebigen anderen Objekt.
     *
     * @param   obj     ein Objekt, das mit {@code this} verglichen wird
     *
     * @return  true:   {@code this} ist gleich dem Objekt<br/>
     *          false:  {@code this} ist ungleich dem Objekt
     *
     */
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

    /**
     * Setzt die Nummer des Spielers auf {@code nummer}.
     *
     * @param nummer ein int, das die zu setztende Nummer enth�lt.
     */
    private void setNummer(final int nummer) {
        this.nummer = nummer;
    }

    /**
     * Gibt die Helden des Spieler zur�ck.
     *
     * @return eine ArrayList&lt;Figur&gt;, die die Helden des Spielers enth�lt.
     */
    public ArrayList<Figur> getHelden() {
        return this.helden;
    }

    /**
     * Gibt wieder, ob der Spieler noch nicht bewegte Figuren im aktuellen Spielbrett hat und markiert diese
     * als aktiv auf einem �bergebenen Spielobjekt[][].
     *
     * @param   brettAlt    ein Spielobjekt[][], auf dem die Figuren als aktiv markiert werden.
     * @return  true:       Spieler hat noch nicht bewegte Figuren<br/>
     *          false:      Spieler hat keine noch nicht bewegten Figuren
     */
    public boolean hatNochNichtBewegteFiguren(final Spielbrett brettAlt) {
        boolean hatNoch = false;
        for (final Figur f : this.helden) {
            if (!f.istBewegt(false)) {
                ((Figur) (brettAlt.getFeld(f.getPosition()))).symbolAddMarkActive();
                hatNoch = true;
            }
        }
        return hatNoch;
    }

    /**
     * Gibt an, ob der Spieler besiegt wurde.
     * @return  true:   Der Spieler ist besiegt.<br/>
     *          false:  Der Spieler ist nicht besiegt.
     */
    public boolean istBesiegt() {

        if (this.helden.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Entfernt einen Figur von den Helden des Spielers.
     *
     * @param figur eine Figur, die von den Helden des Spielers entfernt werden soll.
     */
    public void entferneHeld(final Figur figur) {
        this.helden.remove(figur);
    }

    /**
     * Setzt das "istBewegt" Attribut aller Figuren des Spielers auf {@code false}.
     */
    public void resetHeldenIstBewegt() {
        for (final Figur f : this.getHelden()) {
            f.setIstBewegt(false);
        }
    }

    /**
     * Setzt das "istBewegt" Attribut aller Figuren des Spielers auf {@code true}.
     */
    public void warten() {
        for (final Figur f : this.getHelden()) {
            f.setIstBewegt(true);
        }
    }
}