package spielobjekte;

public class Kampf {

    private Figur angreifer;
    private Figur verteidiger;
    private String angriffsart;

    public Kampf(Figur angreifer, Figur verteidiger, String angriffsart) {
        this.setAngreifer(angreifer);
        this.setVerteidiger(verteidiger);
        this.setAngriffsart(angriffsart);
    }

    public Figur getAngreifer() {
        return angreifer;
    }

    public void setAngreifer(Figur angreifer) {
        this.angreifer = angreifer;
    }

    public Figur getVerteidiger() {
        return verteidiger;
    }

    public void setVerteidiger(Figur verteidiger) {
        this.verteidiger = verteidiger;
    }

    public String getAngriffsart() {
        return angriffsart;
    }

    public void setAngriffsart(String angriffsart) {
        this.angriffsart = angriffsart;
    }

    public String printKampf() {

        int angreiferX = this.getAngreifer().getPosition().x + 1;
        String angreiferY = String.valueOf((char) (this.getAngreifer().getPosition().y + 'A'));

        String angriffsart = this.getAngriffsart().substring(0, 1).toUpperCase() + this.getAngriffsart().substring(1);

        int verteidigerX = this.getVerteidiger().getPosition().x + 1;
        String verteidigerY = String.valueOf((char) (this.getVerteidiger().getPosition().y + 'A'));

        return String.format("%s%02d - %-6s -> %s%02d", angreiferY, angreiferX, angriffsart, verteidigerY,
                verteidigerX);
    }
}
