package spieler;

import java.util.List;

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

    char[][] getSpielfeld() {

	/*
	 * Symbole:
	 * 
	 * B - Bogenschütze L - Lanzenträger M - Magier R - Reiter S - Schwertkämpfer #
	 * - Hindernis
	 * 
	 */

	return null; // 10x10 char-Array
    }

    List<Figur> gegner() {
	return null;
    }

    String getGegnerClass(String cord) {
	return null;
    }

}
