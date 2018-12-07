package spielobjekte;

import java.awt.Point;

public class Spielobjekt {

    private Point position;
    private char[][] symbol;

    public Spielobjekt(final char[][] symbol) {
        this.setSymbol(symbol);
        this.setPosition(position);
    }

    public Spielobjekt(final String symbol) {
        this.setSymbol(symbol);
        this.setPosition(position);
    }

    public char[][] getSymbol() {
        return this.symbol;
    }

    private void setSymbol(final char[][] symbol) {
        this.symbol = symbol;
    }

    private void setSymbol(final String symbol) {
        this.symbol = new char[1][];
        this.symbol[0] = symbol.toCharArray();
    }

    public boolean isEmpty() {
        if (this.symbol.length == 1 && this.symbol[0].length == 1 && this.symbol[0][0] == ' ') {
            return true;
        }
        return false;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public boolean bewegungMoeglich(Point ziel) {
        return false;
    }
}
