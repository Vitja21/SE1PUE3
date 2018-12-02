package spielobjekte;

public abstract class Spielobjekt {

    private String symbol;

    public Spielobjekt(String symbol) {
	this.symbol = symbol;
    }

    public String getSymbol() {
	return symbol;
    }

    @SuppressWarnings("unused")
    private void setSymbol(String symbol) {
	this.symbol = symbol;
    }

    @Override
    public String toString() {
	return symbol;
    }
}
