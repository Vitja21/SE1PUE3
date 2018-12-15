package spielobjekte;

import java.util.ArrayList;

import prototypen.Spiel;

public class Kampf {

    private Figur angreifer;
    private Figur verteidiger;
    private String angriffsart;

    public Kampf(final Figur angreifer, final Figur verteidiger, final String angriffsart) {
        this.setAngreifer(angreifer);
        this.setVerteidiger(verteidiger);
        this.setAngriffsart(angriffsart);
    }

    public Figur getAngreifer() {
        return this.angreifer;
    }

    public void setAngreifer(final Figur angreifer) {
        this.angreifer = angreifer;
    }

    public Figur getVerteidiger() {
        return this.verteidiger;
    }

    public void setVerteidiger(final Figur verteidiger) {
        this.verteidiger = verteidiger;
    }

    public String getAngriffsart() {
        return this.angriffsart;
    }

    public void setAngriffsart(final String angriffsart) {
        this.angriffsart = angriffsart;
    }

    public String printKampf() {

        final String angriffsart = this.getAngriffsart().substring(0, 1).toUpperCase()
                + this.getAngriffsart().substring(1);

        return String.format("%s - %-6s -> %s", this.getAngreifer().printPosition(), angriffsart,
                this.getVerteidiger().printPosition());
    }

    public static String printKampf(final Kampf k1, final Kampf k2) {

        final String k1Angriffsart = k1.getAngriffsart().substring(0, 1).toUpperCase()
                + k1.getAngriffsart().substring(1);

        final String k2Angriffsart = k2.getAngriffsart().substring(0, 1).toUpperCase()
                + k2.getAngriffsart().substring(1);

        return String.format("%s - %-6s -> %s - %-6s", k1.getAngreifer().printPosition(), k1Angriffsart,
                k2.getAngreifer().printPosition(), k2Angriffsart);
    }

    public static void kaempfen(final ArrayList<Kampf> kaempfe) {
        // Gegenseitiges Angreifen
        final ArrayList<Kampf> ausgetrageneKaempfe = new ArrayList<>();

        String ergebnis;
        for (final Kampf k1 : kaempfe) {
            ergebnis = "Ergebnis: ";
            for (final Kampf k2 : kaempfe) {
                if (!k1.equals(k2) && !ausgetrageneKaempfe.contains(k1)) {
                    if (k1.getAngreifer().equals(k2.getVerteidiger())
                            && k1.getVerteidiger().equals(k2.getAngreifer())) {

                        if (!k1.getAngriffsart().equals(k2.getAngriffsart())) {
                            switch (k1.getAngriffsart()) {
                            case "schere":
                                if (k2.getAngriffsart().equals("stein")) {
                                    k1.getAngreifer().setLebenspunkte(k1.getAngreifer().getLebenspunkte() - 1);
                                    k1.getAngreifer().symbolAddCurrentLives();
                                    ergebnis += String.format("%s gewinnt.", k2.getAngreifer().printPosition());
                                } else {
                                    k2.getAngreifer().setLebenspunkte(k2.getAngreifer().getLebenspunkte() - 1);
                                    k2.getAngreifer().symbolAddCurrentLives();
                                    ergebnis += String.format("%s gewinnt.", k1.getAngreifer().printPosition());
                                }
                                break;
                            case "stein":
                                if (k2.getAngriffsart().equals("papier")) {
                                    k1.getAngreifer().setLebenspunkte(k1.getAngreifer().getLebenspunkte() - 1);
                                    k1.getAngreifer().symbolAddCurrentLives();
                                    ergebnis += String.format("%s gewinnt.", k2.getAngreifer().printPosition());
                                } else {
                                    k2.getAngreifer().setLebenspunkte(k2.getAngreifer().getLebenspunkte() - 1);
                                    k2.getAngreifer().symbolAddCurrentLives();
                                    ergebnis += String.format("%s gewinnt.", k1.getAngreifer().printPosition());
                                }
                                break;
                            case "papier":
                                if (k2.getAngriffsart().equals("schere")) {
                                    k1.getAngreifer().setLebenspunkte(k1.getAngreifer().getLebenspunkte() - 1);
                                    k1.getAngreifer().symbolAddCurrentLives();
                                    ergebnis += String.format("%s gewinnt.", k2.getAngreifer().printPosition());
                                } else {
                                    k2.getAngreifer().setLebenspunkte(k2.getAngreifer().getLebenspunkte() - 1);
                                    k2.getAngreifer().symbolAddCurrentLives();
                                    ergebnis += String.format("%s gewinnt.", k1.getAngreifer().printPosition());
                                }
                                break;
                            }
                        } else {
                            ergebnis += "Unentschieden.";
                        }
                        Spiel.setNachrichtTemporaerKurz(
                                Spiel.getNachrichtTemporaerKurz()
                                        + String.format("%s | %s%n", Kampf.printKampf(k1, k2), ergebnis));
                        ausgetrageneKaempfe.add(k1);
                        ausgetrageneKaempfe.add(k2);
                    }
                }
            }
        }
        kaempfe.removeAll(ausgetrageneKaempfe);
        // Eineitiges Angreifen
        for (final Kampf k : kaempfe) {
            k.getVerteidiger().setLebenspunkte(k.getVerteidiger().getLebenspunkte() - 1);
            k.getVerteidiger().symbolAddCurrentLives();
            ergebnis = String.format("Ergebnis: %s gewinnt.", k.getAngreifer().printPosition());
            Spiel.setNachrichtTemporaerKurz(
                    Spiel.getNachrichtTemporaerKurz() + String.format("%s | %s%n", k.printKampf(), ergebnis));
        }
    }
}
