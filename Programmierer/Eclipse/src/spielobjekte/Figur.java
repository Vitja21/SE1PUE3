package spielobjekte;

public abstract class Figur extends Spielobjekt {

    private int lebenspunkte;
    private boolean[][] bewegungsRaster;
    private boolean[][] angriffsRaster;

    public Figur(final int lebenspunkte, boolean[][] bewegungsRaster, boolean[][] angriffsRaster,
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
        return angriffsRaster;
    }

    private void setAngriffsRaster(boolean[][] angriffsRaster) {
        this.angriffsRaster = angriffsRaster;
    }

}
