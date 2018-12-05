package prototypen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import spielobjekte.Bogenschuetze;
import spielobjekte.Figur;
import spielobjekte.Hindernis;
import spielobjekte.Lanzentraeger;
import spielobjekte.Magier;
import spielobjekte.Reiter;
import spielobjekte.Schwertkaempfer;
import spielobjekte.Spielobjekt;

public class Spielbrett {

    private boolean DEBUG = false;
    private static Spielbrett instance;

    private String fehlermeldung;

    ArrayList<Figur> figurenSpieler1 = new ArrayList<>();
    ArrayList<Figur> figurenSpieler2 = new ArrayList<>();

    private Spielobjekt[][] spielobjekte;
    private int xLaenge;
    private int yLaenge;

    // TODO: Evtl. Dynamische Bestimmung der feldLaenge abhängig von
    // xLaenge, yLaenge, oder maxSymbolgröße;
    private int xFeldLaenge = 5;
    private int yFeldLaenge = 3;

    private Spielbrett(Spielobjekt[][] spielobjekte) {
        this.setSpielobjekte(spielobjekte);
    }

    private Spielbrett() {
        this(10, 10);
    }

    private Spielobjekt[][] copySpielobjekte() {
        Spielobjekt[][] copySpielobjekte = new Spielobjekt[yLaenge][xLaenge];
        for (int y = 0; y < yLaenge; y++) {
            for (int x = 0; x < xLaenge; x++) {
                copySpielobjekte[y][x] = this.spielobjekte[y][x];
            }
        }
        return copySpielobjekte;
    }

    private Spielbrett(int xLaenge, int yLaenge, String... parameter) {

        this.setParameter(parameter);

        this.generiereSpielbrettCharArray(xLaenge, yLaenge);
        this.generiereFiguren();
        this.setFiguren();
        this.setHindernisse();
        this.updateConsole();
    }

    private void generiereSpielbrettCharArray(int xLaenge, int yLaenge) {

        this.spielobjekte = new Spielobjekt[yLaenge][xLaenge];
        this.setDimensionen();
    }

    private void setFiguren() {
        ArrayList<Figur> gesetzteFiguren = new ArrayList<>();
        Random randomGenerator = new Random();
        int index;
        final int maxFiguren = (figurenSpieler1.size() > figurenSpieler2.size() ? figurenSpieler1.size()
                : figurenSpieler2.size());
        int x = 0;
        for (int y = 0; y < this.spielobjekte.length; y++) {
            // SPIELER 1
            if (y % (this.spielobjekte.length / maxFiguren) == 0
                    || y % (this.spielobjekte.length / maxFiguren) == (this.spielobjekte.length / maxFiguren)) {
                do {
                    index = randomGenerator.nextInt(figurenSpieler1.size());
                } while (gesetzteFiguren.contains(figurenSpieler1.get(index))
                        && (gesetzteFiguren.size() < (figurenSpieler1.size() + figurenSpieler2.size())));
                this.spielobjekte[y][x] = figurenSpieler1.get(index);
                gesetzteFiguren.add(figurenSpieler1.get(index));
                x = this.spielobjekte[y].length - 1;
            }
            if (y % (this.spielobjekte.length / maxFiguren) + 1 == (this.spielobjekte.length / maxFiguren)) {
                do {
                    index = randomGenerator.nextInt(figurenSpieler2.size());
                } while (gesetzteFiguren.contains(figurenSpieler2.get(index))
                        && (gesetzteFiguren.size() < (figurenSpieler1.size() + figurenSpieler2.size())));
                this.spielobjekte[y][x] = figurenSpieler2.get(index);
                gesetzteFiguren.add(figurenSpieler2.get(index));
                x = 0;
            }
        }
    }

    private void setHindernisse() {

        Random randomGenerator = new Random();
        int y;
        int x;

        int anzahlGrosseHindernisse = 2;
        int anzahlKleineHindernisse = 3;

        while (anzahlGrosseHindernisse > 0) {
            y = randomGenerator.nextInt(this.yLaenge);
            x = 1 + randomGenerator.nextInt(this.xLaenge - 3);

            if (this.spielobjekte[y][x] == null & this.spielobjekte[y][x + 1] == null
                    || this.spielobjekte[y][x].isEmpty() && this.spielobjekte[y][x + 1].isEmpty()) {
                this.spielobjekte[y][x] = new Hindernis();
                this.spielobjekte[y][x + 1] = new Hindernis();
                anzahlGrosseHindernisse--;
            }
        }

        while (anzahlKleineHindernisse > 0) {
            y = randomGenerator.nextInt(this.yLaenge);
            x = 1 + randomGenerator.nextInt(this.xLaenge - 2);

            if (this.spielobjekte[y][x] == null || this.spielobjekte[y][x].isEmpty()) {
                this.spielobjekte[y][x] = new Hindernis();
                anzahlKleineHindernisse--;
            }
        }
    }

    private void generiereFiguren() {

        figurenSpieler1.add(new Bogenschuetze());
        figurenSpieler1.add(new Lanzentraeger());
        figurenSpieler1.add(new Magier());
        figurenSpieler1.add(new Reiter());
        figurenSpieler1.add(new Schwertkaempfer());

        figurenSpieler2.add(new Bogenschuetze());
        figurenSpieler2.add(new Lanzentraeger());
        figurenSpieler2.add(new Magier());
        figurenSpieler2.add(new Reiter());
        figurenSpieler2.add(new Schwertkaempfer());
    }

    public static Spielbrett getInstance() {
        if (Spielbrett.instance == null) {
            Spielbrett.instance = new Spielbrett();
        }
        return Spielbrett.instance;
    }

    public static Spielbrett getInstance(int xLaenge, int yLaenge, String... parameter) {
        if (Spielbrett.instance == null) {
            Spielbrett.instance = new Spielbrett(xLaenge, yLaenge, parameter);
        }
        return Spielbrett.instance;
    }

    public void reset() {
        Spielbrett.instance = null;
    }

    private void setSpielobjekte(final Spielobjekt[][] spielobjekte) {
        this.spielobjekte = spielobjekte;
        this.setDimensionen();
    }

    private void printBoard() {

        // Geraden (Benennungsschema: Uhrzeigersinn, Start bei Oben)
        final char OBEN_UNTEN = '│';
        final char RECHTS_LINKS = '─';

        // Ecken
        final char UNTEN_LINKS = '┐';

        // Kreuzungen
        final char OBEN_RECHTS_UNTEN_LINKS = '┼';
        final char RECHTS_UNTEN_LINKS = '┬';
        final char OBEN_UNTEN_LINKS = '┤';

        StringBuilder output = new StringBuilder("");

        // TOP LINE
        for (int j = 0; j < xFeldLaenge; j++) {
            output.append(RECHTS_LINKS);
        }

        for (int x = 0; x < this.xLaenge; x++) {
            output.append(RECHTS_UNTEN_LINKS);
            for (int j = 0; j < xFeldLaenge; j++) {
                output.append(RECHTS_LINKS);
            }
        }

        output.append(UNTEN_LINKS);
        output.append(String.format("%n"));

        // ABC-INDEX + SPIELFELDINHALT + TRENNLINIE
        for (int y = 0; y < this.yLaenge; y++) {
            for (int i = 0; i < yFeldLaenge; i++) {
                // ABC- INDEX
                for (int j = 0; j < xFeldLaenge; j++) {

                    if ((i % yFeldLaenge) == (yFeldLaenge / 2) && j % xFeldLaenge == (xFeldLaenge / 2)) {
                        output.append((char) ('A' + this.yLaenge - y - 1));
                    } else {
                        output.append(' ');
                    }

                }
                output.append(OBEN_UNTEN);

                // SPIELFELDINHALT
                for (int x = 0; x < this.xLaenge; x++) {
                    for (int j = 0; j < xFeldLaenge; j++) {
                        output.append(this.generiereSpielobjektSymbolCharArray(this.spielobjekte[y][x], xFeldLaenge,
                                yFeldLaenge)[i][j]);
                    }
                    output.append(OBEN_UNTEN);
                }
                output.append(String.format("%n"));
            }

            for (int j = 0; j < xFeldLaenge; j++) {
                output.append(RECHTS_LINKS);
            }
            output.append(OBEN_RECHTS_UNTEN_LINKS);

            // TRENNLINIE
            for (int x = 0; x < this.xLaenge - 1; x++) {
                for (int i = 0; i < xFeldLaenge; i++) {
                    output.append(RECHTS_LINKS);
                }
                output.append(OBEN_RECHTS_UNTEN_LINKS);

            }

            for (int j = 0; j < xFeldLaenge; j++) {
                output.append(RECHTS_LINKS);
            }
            output.append(OBEN_UNTEN_LINKS);
            output.append(String.format("%n"));
        }

        for (int i = 0; i < yFeldLaenge; i++) {

            for (int j = 0; j < xFeldLaenge; j++) {
                output.append(' ');
            }
            output.append(OBEN_UNTEN);

            for (int x = 0; x < this.xLaenge; x++) {

                int sizeOfIndex;

                // Zentrierte Indexnummern

                for (int j = 0; j < xFeldLaenge; j++) {

                    if ((i % yFeldLaenge) == (yFeldLaenge / 2)) {
                        sizeOfIndex = String.valueOf(x + 1).length();
                        if (j == ((xFeldLaenge - sizeOfIndex) / 2) + ((xFeldLaenge - sizeOfIndex) % 2)) {
                            output.append(x + 1);
                            j += sizeOfIndex - 1;
                        } else {
                            output.append(' ');
                        }

                    } else {
                        output.append(' ');

                    }
                }
                output.append(OBEN_UNTEN);
            }
            output.append(String.format("%n"));
        }
        // Geteiltes Printen aufgrund von Fehlern beim printen von großen
        // Strings.
        System.out.print(output.substring(0, (yLaenge * xLaenge * yFeldLaenge * xFeldLaenge)));
        System.out.println((output.substring((yLaenge * xLaenge * yFeldLaenge * xFeldLaenge), output.length())));
    }

    private char[][] generiereSpielobjektSymbolCharArray(Spielobjekt spielobjekt, int xFeldLaenge, int yFeldLaenge) {

        char[][] spielfeld = new char[yFeldLaenge][xFeldLaenge];

        if (spielobjekt == null) {
            spielobjekt = new Spielobjekt(" ");
        } else {
            if (spielobjekt.getSymbol() == null) {
                spielobjekt = new Spielobjekt(" ");
            }
        }

        int ySpielobjektLaenge = spielobjekt.getSymbol().length;
        int ySpielobjekt = 0;

        for (int y = 0; y < yFeldLaenge; y++) {
            if (y >= ((yFeldLaenge / 2) - (ySpielobjektLaenge / 2))
                    && y <= ((yFeldLaenge / 2) + (ySpielobjektLaenge / 2) - (1 - (ySpielobjektLaenge % 2)))) {

                int xSpielobjektLaenge = 0;

                if (spielobjekt.getSymbol()[ySpielobjekt] != null) {
                    xSpielobjektLaenge = spielobjekt.getSymbol()[ySpielobjekt].length;
                }

                int xSpielobjekt = 0;

                for (int x = 0; x < xFeldLaenge; x++) {

                    if (x >= ((xFeldLaenge / 2) - (xSpielobjektLaenge / 2))
                            && x <= ((xFeldLaenge / 2) + (xSpielobjektLaenge / 2) - (1 - (xSpielobjektLaenge % 2)))) {
                        spielfeld[y][x] = spielobjekt.getSymbol()[ySpielobjekt][xSpielobjekt];
                        xSpielobjekt++;
                    } else {
                        spielfeld[y][x] = ' ';
                    }

                }
                ySpielobjekt++;
            } else {
                for (int x = 0; x < xFeldLaenge; x++) {
                    spielfeld[y][x] = ' ';
                }
            }

        }

        return spielfeld;

    }

    private void setDimensionen() {

        this.yLaenge = this.spielobjekte.length;

        for (final Spielobjekt[] spielobjekteX : this.spielobjekte) {
            if (spielobjekteX.length > this.xLaenge) {
                this.xLaenge = spielobjekteX.length;
            }
        }
    }

    public void bewegen(int[] eingabeInt) {

        int yOffset = 'a' + this.yLaenge - 1;
        int xOffset = -1;
        // Hier müssen Bewegungsabfragen implementiert werden (am Besten nicht
        // imperativ!)
        if (bewegungMoeglichSpielfeld(eingabeInt)
                && bewegungMoeglichBelegt(eingabeInt)
                && bewegungMoeglichRaster(this.spielobjekte[-eingabeInt[0] + yOffset][eingabeInt[1] + xOffset],
                        -eingabeInt[0] + yOffset, eingabeInt[1] + xOffset, -eingabeInt[2] + yOffset,
                        eingabeInt[3] + xOffset)) {
            // Bewegendes Objekt von alt auf neu schieben.
            this.spielobjekte[-eingabeInt[2] + yOffset][eingabeInt[3]
                    + xOffset] = this.spielobjekte[-eingabeInt[0] + yOffset][eingabeInt[1] + xOffset];
            // Bewegtes Objekt von alter Position entfernen.
            this.spielobjekte[-eingabeInt[0] + yOffset][eingabeInt[1] + xOffset] = new Spielobjekt(" ");
        }
        this.updateConsole();
    }

    public void bewegungBefehleLesen(String eingabe) {

        eingabe = eingabe.toLowerCase();
        int[] eingabeInt = new int[4];
        int i = 1;
        for (String s : eingabe.replaceAll("\\s", "").split("[^0-9]+")) {
            if (!s.isEmpty() && i < 4) {
                eingabeInt[i] = Integer.parseInt(s);
                i += 2;
            }
        }
        i = 0;
        for (String s : eingabe.replaceAll("\\s", "").split("[^a-z]+")) {
            if (!s.isEmpty() && i < 4) {
                eingabeInt[i] = s.toCharArray()[0];
                i += 2;
            }
        }

        if (eingabeInt[0] > 0 && eingabeInt[2] > 0) {
            this.bewegen(eingabeInt);
        } else if (eingabeInt[0] > 0 && eingabeInt[2] == 0) {
            this.printBewegen(eingabeInt[0], eingabeInt[1]);
        }
    }

    private void printBewegen(int y, int x) {

        int yOffset = 'a' + this.yLaenge - 1;
        int xOffset = -1;

        char[][] rahmenChar = {
                { '┌', '─', '─', '─', '┐' },
                { '│', ' ', ' ', ' ', '│' },
                { '└', '─', '─', '─', '┘' } };

        Spielobjekt[][] originalSpielbrett = this.copySpielobjekte();
        Spielobjekt rahmen = new Spielobjekt(rahmenChar);

        int[] eingabeInt = { y, x, 0, 0 };

        for (int yZiel = 0; yZiel < this.yLaenge; yZiel++) {
            eingabeInt[2] = -yZiel + yOffset;
            for (int xZiel = 1; xZiel < this.xLaenge + 1; xZiel++) {
                eingabeInt[3] = xZiel;

                if (bewegungMoeglichSpielfeld(eingabeInt)
                        && bewegungMoeglichBelegt(eingabeInt)
                        && bewegungMoeglichRaster(this.spielobjekte[-eingabeInt[0] + yOffset][eingabeInt[1] + xOffset],
                                -eingabeInt[0] + yOffset, eingabeInt[1] + xOffset, -eingabeInt[2] + yOffset,
                                eingabeInt[3] + xOffset)) {
                    System.out.printf("[%d][%d] gesetzt%n", yZiel, xZiel);
                    this.spielobjekte[-eingabeInt[2] + yOffset][eingabeInt[3] + xOffset] = rahmen;
                } else {
                    System.out.printf("[%d][%d] nicht gesetzt%n", yZiel, xZiel);
                }
            }
        }
        fehlermeldung = "";
        this.updateConsole();
        this.spielobjekte = originalSpielbrett;
    }

    private boolean bewegungMoeglichBelegt(int[] eingabeInt) {
        int yOffset = 'a' + this.yLaenge - 1;
        int xOffset = -1;
        boolean moeglich = (this.spielobjekte[-eingabeInt[2] + yOffset][eingabeInt[3] + xOffset] == null
                || this.spielobjekte[-eingabeInt[2] + yOffset][eingabeInt[3] + xOffset].isEmpty());
        if (!moeglich)
            fehlermeldung = "Zug auf dieses Feld nicht moeglich. Bereits belegt.";
        return moeglich;
    }

    private boolean bewegungMoeglichSpielfeld(int[] eingabeInt) {
        int yOffset = 'a' + this.yLaenge - 1;
        int xOffset = -1;
        boolean moeglich = (-eingabeInt[0] + yOffset < this.yLaenge) && ((eingabeInt[1] + xOffset) < this.xLaenge)
                && (-eingabeInt[2] + yOffset < this.yLaenge) && ((eingabeInt[3] + xOffset) < this.xLaenge)
                && (-eingabeInt[0] + yOffset >= 0) && ((eingabeInt[1] + xOffset) >= 0)
                && (-eingabeInt[2] + yOffset >= 0) && ((eingabeInt[3] + xOffset) >= 0)
                && (!(eingabeInt[0] == eingabeInt[2] && eingabeInt[1] == eingabeInt[3]));
        if (!moeglich)
            fehlermeldung = "Zug auf dieses Feld nicht moeglich. Außerhalb des Spielfeldes.";
        return moeglich;
    }

    private boolean bewegungMoeglichRaster(Spielobjekt spielobjekt, int startY, int startX, int zielY, int zielX) {
        if (!Figur.class.isInstance(spielobjekt)) {
            fehlermeldung = "Zug nicht möglich, da ausgewähltes Objekt keine bewegbare Spielfigur ist.";
            return false;
        } else {

            Figur figur = (Figur) (spielobjekt);
            if (figur.getBewegungsRaster().length > 0 && figur.getBewegungsRaster()[0].length > 0) {

                int relativX = zielX - startX;
                int relativY = zielY - startY;

                int bewegungsRasterStartY = (figur).getBewegungsRaster().length / 2;
                int bewegungsRasterStartX = (figur).getBewegungsRaster()[0].length / 2;

                if (bewegungsRasterStartY + relativY >= 0
                        && bewegungsRasterStartY + relativY < figur.getBewegungsRaster().length) {
                    if (bewegungsRasterStartX + relativX >= 0 && bewegungsRasterStartX
                            + relativX < figur.getBewegungsRaster()[bewegungsRasterStartY + relativY].length) {
                        return figur.getBewegungsRaster()[bewegungsRasterStartY + relativY][bewegungsRasterStartX
                                + relativX];
                    }
                }
            }
        }
        fehlermeldung = "Zug auf dieses Feld nicht moeglich. Auserhalb der Reichweite.";
        return false;
    }

    private void updateConsole() {
        if (DEBUG) {
            for (int i = 0; i < 20; i++) {
                System.out.println();
            }
        } else {
            clearConsole();
        }
        this.printBoard();
        this.printFehler(fehlermeldung);
    }

    private void clearConsole() {

        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");

        } catch (IOException | InterruptedException e) {
            System.err.println("Fehler beim Neuzeichnen des Spielfeldes: " + e);
        }

    }

    public String getFehlermeldung() {
        return this.fehlermeldung;
    }

    public void setFehlermeldung(String fehlermeldung) {
        this.fehlermeldung = fehlermeldung;
    }

    private void printFehler(String fehler) {
        if (fehler != null && !fehler.equals(""))
            System.out.println(fehler);
    }

    private void setParameter(String... parameter) {

        if (parameter != null) {
            for (String s : parameter) {
                if (s != null) {
                    switch (s.toUpperCase()) {
                    case "DEBUG":
                        this.DEBUG = true;
                        break;
                    }
                }
            }
        }

    }
}
