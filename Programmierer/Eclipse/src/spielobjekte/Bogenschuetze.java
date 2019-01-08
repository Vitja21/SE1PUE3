package spielobjekte;

import akteure.Spieler;

public final class Bogenschuetze extends Figur {

    private static boolean[][] bewegungsRaster = { { true, true, true, true, true }, { true, true, true, true, true },
            { true, true, false, true, true }, { true, true, true, true, true }, { true, true, true, true, true }, };

    private static boolean[][] angriffsRaster = { { true, false, true, false, true },
            { false, true, true, true, false }, { true, true, false, true, true }, { false, true, true, true, false },
            { true, false, true, false, true }, };

    private static char symbol = 'B';
    private static String name = "Bogensch�tze";

    private static int lebenspunkte = 1;

    public Bogenschuetze(final Spieler team) {
        super(Bogenschuetze.name, Bogenschuetze.lebenspunkte, Bogenschuetze.bewegungsRaster,
                Bogenschuetze.angriffsRaster,
                Bogenschuetze.symbol, team);
    }
}