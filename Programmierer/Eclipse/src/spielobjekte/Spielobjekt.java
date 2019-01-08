package spielobjekte;

import java.awt.Point;

import main.Spiel;

public class Spielobjekt {

	private Point position;
	protected char[][] symbol = { { ' ', ' ', ' ', ' ', ' ', ' ', ' ', }, { ' ', ' ', ' ', ' ', ' ', ' ', ' ', },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', } };

	public Spielobjekt(final char symbol) {
		this.symbolAddInitial(symbol);
	}

	private void symbolAddInitial(final char symbol) {
		this.symbol[1][3] = symbol;
	}

	public char[][] getSymbol() {
		return this.symbol;
	}

	protected void setSymbol(final char[][] symbol) {
		this.symbol = symbol;
	}

	public boolean isEmpty() {
		if (this.symbol[1][3] == ' ') {
			return true;
		}
		if (this.symbol[1][3] == '+') {
			return true;
		}
		return false;
	}

	public Point getPosition() {
		return this.position;
	}

	public void setPosition(final Point position) {
		this.position = position;
	}

	public boolean bewegungMoeglich(final Spielbrett spielbrett, final Point ziel, final boolean figurenZaehlen,
			final boolean setMessage) {
		if (setMessage) {
			Spiel.setNachrichtTemporaerKurz("Bewegung nicht möglich: Spielobjekt ist keine Figur.");
		}
		return false;
	}

	public boolean angriffMoeglich(final Point ziel) {
		return false;
	}

	public boolean istAngreifbar(final Figur f) {
		return false;
	}

	public void symbolAddMarkMovementPossible() {
		this.symbol[1][3] = '+';
	}

	public void symbolRemoveMarkMovementPossible() {
		this.symbol[1][3] = ' ';
	}

	public void symbolAddMarkMovementPossibleFigur() {
		this.symbol[0][0] = '┌';
		this.symbol[0][6] = '┐';
		this.symbol[2][0] = '└';
		this.symbol[2][6] = '┘';
	}

	public void symbolRemoveMarkMovementPossibleFigur() {
		this.symbol[0][0] = ' ';
		this.symbol[0][6] = ' ';
		this.symbol[2][0] = ' ';
		this.symbol[2][6] = ' ';
	}
}