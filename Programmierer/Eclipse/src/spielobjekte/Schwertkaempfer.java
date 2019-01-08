package spielobjekte;

import akteure.Spieler;

public final class Schwertkaempfer extends Figur {

    private static boolean[][] bewegungsRaster = { { true, true, true }, { true, false, true }, { true, true, true }, };

    private static boolean[][] angriffsRaster = { { true, true, true }, { true, false, true }, { true, true, true }, };

    private static char symbol = 'S';
    private static String name = "Schwertk�mpfer";

    private static int lebenspunkte = 3;

    public Schwertkaempfer(final Spieler team) {
        super(Schwertkaempfer.name, Schwertkaempfer.lebenspunkte, Schwertkaempfer.bewegungsRaster,
                Schwertkaempfer.angriffsRaster, Schwertkaempfer.symbol, team);
    }
}