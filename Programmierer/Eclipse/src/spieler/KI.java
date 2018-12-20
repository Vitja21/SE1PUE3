package spieler;

import java.util.ArrayList;
import java.util.List;

import spielobjekte.Bogenschuetze;
import spielobjekte.Figur;
import spielobjekte.Hindernis;
import spielobjekte.Lanzentraeger;
import spielobjekte.Magier;
import spielobjekte.Reiter;
import spielobjekte.Schwertkaempfer;
import spielobjekte.Spielobjekt;

public abstract class KI extends Spieler {

    private final Team1 t = new Team1();

    private Spielobjekt[][] spielfeld;

    private final Spieler anderer = null;

    public KI(final int nummer) {
        super(nummer);

//	spielfeld = spielfeldInterpreter();
    }

//    private ArrayList<Spielobjekt> berechneFeindBewegung() {
//	ArrayList<Spielobjekt> bewegung = new ArrayList<>();
//	return bewegung;
//    }
//
//    private ArrayList<Spielobjekt> berechneFeindAngriff() {
//	ArrayList<Spielobjekt> angriff = new ArrayList<>();
//	return angriff;
//    }
//
//    private void waehleBewegung() {
//
//    }
//
//    private void waehleAngriff() {
//
//    }

    public void bewegungsphase() {

        final List<String> bewegungen = new ArrayList<>();

        for (final Figur f : this.getHelden()) {
            bewegungen.add(this.waehleBewegung(f));
        }

        while (this.t.canPlayerMove("Team1")) {

        }
    }

    private Spielobjekt[][] spielfeldInterpreter() {
        final char[][] feld = this.t.getSpielfeld();
        final Spielobjekt[][] spielfeld = new Spielobjekt[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Spielobjekt objekt;
                switch (feld[i][j]) {
                case 'B':
                    objekt = new Bogenschuetze(this.welcherSpieler(i));
                    break;
                case 'L':
                    objekt = new Lanzentraeger(this.welcherSpieler(i));
                    break;
                case 'M':
                    objekt = new Magier(this.welcherSpieler(i));
                    break;
                case 'R':
                    objekt = new Reiter(this.welcherSpieler(i));
                    break;
                case 'S':
                    objekt = new Schwertkaempfer(this.welcherSpieler(i));
                    break;
                case '#':
                    objekt = new Hindernis();
                    break;
                default:
                    objekt = new Spielobjekt(' ');
                    break;
                }

                spielfeld[i][j] = objekt;
            }
        }

        return spielfeld;

    }

    private Spieler welcherSpieler(final int spalte) {
        if (this.nummer == 1) {
            return spalte == 0 ? this : this.anderer;
        } else {
            return spalte == 0 ? this.anderer : this;
        }
    }

    private String waehleBewegung(final Figur held) {
        final Figur zuBewegenderHeld = held;
        final List<String> moeglicheZiele = new ArrayList<>();
        final List<String> moeglicheAngegriffeneFelder = new ArrayList<>();
        final List<Figur> gegner = this.t.gegner();

        final int scores;

        String potentiellesOpferPosition;

        for (final Figur potentiellesOpfer : gegner) {

            // Iterieren über das Spielfeld und mögliche Felder speichern, auf
            // die sich ein
            // Gegner bewegen könnte
            for (int i = 65; i <= 75; i++) {
                for (int j = 1; j <= 10; j++) {

                    // TODO position von potentiellesOpfer
                    potentiellesOpferPosition = "position";

                    final String zielPosition = i + "" + j;
                    final String gegnerClass = this.t.getGegnerClass(potentiellesOpferPosition);

                    if (this.t.isValidMove(potentiellesOpferPosition, zielPosition, gegnerClass)) {

                        moeglicheZiele.add(zielPosition);

                        // Erneutes Iterieren über das Spielfeld und Speichern
                        // der möglichen
                        // Angriffsziele von der aktuellen Position aus
                        for (int k = 65; k <= 75; k++) {

                            for (int l = 1; l <= 10; l++) {

                                final String moeglichesAngriffsziel = k + "" + l;

                                if (this.t.isValidAttack(zielPosition, moeglichesAngriffsziel, gegnerClass)) {
                                    moeglicheAngegriffeneFelder.add(moeglichesAngriffsziel);
                                }
                            }
                        }

                    }
                }
            }
        }

        return null;
    }

}
