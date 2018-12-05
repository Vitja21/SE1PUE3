package spielobjekte;

public class Spielobjekt {

    private char[][] symbol;

    public Spielobjekt(char[][] symbol) {
        this.setSymbol(symbol);
    }

    public Spielobjekt(String symbol) {
        this.setSymbol(symbol);
    }

    public char[][] getSymbol() {
        return symbol;
    }

    private void setSymbol(char[][] symbol) {
        this.symbol = symbol;
    }

    private void setSymbol(String symbol) {
        this.symbol = new char[1][];
        this.symbol[0] = symbol.toCharArray();
    }
}
