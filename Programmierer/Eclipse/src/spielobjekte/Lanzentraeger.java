package spielobjekte;

import akteure.Spieler;

public final class Lanzentraeger extends Figur {

    private static boolean[][] bewegungsRaster = { { true, true, true }, { true, false, true }, { true, true, true }, };

    private static boolean[][] angriffsRaster = { { false, true, true, true, false },
            { true, false, false, false, true }, { true, false, false, false, true },
            { true, false, false, false, true }, { false, true, true, true, false }, };

    private static char symbol = 'L';
    private static String name = "Lanzentr�ger";

    private static int lebenspunkte = 1;

    public Lanzentraeger(final Spieler team) {
        super(Lanzentraeger.name, Lanzentraeger.lebenspunkte, Lanzentraeger.bewegungsRaster,
                Lanzentraeger.angriffsRaster,
                Lanzentraeger.symbol, team);
    }
}