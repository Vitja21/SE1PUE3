package spielobjekte;

import java.awt.Point;

public class Spielobjekt {

    private Point position;
    protected char[][] symbol = { { ' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            { ' ', ' ', ' ', ' ', ' ', ' ', ' ', },
            { ' ', ' ', ' ', ' ', ' ', ' ', ' ', } };

    public Spielobjekt(final char symbol) {
        this.symbolAddInitial(symbol);
    }

    @Override
    public Spielobjekt clone() {
        final Spielobjekt copy = new Spielobjekt(' ');
        copy.setSymbol(this.symbol.clone());
        copy.setPosition(new Point(this.getPosition().x, this.getPosition().y));
        return copy;
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
        return false;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(final Point position) {
        this.position = position;
    }

    public boolean bewegungMoeglich(final Point ziel) {
        return false;
    }

    public boolean angriffMoeglich(final Point ziel) {
        return false;
    }

    public boolean istAngreifbar(final Figur f) {
        return false;
    }
}