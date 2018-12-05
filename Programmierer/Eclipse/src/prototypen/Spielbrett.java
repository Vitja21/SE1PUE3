package prototypen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import spielobjekte.Bogenschuetze;
import spielobjekte.Figur;
import spielobjekte.Lanzentraeger;
import spielobjekte.Magier;
import spielobjekte.Reiter;
import spielobjekte.Schwertkaempfer;
import spielobjekte.Spielobjekt;

public class Spielbrett {

    private static Spielbrett instance;

    ArrayList<Figur> figurenSpieler1 = new ArrayList<>();
    ArrayList<Figur> figurenSpieler2 = new ArrayList<>();

    private Spielobjekt[][] spielobjekte;
    private int xLaenge;
    private int yLaenge;

    private Spielbrett(Spielobjekt[][] spielobjekte) {
	this.setSpielobjekte(spielobjekte);
    }

    private Spielbrett() {
	this(10, 10);
    }

    private Spielbrett(int xLaenge, int yLaenge) {
	this.generiereSpielbrettCharArray(xLaenge, yLaenge);
	this.generiereFiguren();
	this.setFiguren();
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

    @SuppressWarnings("unused")
    private void setHindernisse() {

	// TODO;
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

    public static Spielbrett getInstance(int xLaenge, int yLaenge) {
	if (Spielbrett.instance == null) {
	    Spielbrett.instance = new Spielbrett(xLaenge, yLaenge);
	}
	return Spielbrett.instance;
    }

    public void delete() {
	Spielbrett.instance = null;
    }

    private void setSpielobjekte(final Spielobjekt[][] spielobjekte) {
	this.spielobjekte = spielobjekte;
	this.setDimensionen();
    }

    private void printBoard() {

	// TODO: Evtl. Dynamische Bestimmung der feldLaenge abhängig von
	// xLaenge, yLaenge, oder maxSymbolgröße;
	int xFeldLaenge = 5;
	int yFeldLaenge = 3;
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

    public void bewegen(String eingabe) {

	eingabe = eingabe.toLowerCase();
	int[] eingabeChar = new int[4];
	int i = 1;
	for (String s : eingabe.replaceAll("\\s", "").split("[a-z]")) {
	    if (!s.isEmpty()) {
		eingabeChar[i] = Integer.parseInt(s);
		i += 2;
	    }
	}
	i = 0;
	for (String s : eingabe.replaceAll("\\s", "").split("[0-9]")) {
	    if (!s.isEmpty()) {
		eingabeChar[i] = s.toCharArray()[0];
		i += 2;
	    }
	}

	int yOffset = 'a' + this.yLaenge - 1;
	int xOffset = -1;
	// Hier müssen Bewegungsabfragen implementiert werden (am Besten nicht
	// imperativ!)
	if ((-eingabeChar[0] + yOffset < this.yLaenge) && ((eingabeChar[1] + xOffset) < this.xLaenge)
		&& (-eingabeChar[2] + yOffset < this.yLaenge) && ((eingabeChar[3] + xOffset) < this.xLaenge)
		&& (!(eingabeChar[0] == eingabeChar[2] && eingabeChar[1] == eingabeChar[3]))) {
	    // Bewegendes Objekt von alt auf neu schieben.
	    this.spielobjekte[-eingabeChar[2] + yOffset][eingabeChar[3]
		    + xOffset] = this.spielobjekte[-eingabeChar[0] + yOffset][eingabeChar[1] + xOffset];
	    // Bewegtes Objekt von alter Position entfernen.
	    this.spielobjekte[-eingabeChar[0] + yOffset][eingabeChar[1] + xOffset] = new Spielobjekt(" ");
	}
	this.updateConsole();
    }

    private void updateConsole() {
	clearConsole();
//	for (int i = 0; i < 20; i++) {
//	    System.out.println();
//	}
	this.printBoard();
    }

    private void clearConsole() {

	try {
	    if (System.getProperty("os.name").contains("Windows")) {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	    } else
		Runtime.getRuntime().exec("clear");

	} catch (InterruptedException e) {
	    System.err.println(e);
	} catch (IOException e) {
	    System.err.println(e);
	}

    }
}
