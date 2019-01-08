package akteure;

import java.util.ArrayList;
import java.util.Collections;

import main.Spiel;
import spielobjekte.Figur;
import spielobjekte.Reiter;

public class EasyKI extends KI {

    double x_predictor;
    double y_predictor;
    String letztermove;

    public EasyKI(final int nummer) {
        super(nummer);
    }

    public void setX(final double x) {
        this.x_predictor = x;
    }

    public double getX() {
        return this.x_predictor;
    }

    public void setY(final double y) {
        this.y_predictor = y;
    }

    public double getY() {
        return this.y_predictor;
    }

    public String berechneSpielzug() {
        ArrayList<Double> next_predictor = new ArrayList<>();
        for (final Figur f : this.getHelden()) {
            if (!f.istBewegt(false) && !f.istTot()) {
                for (final Figur g_f : this.gegner) {
                    next_predictor.add(Math.abs(f.getPosition().getX() - g_f.getPosition().getX())
                            + Math.abs(f.getPosition().getY() - g_f.getPosition().getY()));
                }
            }
            if (next_predictor != null) {
                for (int i = 0; i < next_predictor.size(); i++) {
                    if (next_predictor.get(i) == Collections.min(next_predictor)) {
                        final String ausgabe = this.moeglicherSpielzug(this.gegner.get(i));
                        if (!ausgabe.equals("nichts")) {
                            return ausgabe;
                        } else {
                            f.setIstBewegt(true);
                            break;
                        }
                    }
                }
            }
            next_predictor = new ArrayList<>();
        }

        return "warten";
    }

    public String moeglicherSpielzug(final Figur gegn) {
        for (final Figur f : this.getHelden()) {
            if (!f.istBewegt(false) && !f.istTot()) {
                if (f.angriffMoeglich(gegn.getPosition())) {
                    return "nichts";
                } else {
                    final String suche = this.sucheFeld(f, gegn);
                    if (!suche.equals("nichts")) {
                        this.letztermove = f.getPosition() + " " + suche;
                        return this.letztermove;
                    } else {
                        return suche;
                    }
                }
            }
        }
        return "nichts";
    }

    private String sucheFeld(final Figur figur, final Figur gegn) {
        final ArrayList<String> move = new ArrayList<>();
        final ArrayList<String> move1 = new ArrayList<>();
        final ArrayList<String> move2 = new ArrayList<>();
        final ArrayList<String> move3 = new ArrayList<>();
        boolean bool = false;
        if (figur instanceof Reiter) {
            bool = true;
        }
        for (int i = 0; i < this.getAktuellesSpielbrett().getXLaenge(); i++) {
            for (int j = 0; j < this.getAktuellesSpielbrett().getYLaenge(); j++) {
                if (figur.bewegungMoeglich(this.getAktuellesSpielbrett(),
                        this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition(), bool, false)
                        && Spiel.getSpielbrett()
                                .getFeld(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())
                                .isEmpty()) {
                    if (!gegn.angriffMoeglich(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())) {
                        if (figur.angriffMoeglich(gegn.getPosition())) {
                            move.add("" + this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition());
                        } else if (!figur.angriffMoeglich(gegn.getPosition())) {
                            move2.add("" + this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition());
                        }
                    } else {
                        if (gegn.angriffMoeglich(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())
                                && figur.angriffMoeglich(gegn.getPosition())) {
                            move1.add("" + this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition());
                        } else if (gegn
                                .angriffMoeglich(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())
                                && !figur.angriffMoeglich(gegn.getPosition())) {
                            move3.add("" + this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition());
                        }
                    }
                }
            }
        }
        if (move.size() > 0) {
            Collections.shuffle(move);
            return move.get(0);
        } else if (move1.size() > 0) {
            Collections.shuffle(move1);
            return move1.get(0);
        } else if (move2.size() > 0) {
            Collections.shuffle(move2);
            return move2.get(0);
        } else if (move3.size() > 0) {
            Collections.shuffle(move3);
            return move3.get(0);
        } else {
            return "nichts";
        }
    }
}
