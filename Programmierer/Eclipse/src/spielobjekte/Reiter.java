package spielobjekte;

public final class Reiter extends Figur {

    private static boolean[][] bewegungsRaster = {
            { false, false, false, true, false, false, false },
            { false, false, false, true, false, false, false },
            { false, false, false, true, false, false, false },
            { true, true, true, false, true, true, true },
            { false, false, false, true, false, false, false },
            { false, false, false, true, false, false, false },
            { false, false, false, true, false, false, false },
    };

    private static boolean[][] angriffsRaster = {
            { true, true, true },
            { true, false, true },
            { true, true, true }
    };

    private static char[][] symbol = { { 'R' } };

    private static int lebenspunkte = 2;

    public Reiter() {
        super(Reiter.lebenspunkte, Reiter.bewegungsRaster, Reiter.angriffsRaster, Reiter.symbol);
    }

}