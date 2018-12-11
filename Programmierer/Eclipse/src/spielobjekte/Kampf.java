package spielobjekte;

public class Kampf {

    private Figur angreifer;
    private Figur verteidiger;
    private String angriffsart;

    public Kampf(Figur angreifer, Figur verteidiger, String angriffsart) {
        this.setAngreifer(angreifer);
        this.setVerteidiger(verteidiger);
        this.setAngreifer(angreifer);
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

        String angreiferX = String.valueOf(this.getAngreifer().getPosition().x + 1);
        String angreiferY = String.valueOf((char) (this.getAngreifer().getPosition().y + 'A'));
        String verteidigerX = String.valueOf(this.getVerteidiger().getPosition().x + 1);
        String verteidigerY = String.valueOf((char) (this.getVerteidiger().getPosition().y + 'A'));

        return String.format("%s%s -> %s%s", angreiferY, angreiferX, verteidigerY, verteidigerX);
    }
}
