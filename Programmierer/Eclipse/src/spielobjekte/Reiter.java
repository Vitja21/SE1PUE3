package spielobjekte;

import java.awt.Point;

import spieler.Spieler;

public final class Reiter extends Figur {

    private static boolean[][] bewegungsRaster = { { false, false, false, true, false, false, false },
            { false, false, false, true, false, false, false }, { false, false, false, true, false, false, false },
            { true, true, true, false, true, true, true }, { false, false, false, true, false, false, false },
            { false, false, false, true, false, false, false }, { false, false, false, true, false, false, false }, };

    private static boolean[][] angriffsRaster = { { true, true, true }, { true, false, true }, { true, true, true } };

    private static char symbol = 'R';
    private static String name = "Reiter";

    private static int lebenspunkte = 2;

    public Reiter(final Spieler team) {
        super(Reiter.name, Reiter.lebenspunkte, Reiter.bewegungsRaster, Reiter.angriffsRaster, Reiter.symbol, team);
    }

    @Override
    /**
     * Bewegungsbeschränkung für Reiter.
     */
    public boolean bewegungMoeglich(final Point ziel, final boolean setMessage) {

        // TODO: Bewegungsbeschränking für Reiter.

        return super.bewegungMoeglich(ziel, setMessage);

    }
}