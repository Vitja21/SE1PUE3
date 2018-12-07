package spielobjekte;

public final class Bogenschuetze extends Figur {

    private static boolean[][] bewegungsRaster = {
            { true, true, true, true, true },
            { true, true, true, true, true },
            { true, true, false, true, true },
            { true, true, true, true, true },
            { true, true, true, true, true },
    };

    private static boolean[][] angriffsRaster = {
            { true, false, true, false, true },
            { false, true, true, true, false },
            { true, true, false, true, true },
            { false, true, true, true, false },
            { true, false, true, false, true },
    };

    private static char[][] symbol = { { 'B' } };

    private static int lebenspunkte = 1;

    public Bogenschuetze() {
        super(Bogenschuetze.lebenspunkte, Bogenschuetze.bewegungsRaster, Bogenschuetze.angriffsRaster,
                Bogenschuetze.symbol);
    }

}