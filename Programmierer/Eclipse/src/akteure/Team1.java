package akteure;

import java.util.List;

import main.Spiel;
import spielobjekte.Figur;

public class Team1 {

    void firstPlayer() {

    }

    void secondPlayer() {

    }

    boolean isItOver() {
        return false;
    }

    String andTheWinnerIs() {
        return null;
    }

    // Bewegung

    boolean canPlayerMove(final String player) {
        return false;
    }

    boolean isValidMove(final String cord1, final String cord2, final String xClass) {
        return false;
    }

    String move(final String cord1, final String cord2) {
        return null;
    }

    String[] getNextMove() {
        return null;
    }

    // Angriff

    boolean canPlayerAttack(final String Player) {
        return false;
    }

    boolean isValidAttack(final String cord1, final String cord2, final String xClass) {
        return false;
    }

    String attack(final String cord1, final String cord2, final String attackType) {
        return null;
    }

    String[] getNextAttack() {
        return null;
    }

    String whoIsTheLooser(final String ko1, final String ko2) {
        return null;
    }

    // nicht notwendig, wenn wir keine wrapper klasse benutzen
    char[][] getSpielfeld() {

        /*
         * Symbole:
         *
         * B - Bogensch�tze L - Lanzentr�ger M - Magier R - Reiter S -
         * Schwertk�mpfer # - Hindernis
         *
         */

        final char[][] spielfeld = new char[10][10];

        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                char symbol;
                switch (Spiel.getSpielbrett().copySpielobjekte()[i][k].getClass().getName()) {
                case "Bogenschuetze":
                    symbol = 'B';
                    break;
                case "Lanzentraeger":
                    symbol = 'L';
                    break;
                case "Magier":
                    symbol = 'M';
                    break;
                case "Reiter":
                    symbol = 'R';
                    break;
                case "Schwertkaempfer":
                    symbol = 'S';
                    break;
                case "Hindernis":
                    symbol = '#';
                    break;
                default:
                    symbol = ' ';
                    break;
                }

                spielfeld[i][k] = symbol;
            }
        }

        return spielfeld; // 10x10 char-Array
    }

    List<Figur> gegner() {
        return null;
    }

    String getGegnerClass(final String cord) {
        return null;
    }

}