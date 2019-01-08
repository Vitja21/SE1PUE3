package akteure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import spielobjekte.Figur;
import spielobjekte.Spielbrett;
import spielobjekte.Spielobjekt;

public class KI extends Spieler {

	Spielbrett aktuelles_brett;

	ArrayList<Figur> gegner = new ArrayList<>();

	public KI(final int nummer) {
		super(nummer);

	}

	public String bewegungsphase(Spielbrett brettAlt) {
		setAktuellesSpielbrett(brettAlt);
		setGegner();
		String ergebnis = "";
		if (this instanceof EasyKI) {
			ergebnis = umstellenDerDaten(((EasyKI) this).berechneSpielzug());
		} else if (this instanceof MediumKI) {
			ergebnis = umstellenDerDaten(((MediumKI) this).berechneSpielzug());
		} else {
			ergebnis = umstellenDerDaten(((HardKI) this).berechneSpielzug());
		}
		this.gegner = new ArrayList<>();
		return ergebnis;
	}

	public void setAktuellesSpielbrett(Spielbrett brett) {
		aktuelles_brett = brett;
	}

	public Spielbrett getAktuellesSpielbrett() {
		return aktuelles_brett;
	}

	public void setGegner() {
		final Spielobjekt[][] copySpielobjekte = this.getAktuellesSpielbrett().copySpielobjekte();
		for (int i = 0; i < copySpielobjekte.length; i++) {
			for (int j = 0; j < copySpielobjekte[i].length; j++) {
				if (this.getAktuellesSpielbrett().isFigur(copySpielobjekte[i][j])) {
					if (((Figur) copySpielobjekte[i][j]).getTeam().nummer == 1) {
						gegner.add((Figur) copySpielobjekte[i][j]);
					}
				}

			}
		}
	}

	private String umstellenDerDaten(String string) {
		if (string.equals("warten")) {
			return "warten";
		}
		string = string.toLowerCase().replaceAll("[\\W_]+", "");
		string = string.toLowerCase().replaceAll("[^0-9]+", "");
		final String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j" };
		final int[] zahlen = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String eingabeKoordinaten = "";
		for (int i = 0; i < string.length(); i++) {
			if (i % 2 == 0) {
				String a = "" + string.charAt(i + 1);
				int x = Integer.parseInt(a);
				for (int j = 0; j < zahlen.length; j++) {
					if (x == zahlen[j]) {
						eingabeKoordinaten += alphabet[j];
					}
				}
			} else {
				String a = "" + string.charAt(i - 1);
				int x = Integer.parseInt(a);
				for (int j = 0; j < zahlen.length; j++) {
					if (x == zahlen[j]) {
						int zahl = (1 + zahlen[j]);
						eingabeKoordinaten += zahl;
					}
				}
			}
		}
		return eingabeKoordinaten;
	}

	public String random() {
		setGegner();
		Collections.shuffle(this.gegner);
		for (Figur f : this.getHelden()) {
			for (Figur g_f : this.gegner) {
				if (f.angriffMoeglich(g_f.getPosition())) {
					final Random randomGenerator = new Random();
					switch (randomGenerator.nextInt(3)) {
					case 0:
						String ausgabeSchere = umstellenDerDaten(f.getPosition() + " " + g_f.getPosition()) + " schere";
						this.gegner = new ArrayList<>();
						return ausgabeSchere;
					case 1:
						String ausgabeStein = umstellenDerDaten(f.getPosition() + " " + g_f.getPosition()) + " stein";
						this.gegner = new ArrayList<>();
						return ausgabeStein;
					case 2:
						String ausgabePapier = umstellenDerDaten(f.getPosition() + " " + g_f.getPosition()) + " papier";
						this.gegner = new ArrayList<>();
						return ausgabePapier;
					default:
						return "Fehler";
					}
				}

			}
		}
		this.gegner = new ArrayList<>();
		return "Fehler";
	}
}
//
// public void bewegungsphase() {
//
// final List<String> bewegungen = new ArrayList<>();
//
// for (final Figur f : this.getHelden()) {
// bewegungen.add(this.waehleBewegung(f));
// }
//
// while (this.t.canPlayerMove("Team1")) {
//
// }
// }
//
// // nur n�tig, wenn wir wrapper klasse benutzen
// private Spielobjekt[][] spielfeldInterpreter() {
// final char[][] feld = this.t.getSpielfeld();
// final Spielobjekt[][] spielfeld = new Spielobjekt[10][10];
//
// for (int i = 0; i < 10; i++) {
// for (int j = 0; j < 10; j++) {
// Spielobjekt objekt;
// switch (feld[i][j]) {
// case 'B':
// objekt = new Bogenschuetze(this.welcherSpieler(i));
// break;
// case 'L':
// objekt = new Lanzentraeger(this.welcherSpieler(i));
// break;
// case 'M':
// objekt = new Magier(this.welcherSpieler(i));
// break;
// case 'R':
// objekt = new Reiter(this.welcherSpieler(i));
// break;
// case 'S':
// objekt = new Schwertkaempfer(this.welcherSpieler(i));
// break;
// case '#':
// objekt = new Hindernis();
// break;
// default:
// objekt = new Spielobjekt(' ');
// break;
// }
//
// spielfeld[i][j] = objekt;
// }
// }
//
// return spielfeld;
//
// }
//
// private Spieler welcherSpieler(final int spalte) {
// if (this.nummer == 1) {
// return spalte == 0 ? this : this.anderer;
// } else {
// return spalte == 0 ? this.anderer : this;
// }
// }
//
// private String waehleBewegung(final Figur held) {
// final Figur zuBewegenderHeld = held;
// final List<String> moeglicheZiele = new ArrayList<>();
// final List<String> moeglicheAngegriffeneFelder = new ArrayList<>();
// final List<Figur> gegner = this.t.gegner();
//
// final int scores;
//
// String potentiellesOpferPosition;
//
// for (final Figur potentiellesOpfer : gegner) {
//
// // Iterieren �ber das Spielfeld und m�gliche Felder speichern, auf
// // die sich ein
// // Gegner bewegen k�nnte
// for (int i = 65; i <= 75; i++) {
// for (int j = 1; j <= 10; j++) {
//
// // TODO position von potentiellesOpfer
// potentiellesOpferPosition = "position";
//
// final String zielPosition = i + "" + j;
// final String gegnerClass = this.t.getGegnerClass(potentiellesOpferPosition);
//
// if (this.t.isValidMove(potentiellesOpferPosition, zielPosition, gegnerClass))
// {
//
// moeglicheZiele.add(zielPosition);
//
// // Erneutes Iterieren �ber das Spielfeld und Speichern
// // der m�glichen
// // Angriffsziele von der aktuellen Position aus
// for (int k = 65; k <= 75; k++) {
//
// for (int l = 1; l <= 10; l++) {
//
// final String moeglichesAngriffsziel = k + "" + l;
//
// if (this.t.isValidAttack(zielPosition, moeglichesAngriffsziel, gegnerClass))
// {
// moeglicheAngegriffeneFelder.add(moeglichesAngriffsziel);
// }
// }
// }
//
// }
// }
// }
// }
//
// return null;
// }