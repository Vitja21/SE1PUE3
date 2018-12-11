package spielobjekte;

import spieler.Spieler;

public final class Magier extends Figur {

    private static boolean[][] bewegungsRaster = { { true, true, true, true, true }, { true, true, true, true, true },
            { true, true, false, true, true }, { true, true, true, true, true }, { true, true, true, true, true }, };

    // TODO: FALLUNTERSCHEIDUNG JE NACH SPIELSEITE !!!
    private static boolean[][] angriffsRaster = {
            { false, false, false, false, false },
            { false, false, false, true, true },
            { false, false, false, true, true },
            { false, false, false, true, true },
            { false, false, false, false, false }, };

    private static boolean[][] angriffsRasterS2 = {
            { false, false, false, false, false },
            { true, true, false, false, false },
            { true, true, false, false, false },
            { true, true, false, false, false },
            { false, false, false, false, false }, };

    private static char[][] symbol = { { 'M' } };

    private static int lebenspunkte = 1;

    public Magier(Spieler team) {
        super("Magier", Magier.lebenspunkte, Magier.bewegungsRaster, Magier.angriffsRaster, Magier.symbol, team);
        if (team.getNummer() != 1) {
            this.setAngriffsRaster(angriffsRasterS2);
        }
    }

}