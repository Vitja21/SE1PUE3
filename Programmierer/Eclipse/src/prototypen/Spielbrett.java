package prototypen;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

    private final int xFeldLaenge = 7;
    private final int yFeldLaenge = 3;

    public void setFehlermeldung(String fehlermeldung) {
        this.fehlermeldung = fehlermeldung;
    }

    public int getXLaenge() {
        return this.xLaenge;
    }

    public int getYLaenge() {
        return this.yLaenge;
    }

    private Spielbrett(final Spielobjekt[][] spielobjekte) {
        this.setSpielobjekte(spielobjekte);
    }

    private Spielbrett() {
        this(10, 10);
    }

    private Spielobjekt[][] copySpielobjekte() {
        final Spielobjekt[][] copySpielobjekte = new Spielobjekt[this.yLaenge][this.xLaenge];
        for (int y = 0; y < this.yLaenge; y++) {
            for (int x = 0; x < this.xLaenge; x++) {
                copySpielobjekte[y][x] = this.spielobjekte[y][x];
            }
        }
        return copySpielobjekte;
    }

    private Spielbrett(final int xLaenge, final int yLaenge, final String... parameter) {

        this.setParameter(parameter);

        this.generiereSpielbrettCharArray(xLaenge, yLaenge);
        this.generiereFiguren();
        this.setFiguren();
        this.setHindernisse();
        this.updateConsole();
    }

    private void generiereSpielbrettCharArray(final int xLaenge, final int yLaenge) {

        this.spielobjekte = new Spielobjekt[yLaenge][xLaenge];
        this.setDimensionen();
    }

    private void setFiguren() {

        ArrayList<Figur> temp1 = figurenSpieler1;
        ArrayList<Figur> temp2 = figurenSpieler2;
        Collections.shuffle(temp1);
        Collections.shuffle(temp2);
        for (int y = 0; y < this.spielobjekte.length; y++) {
            if (y % 2 == 0) {
                this.spielobjekte[y][0] = temp1.get(0);
                this.spielobjekte[y][0].setPosition(new Point(0, y));
                temp1.remove(0);
                Collections.shuffle(temp1);
            } else if (y % 2 == 1) {
                this.spielobjekte[y][9] = temp2.get(0);
                this.spielobjekte[y][9].setPosition(new Point(9, y));
                temp2.remove(0);
                Collections.shuffle(temp2);
            }
        }
    }

    private void setHindernisse() {

        final Random randomGenerator = new Random();
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
                this.spielobjekte[y][x].setPosition(new Point(x, y));
                this.spielobjekte[y][x + 1] = new Hindernis();
                this.spielobjekte[y][x + 1].setPosition(new Point(x + 1, y));
                anzahlGrosseHindernisse--;
            }
        }

        while (anzahlKleineHindernisse > 0) {
            y = randomGenerator.nextInt(this.yLaenge);
            x = 1 + randomGenerator.nextInt(this.xLaenge - 2);

            if (this.spielobjekte[y][x] == null || this.spielobjekte[y][x].isEmpty()) {
                this.spielobjekte[y][x] = new Hindernis();
                this.spielobjekte[y][x].setPosition(new Point(x, y));
                anzahlKleineHindernisse--;
            }
        }
    }

    private void generiereFiguren() {

        this.figurenSpieler1.add(new Bogenschuetze());
        this.figurenSpieler1.add(new Lanzentraeger());
        this.figurenSpieler1.add(new Magier());
        this.figurenSpieler1.add(new Reiter());
        this.figurenSpieler1.add(new Schwertkaempfer());

        this.figurenSpieler2.add(new Bogenschuetze());
        this.figurenSpieler2.add(new Lanzentraeger());
        this.figurenSpieler2.add(new Magier());
        this.figurenSpieler2.add(new Reiter());
        this.figurenSpieler2.add(new Schwertkaempfer());
    }

    public static Spielbrett getInstance() {
        if (Spielbrett.instance == null) {
            Spielbrett.instance = new Spielbrett();
        }
        return Spielbrett.instance;
    }

    public static Spielbrett getInstance(final int xLaenge, final int yLaenge, final String... parameter) {
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

        final StringBuilder output = new StringBuilder("");

        // TOP LINE
        for (int j = 0; j < this.xFeldLaenge; j++) {
            output.append(RECHTS_LINKS);
        }

        for (int x = 0; x < this.xLaenge; x++) {
            output.append(RECHTS_UNTEN_LINKS);
            for (int j = 0; j < this.xFeldLaenge; j++) {
                output.append(RECHTS_LINKS);
            }
        }

        output.append(UNTEN_LINKS);
        output.append(String.format("%n"));

        // ABC-INDEX + SPIELFELDINHALT + TRENNLINIE
        for (int y = 0; y < this.yLaenge; y++) {
            for (int i = 0; i < this.yFeldLaenge; i++) {
                // ABC- INDEX
                for (int j = 0; j < this.xFeldLaenge; j++) {

                    if ((i % this.yFeldLaenge) == (this.yFeldLaenge / 2)
                            && j % this.xFeldLaenge == (this.xFeldLaenge / 2)) {
                        output.append((char) ('A' + y));
                    } else {
                        output.append(' ');
                    }

                }
                output.append(OBEN_UNTEN);

                // SPIELFELDINHALT
                for (int x = 0; x < this.xLaenge; x++) {
                    for (int j = 0; j < this.xFeldLaenge; j++) {
                        output.append(
                                this.generiereSpielobjektSymbolCharArray(this.spielobjekte[y][x], this.xFeldLaenge,
                                        this.yFeldLaenge)[i][j]);
                    }
                    output.append(OBEN_UNTEN);
                }
                output.append(String.format("%n"));
            }

            for (int j = 0; j < this.xFeldLaenge; j++) {
                output.append(RECHTS_LINKS);
            }
            output.append(OBEN_RECHTS_UNTEN_LINKS);

            // TRENNLINIE
            for (int x = 0; x < this.xLaenge - 1; x++) {
                for (int i = 0; i < this.xFeldLaenge; i++) {
                    output.append(RECHTS_LINKS);
                }
                output.append(OBEN_RECHTS_UNTEN_LINKS);

            }

            for (int j = 0; j < this.xFeldLaenge; j++) {
                output.append(RECHTS_LINKS);
            }
            output.append(OBEN_UNTEN_LINKS);
            output.append(String.format("%n"));
        }

        for (int i = 0; i < this.yFeldLaenge; i++) {

            for (int j = 0; j < this.xFeldLaenge; j++) {
                output.append(' ');
            }
            output.append(OBEN_UNTEN);

            for (int x = 0; x < this.xLaenge; x++) {

                int sizeOfIndex;

                // Zentrierte Indexnummern

                for (int j = 0; j < this.xFeldLaenge; j++) {

                    if ((i % this.yFeldLaenge) == (this.yFeldLaenge / 2)) {
                        sizeOfIndex = String.valueOf(x + 1).length();
                        if (j == ((this.xFeldLaenge - sizeOfIndex) / 2) + ((this.xFeldLaenge - sizeOfIndex) % 2)) {
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
        // Strings. TODO: Bessere Lösung.
        System.out.print(output.substring(0, (this.yLaenge * this.xLaenge * this.yFeldLaenge * this.xFeldLaenge)));
        System.out.println((output.substring((this.yLaenge * this.xLaenge * this.yFeldLaenge * this.xFeldLaenge),
                output.length())));
    }

    private char[][] generiereSpielobjektSymbolCharArray(Spielobjekt spielobjekt, final int xFeldLaenge,
            final int yFeldLaenge) {

        final char[][] spielfeld = new char[yFeldLaenge][xFeldLaenge];

        if (spielobjekt == null) {
            spielobjekt = new Spielobjekt(" ");
        } else {
            if (spielobjekt.getSymbol() == null) {
                spielobjekt = new Spielobjekt(" ");
            }
        }

        final int ySpielobjektLaenge = spielobjekt.getSymbol().length;
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

    public void bewegen(Point start, Point ziel) {

        if (this.getFeld(start).bewegungMoeglich(ziel)) {
            // Bewegendes Objekt von alt auf neu schieben.
            this.setFeld(ziel, this.getFeld(start));
            // Bewegtes Objekt von alter Position entfernen.
            this.setFeld(start, new Spielobjekt(" "));
        }
        this.updateConsole();
    }

    public void bewegungBefehleInterpretieren(String eingabe) {

        eingabe = eingabe.toLowerCase().replaceAll("\\s", "");
        final int[] eingabeInt = { -1, -1, -1, -1 };
        int i = 0;

        for (final String s : eingabe.split("[^0-9]+")) {
            if (!s.isEmpty() &&
                    i < 4) {
                eingabeInt[i] = Integer.parseInt(s) - 1;
                i += 2;
            }
        }
        i = 1;
        for (final String s : eingabe.split("[^a-z]+")) {
            if (!s.isEmpty() && i < 4) {
                eingabeInt[i] = s.toCharArray()[0] - 'a';
                i += 2;
            }
        }

        Point start = new Point(eingabeInt[0], eingabeInt[1]);
        Point ziel = new Point(eingabeInt[2], eingabeInt[3]);

        if (start.y >= 0 && ziel.y >= 0) {
            if (this.getFeld(start).bewegungMoeglich(ziel)) {
                this.bewegen(start, ziel);
            } else {
                this.fehlermeldung = "Zug nicht möglich, da ausgewähltes Objekt keine bewegbare Spielfigur ist.";
            }
        } else {
            this.printBewegen(start);
        }
    }

    private boolean isInBounds(Point p) {
        if (p.x >= 0 && p.x < xLaenge && p.y >= 0 && p.y < yLaenge) {
            return true;
        } else {
            return false;
        }
    }

    private void printBewegen(Point start) {

        final char[][] rahmenChar = {
                { '┌', '─', '─', '─', '┐' },
                { '│', ' ', ' ', ' ', '│' },
                { '└', '─', '─', '─', '┘' } };
        final Spielobjekt rahmen = new Spielobjekt(rahmenChar);

        final Spielobjekt[][] originalSpielbrett = this.copySpielobjekte();

        Point ziel = new Point();

        for (int yZiel = 0; yZiel < this.yLaenge; yZiel++) {

            ziel.y = yZiel;

            for (int xZiel = 0; xZiel < this.xLaenge; xZiel++) {

                ziel.x = xZiel;

                if (this.getFeld(start).bewegungMoeglich(ziel)) {
                    this.setFeld(ziel, rahmen);
                }
            }
        }
        this.fehlermeldung = "";
        this.updateConsole();
        this.spielobjekte = originalSpielbrett;
    }

    public boolean bewegungMoeglichBelegt(Point ziel) {

        final boolean moeglich = (this.getFeld(ziel) == null
                || this.getFeld(ziel).isEmpty());
        if (!moeglich) {
            Spielbrett.getInstance().setFehlermeldung("Zug auf dieses Feld nicht moeglich. Bereits belegt.");
        }
        return moeglich;
    }

    public boolean bewegungMoeglichSpielfeld(Point start, Point ziel) {

        final boolean moeglich = (this.isInBounds(start) && this.isInBounds(ziel) && !start.equals(ziel));
        if (!moeglich) {
            this.fehlermeldung = "Zug auf dieses Feld nicht moeglich. Außerhalb des Spielfeldes.";
        }
        return moeglich;
    }

    private void updateConsole() {
        if (this.DEBUG) {
            for (int i = 0; i < 20; i++) {
                System.out.println();
            }
        } else {
            this.clearConsole();
        }
        this.printBoard();
        this.printFehler(this.fehlermeldung);
    }

    private void clearConsole() {

        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Fehler beim Neuzeichnen des Spielfeldes: " + e);
        }

    }

    public String getFehlermeldung() {
        return Spielbrett.getInstance().fehlermeldung;
    }

    private void printFehler(final String fehler) {
        if (fehler != null && !fehler.equals("")) {
            System.out.println(fehler);
        }
    }

    private void setParameter(final String... parameter) {

        if (parameter != null) {
            for (final String s : parameter) {
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

    private Spielobjekt getFeld(Point ziel) {
        if (this.isInBounds(ziel)) {
            return this.spielobjekte[ziel.y][ziel.x];
        } else {
            Spielobjekt leeresObjekt = new Spielobjekt(" ");
            leeresObjekt.setPosition(new Point(-1, -1));

            return leeresObjekt;
        }
    }

    private void setFeld(Point ziel, Spielobjekt spielobjekt) {
        if (this.isInBounds(ziel)) {
            this.spielobjekte[ziel.y][ziel.x] = spielobjekt;
            this.spielobjekte[ziel.y][ziel.x].setPosition(ziel);
        }
    }
}
