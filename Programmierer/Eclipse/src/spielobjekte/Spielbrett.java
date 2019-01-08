package spielobjekte;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import akteure.Spieler;
import main.Spiel;

public class Spielbrett {

	private Spielobjekt[][] spielobjekte;
	private int xLaenge;
	private int yLaenge;

	private final int xFeldLaenge = 7;
	private final int yFeldLaenge = 3;

	public int getXLaenge() {
		return this.xLaenge;
	}

	public int getYLaenge() {
		return this.yLaenge;
	}

	public Spielbrett() {
		this.generiereSpielbrettSpielobjektArray(10, 10);
		this.setHindernisse();
		this.setFiguren(Spiel.getSpieler1(), Spiel.getSpieler2());
	}

	public Spielbrett(final Spielobjekt[][] spielobjekte) {
		this.spielobjekte = spielobjekte;
		this.setDimensionen();
	}

	public Spielobjekt[][] copySpielobjekte() {
		final Spielobjekt[][] copySpielobjekte = new Spielobjekt[this.yLaenge][this.xLaenge];
		for (int y = 0; y < this.yLaenge; y++) {
			for (int x = 0; x < this.xLaenge; x++) {
				copySpielobjekte[y][x] = this.getFeld(new Point(x, y));
			}
		}
		return copySpielobjekte;
	}

	private void generiereSpielbrettSpielobjektArray(final int xLaenge, final int yLaenge) {

		this.spielobjekte = new Spielobjekt[yLaenge][xLaenge];

		this.setDimensionen();

		for (int y = 0; y < yLaenge; y++) {
			for (int x = 0; x < xLaenge; x++) {
				this.setFeld(new Point(x, y), new Spielobjekt(' '));
			}
		}
	}

	private void setFiguren(final Spieler spieler1, final Spieler spieler2) {

		final ArrayList<Figur> gesetzteFiguren = new ArrayList<>();

		final ArrayList<Figur> heldenSpieler1 = spieler1.getHelden();
		final ArrayList<Figur> heldenSpieler2 = spieler2.getHelden();

		final Random randomGenerator = new Random();
		int index;
		final int maxFiguren = heldenSpieler1.size() + heldenSpieler2.size();

		for (int y = 0; y < this.yLaenge; y++) {
			if ((y % 2) == 0) {
				do {
					index = randomGenerator.nextInt(heldenSpieler1.size());
				} while (gesetzteFiguren.contains(heldenSpieler1.get(index)) && (gesetzteFiguren.size() <= maxFiguren));
				this.setFeld(new Point(0, y), heldenSpieler1.get(index));
				gesetzteFiguren.add(heldenSpieler1.get(index));
			} else if ((y % 2) == 1) {
				do {
					index = randomGenerator.nextInt(heldenSpieler2.size());
				} while (gesetzteFiguren.contains(heldenSpieler2.get(index)) && (gesetzteFiguren.size() <= maxFiguren));
				this.setFeld(new Point(9, y), heldenSpieler2.get(index));
				gesetzteFiguren.add(heldenSpieler2.get(index));
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

			if (((this.spielobjekte[y][x] == null) || this.spielobjekte[y][x].isEmpty())
					&& ((this.spielobjekte[y][x + 1] == null) || this.spielobjekte[y][x + 1].isEmpty())) {
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

			if ((this.spielobjekte[y][x] == null) || this.spielobjekte[y][x].isEmpty()) {
				this.spielobjekte[y][x] = new Hindernis();
				this.spielobjekte[y][x].setPosition(new Point(x, y));
				anzahlKleineHindernisse--;
			}
		}
	}

	public static final int BEWEGUNG = 0b0001;
	public static final int ANGREIFBAR = 0b0010;
	public static final int AKTIV = 0b0100;
	public static final int BEWEGUNG_FIGUR = 0b1000;

	/**
	 * Entfernt Markierungen vom übergebenen Spielbrett.
	 *
	 * @param parameters
	 *            byte, das bestimmt, welche Markierungen entfernt werden sollen
	 */
	public void removeSymbolMarks(final int parameters) {

		for (int y = 0; y < this.getYLaenge(); y++) {
			for (int x = 0; x < this.getXLaenge(); x++) {
				if (Figur.class.isInstance(this.getFeld(new Point(x, y)))) {
					if ((Spielbrett.AKTIV & parameters) == Spielbrett.AKTIV) {
						((Figur) (this.getFeld(new Point(x, y)))).symbolRemoveMarkActive();
					}
					if ((Spielbrett.ANGREIFBAR & parameters) == Spielbrett.ANGREIFBAR) {
						((Figur) (this.getFeld(new Point(x, y)))).symbolRemoveMarkCanBeAttacked();
					}
				}
				if ((Spielbrett.BEWEGUNG & parameters) == Spielbrett.BEWEGUNG) {
					if (this.getFeld(new Point(x, y)).getSymbol()[1][3] == '+') {
						this.getFeld(new Point(x, y)).symbolRemoveMarkMovementPossible();
					}
				}
				if ((Spielbrett.BEWEGUNG_FIGUR & parameters) == Spielbrett.BEWEGUNG_FIGUR) {
					this.getFeld(new Point(x, y)).symbolRemoveMarkMovementPossibleFigur();
				}
			}
		}
	}

	/**
	 * Markiert alle Bewegungsmöglichkeiten der Helden des Spielers auf dem
	 * übergebenen Spielobjekt[][]
	 *
	 * @param brettAlt
	 *            ein Spielobjekt[][], auf dem die Bewegungsmöglichkeiten markiert
	 *            werden. spieler ein Spieler, dessen Bewegungsmöglichkeiten
	 *            markiert werden sollen.
	 */
	public void addMovementMarks(final Spieler spieler) {
		for (final Figur f : spieler.getHelden()) {
			if (!f.istBewegt(false)) {
				((Figur) (this.getFeld(f.getPosition()))).symbolAddMarkActive();
				for (int y = 0; y < this.getYLaenge(); y++) {
					for (int x = 0; x < this.getXLaenge(); x++) {
						if (f.bewegungMoeglich(this, new Point(x, y), false, false)
								&& this.getFeld(new Point(x, y)).isEmpty()) {
							this.getFeld(new Point(x, y)).symbolAddMarkMovementPossible();
						}
					}
				}
			}
		}
	}

	public void addMovementMarksFigur(final Figur f) {
		if (!f.istBewegt(false)) {
			((Figur) (this.getFeld(f.getPosition()))).symbolAddMarkActive();
			for (int y = 0; y < this.getYLaenge(); y++) {
				for (int x = 0; x < this.getXLaenge(); x++) {
					if (f.bewegungMoeglich(Spiel.getSpielbrett(), new Point(x, y), false, false)
							&& (this.getFeld(new Point(x, y)).getSymbol()[1][3] == '+')) {
						this.getFeld(new Point(x, y)).symbolAddMarkMovementPossibleFigur();
					}
				}
			}
		}
	}

	public void printBoard() {

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

					if (((i % this.yFeldLaenge) == (this.yFeldLaenge / 2))
							&& ((j % this.xFeldLaenge) == (this.xFeldLaenge / 2))) {
						output.append((char) ('A' + y));
					} else {
						output.append(' ');
					}

				}
				output.append(OBEN_UNTEN);

				// SPIELFELDINHALT
				for (int x = 0; x < this.xLaenge; x++) {
					for (int j = 0; j < this.xFeldLaenge; j++) {
						output.append(this.spielobjekte[y][x].getSymbol()[i][j]);
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
			for (int x = 0; x < (this.xLaenge - 1); x++) {
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
						if (j == (((this.xFeldLaenge - sizeOfIndex) / 2) + ((this.xFeldLaenge - sizeOfIndex) % 2))) {
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

	private void setDimensionen() {

		this.yLaenge = this.spielobjekte.length;

		for (final Spielobjekt[] spielobjekteX : this.spielobjekte) {
			if (spielobjekteX.length > this.xLaenge) {
				this.xLaenge = spielobjekteX.length;
			}
		}
	}

	public boolean isInBounds(final Point p) {
		if ((p.x >= 0) && (p.x < this.xLaenge) && (p.y >= 0) && (p.y < this.yLaenge)) {
			return true;
		} else {
			return false;
		}
	}

	// überprüft, ob das zielfeld bereits belegt ist
	public boolean bewegungMoeglichBelegt(final Point ziel, final boolean setMessage) {

		final boolean moeglich = ((this.getFeld(ziel) == null) || this.getFeld(ziel).isEmpty());
		if (!moeglich && setMessage) {
			Spiel.setNachrichtTemporaerKurz("Zug auf dieses Feld nicht moeglich. Bereits belegt.");
		}
		return moeglich;
	}

	// überprüft, ob das zielfeld innerhalb des spielfelds liegt und nicht
	// gleichzeitig das startfeld ist
	public boolean bewegungMoeglichSpielfeld(final Point start, final Point ziel, final boolean setMessage) {

		final boolean moeglich = (this.isInBounds(start) && this.isInBounds(ziel) && !start.equals(ziel));
		if (!moeglich && setMessage) {
			Spiel.setNachrichtTemporaerKurz("Zug auf dieses Feld nicht moeglich. Außerhalb des Spielfeldes.");
		}
		return moeglich;
	}

	public Spielobjekt getFeld(final Point ziel) {

		if (this.isInBounds(ziel)) {
			return this.spielobjekte[ziel.y][ziel.x];
		} else {
			final Spielobjekt leeresObjekt = new Spielobjekt(' ');
			leeresObjekt.setPosition(new Point(-1, -1));
			return leeresObjekt;
		}
	}

	public void setFeld(final Point ziel, final Spielobjekt spielobjekt) {
		if (this.isInBounds(ziel)) {
			this.spielobjekte[ziel.y][ziel.x] = spielobjekt;
			this.spielobjekte[ziel.y][ziel.x].setPosition(ziel);
		}
	}

	public void bewege(final Point start, final Point ziel) {
		// Bewegendes Objekt auf neue Position setzten.
		this.setFeld(ziel, this.getFeld(start));
		// Bewegtes Objekt von alter Position entfernen.
		this.setFeld(start, new Spielobjekt(' '));
	}

	public void toteEntfernen() {

		for (int y = 0; y < this.yLaenge; y++) {
			for (int x = 0; x < this.xLaenge; x++) {
				if (Figur.class.isInstance(this.getFeld(new Point(x, y)))
						&& ((Figur) (this.getFeld(new Point(x, y)))).istTot()) {
					((Figur) (this.getFeld(new Point(x, y)))).getTeam()
							.entferneHeld((Figur) (this.getFeld(new Point(x, y))));
					final Spielobjekt leeresObjekt = new Spielobjekt(' ');
					this.setFeld(new Point(x, y), leeresObjekt);
				}
			}
		}
	}
	
	public boolean isFigur(Spielobjekt obj) {
		if(obj instanceof Figur) {
			return true;
		} else {
			return false;
		}
	}
}