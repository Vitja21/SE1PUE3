package spielobjekte;

import java.awt.Point;

import prototypen.Spielbrett;

public abstract class Figur extends Spielobjekt {

    private int lebenspunkte;
    private boolean[][] bewegungsRaster;
    private boolean[][] angriffsRaster;

    public Figur(final int lebenspunkte, final boolean[][] bewegungsRaster, final boolean[][] angriffsRaster,
            final char[][] symbol) {
        super(symbol);
        this.setLebenspunkte(lebenspunkte);
        this.setBewegungsRaster(bewegungsRaster);
        this.setAngriffsRaster(angriffsRaster);
    }

    public void bewegen() {
        // TODO
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
        Spielbrett.getInstance().setFehlermeldung("Zug auf dieses Feld nicht moeglich. Auserhalb der Reichweite.");
        return false;
    }

    @Override
    public boolean bewegungMoeglich(Point ziel) {

        if (Spielbrett.getInstance().bewegungMoeglichSpielfeld(this.getPosition(), ziel)
                && Spielbrett.getInstance().bewegungMoeglichBelegt(ziel)
                && this.bewegungMoeglichRaster(ziel)) {
            return true;
        } else {
            return false;
        }
    }
}
