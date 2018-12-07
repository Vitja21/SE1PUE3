package spielobjekte;

public final class Schwertkaempfer extends Figur {

    private static boolean[][] bewegungsRaster = {
            { true, true, true },
            { true, false, true },
            { true, true, true },
    };

    private static boolean[][] angriffsRaster = {
            { true, true, true },
            { true, false, true },
            { true, true, true },
    };

    private static char[][] symbol = { { 'S' } };

    private static int lebenspunkte = 3;

    public Schwertkaempfer() {
        super(Schwertkaempfer.lebenspunkte, Schwertkaempfer.bewegungsRaster, Schwertkaempfer.angriffsRaster,
                Schwertkaempfer.symbol);
    }

}