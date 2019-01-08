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

    boolean canPlayerMove(String player) {
	return false;
    }

    boolean isValidMove(String cord1, String cord2, String xClass) {
	return false;
    }

    String move(String cord1, String cord2) {
	return null;
    }

    String[] getNextMove() {
	return null;
    }

    // Angriff

    boolean canPlayerAttack(String Player) {
	return false;
    }

    boolean isValidAttack(String cord1, String cord2, String xClass) {
	return false;
    }

    String attack(String cord1, String cord2, String attackType) {
	return null;
    }

    String[] getNextAttack() {
	return null;
    }

    String whoIsTheLooser(String ko1, String ko2) {
	return null;
    }

    // nicht notwendig, wenn wir keine wrapper klasse benutzen
    char[][] getSpielfeld() {

	/*
	 * Symbole:
	 * 
	 * B - Bogensch�tze L - Lanzentr�ger M - Magier R - Reiter S - Schwertk�mpfer #
	 * - Hindernis
	 * 
	 */

	char[][] spielfeld = new char[10][10];

	for (int i = 0; i < 10; i++) {
	    for (int k = 0; k < 10; k++) {
		char symbol;
		switch (Spiel.getSpielbrett().copySpielobjekte()[i][k].getClass().getName()) {
		case "Bogenschuetze":
		    symbol = 'B';
		    break;
		case "Lanzentr�ger":
		    symbol = 'L';
		    break;
		case "Magier":
		    symbol = 'M';
		    break;
		case "Reiter":
		    symbol = 'R';
		    break;
		case "Schwertk�mpfer":
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

    String getGegnerClass(String cord) {
	return null;
    }

}