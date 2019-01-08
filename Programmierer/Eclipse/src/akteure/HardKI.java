package akteure;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import main.Spiel;
import spielobjekte.Figur;
import spielobjekte.Reiter;
import spielobjekte.Spielobjekt;

public class HardKI extends KI {

    double x_predictor;
    double y_predictor;
    String letztermove;
    ArrayList<ArrayList<Point>> positionen = new ArrayList<>();
    ArrayList<Figur> a_gegner = new ArrayList<>();

    public HardKI(final int nummer) {
        super(nummer);
        // TODO Auto-generated constructor stub
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
        for (final Figur f : this.getHelden()) {
            if (!f.istBewegt(false) && !f.istTot()) {
                if (this.angriffsPositionenKalkulieren(f)) {
                    final String a = this.sucheAnderesFeld(f);
                    this.a_gegner = new ArrayList<>();
                    this.positionen = new ArrayList<>();
                    if (!a.equals("nichts")) {
                        return f.getPosition() + " " + a;
                    }
                } else {
                    for (final Figur g : this.gegner) {
                        if (f.angriffMoeglich(g.getPosition())) {
                            final String ausgabe = this.moeglicherSpielzug(f, g);
                            if (!ausgabe.equals("nichts")) {
                                return ausgabe;
                            }
                        }
                    }

                    final String ausgabe = this.moeglicherSpielzug(f, this.andereFigur(f));
                    if (!ausgabe.equals("nichts")) {
                        return ausgabe;
                    }
                }
            }
        }

        return "warten";
    }

    public Figur andereFigur(final Figur f) {
        final ArrayList<Double> next_predictor = new ArrayList<>();
        if (!f.istBewegt(false) && !f.istTot()) {
            for (final Figur g_f : this.gegner) {
                this.setX(Math.abs(f.getPosition().getX() - g_f.getPosition().getX()));
                this.setY(Math.abs(f.getPosition().getY() - g_f.getPosition().getY()));
                next_predictor.add(this.getX() + this.getY());
            }
        }
        if (next_predictor != null) {
            for (int i = 0; i < next_predictor.size(); i++) {
                if (next_predictor.get(i) == Collections.min(next_predictor)) {
                    return this.gegner.get(i);
                }

            }

        }
        Collections.shuffle(this.gegner);
        return this.gegner.get(0);
    }

    private String moeglicherSpielzug(final Figur f, final Figur gegn) {
        if (!f.angriffMoeglich(gegn.getPosition())) {
            final String suche = this.sucheFeld(f, gegn);
            if (!suche.equals("nichts")) {
                this.letztermove = f.getPosition() + " " + suche;
                return this.letztermove;
            }
        }
        return "nichts";
    }

    public boolean angriffsPositionenKalkulieren(final Figur f) {
        for (final Figur gegn : this.gegner) {
            if (this.potenziellePosition(gegn, f)) {
                this.a_gegner.add(gegn);
            }
        }
        if (!this.positionen.isEmpty() && !this.a_gegner.isEmpty()) {
            for (int i = 0; i < this.positionen.size(); i++) {
                for (final Point point : this.positionen.get(i)) {
                    for (int j = i + 1; j < this.positionen.size(); j++) {
                        if (this.positionen.get(j).contains(point)) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    private String sucheAnderesFeld(final Figur f) {
        final ArrayList<Point> potenziellangreifbar = new ArrayList<>();
        final Spielobjekt[][] spielfeld = this.getAktuellesSpielbrett().copySpielobjekte();
        for (int i = 0; i < spielfeld.length; i++) {
            for (int j = 0; j < spielfeld[i].length; j++) {
                boolean wir_angreifbar = false;
                boolean gegner_angreifbar = false;
                boolean bool = false;
                if (f instanceof Reiter) {
                    bool = true;
                }
                if (f.bewegungMoeglich(this.getAktuellesSpielbrett(), spielfeld[i][j].getPosition(), bool, false)
                        && Spiel.getSpielbrett().getFeld(spielfeld[i][j].getPosition()).isEmpty()) {
                    for (int k = 0; k < this.positionen.size(); k++) {
                        if (this.test(this.positionen.get(k), this.a_gegner.get(k), spielfeld[i][j].getPosition(), f)) {
                            wir_angreifbar = true;
                        }
                    }
                    if (!wir_angreifbar) {
                        return "" + spielfeld[i][j].getPosition();
                    } else {
                        for (int k = 0; k < this.positionen.size(); k++) {
                            if (this.test(spielfeld[i][j].getPosition(), f, this.positionen.get(k),
                                    this.a_gegner.get(k))) {
                                gegner_angreifbar = true;
                            }
                        }
                        if (gegner_angreifbar) {
                            potenziellangreifbar.add(spielfeld[i][j].getPosition());
                        }
                    }
                }
            }
        }
        if (!potenziellangreifbar.isEmpty()) {
            Collections.shuffle(potenziellangreifbar);
            return "" + potenziellangreifbar.get(0);
        } else {
            for (final Figur g : this.a_gegner) {
                if (f.angriffMoeglich(g.getPosition())) {
                    return this.sucheFeld(f, g);
                }
            }
            Collections.shuffle(this.a_gegner);
            return this.sucheFeld(f, this.a_gegner.get(0));
        }

    }

    private boolean test(final ArrayList<Point> arrayList, final Figur gegner, final Point unser, final Figur f) {
        for (final Point p : arrayList) {
            if (this.hypothese(gegner, f, p, unser)) {
                return true;
            }
        }
        return false;
    }

    private boolean test(final Point unser, final Figur f, final ArrayList<Point> arrayList, final Figur gegner) {
        for (final Point p : arrayList) {
            if (this.hypothese(gegner, f, p, unser)) {
                return true;
            }
        }
        return false;
    }

    private boolean potenziellePosition(final Figur gegn, final Figur f) {
        final ArrayList<Point> position = new ArrayList<>();
        final Spielobjekt[][] spielfeld = this.getAktuellesSpielbrett().copySpielobjekte();
        for (int i = 0; i < spielfeld.length; i++) {
            for (int j = 0; j < spielfeld[i].length; j++) {
                boolean bool = false;
                if (gegn instanceof Reiter) {
                    bool = true;
                }
                if (gegn.bewegungMoeglich(this.getAktuellesSpielbrett(), spielfeld[i][j].getPosition(), bool, false)
                        && Spiel.getSpielbrett().getFeld(spielfeld[i][j].getPosition()).isEmpty()) {
                    if (this.hypothese(gegn, f, spielfeld[i][j].getPosition())) {
                        position.add(spielfeld[i][j].getPosition());
                    }
                }
            }
        }
        if (!position.isEmpty()) {
            this.positionen.add(position);
        }
        return !position.isEmpty();
    }

    private boolean hypothese(final Figur figur1, final Figur figur2, final Point position) {
        final Point p = figur1.getPosition();
        figur1.setPosition(position);
        if (figur1.angriffMoeglich(figur2.getPosition())) {
            figur1.setPosition(p);
            return true;
        } else {
            figur1.setPosition(p);
            return false;
        }
    }

    private boolean hypothese(final Figur figur1, final Figur figur2, final Point position1, final Point position2) {
        final Point p1 = figur1.getPosition();
        final Point p2 = figur2.getPosition();
        figur1.setPosition(position1);
        figur2.setPosition(position2);
        if (figur1.angriffMoeglich(figur2.getPosition())) {
            figur1.setPosition(p1);
            figur2.setPosition(p2);
            return true;
        } else {
            figur1.setPosition(p1);
            figur2.setPosition(p2);
            return false;
        }
    }

    private String sucheFeld(final Figur figur, final Figur gegn) {
        final ArrayList<String> move = new ArrayList<>();
        final ArrayList<String> move1 = new ArrayList<>();
        final ArrayList<String> move2 = new ArrayList<>();
        final ArrayList<String> move3 = new ArrayList<>();
        final ArrayList<String> move4 = new ArrayList<>();
        boolean bool = false;
        // System.out.println(figur.getName());
        final double entfernung = ((Math.abs(figur.getPosition().getX() - gegn.getPosition().getX()))
                + Math.abs(figur.getPosition().getY() - gegn.getPosition().getY()));
        // System.out.println(entfernung);
        final Spielobjekt[][] spielfeld = this.getAktuellesSpielbrett().copySpielobjekte();
        if (figur instanceof Reiter) {
            bool = true;
        }
        for (int i = 0; i < spielfeld.length; i++) {
            for (int j = 0; j < spielfeld[i].length; j++) {
                if (figur.bewegungMoeglich(this.getAktuellesSpielbrett(), spielfeld[i][j].getPosition(), bool, false)
                        && spielfeld[i][j].isEmpty()
                        && Spiel.getSpielbrett().getFeld(spielfeld[i][j].getPosition()).isEmpty()) {
                    if (this.hypothese(figur, gegn, spielfeld[i][j].getPosition())
                            && !gegn.angriffMoeglich(spielfeld[i][j].getPosition())) {
                        move.add("" + spielfeld[i][j].getPosition());

                        // System.out.println("move");
                    } else if (this.hypothese(figur, gegn, spielfeld[i][j].getPosition())
                            && gegn.angriffMoeglich(spielfeld[i][j].getPosition())) {
                        move1.add("" + spielfeld[i][j].getPosition());

                        // System.out.println("move1");
                    } else if (!gegn.angriffMoeglich(spielfeld[i][j].getPosition())) {
                        final double moeglicheentfernung = (Math
                                .abs(spielfeld[i][j].getPosition().getX() - gegn.getPosition().getX())
                                + Math.abs(spielfeld[i][j].getPosition().getY() - gegn.getPosition().getY()));
                        if (entfernung > moeglicheentfernung) {
                            move2.add("" + spielfeld[i][j].getPosition());
                            // System.out.println(moeglicheentfernung);
                        } else if (entfernung == moeglicheentfernung) {
                            move3.add("" + spielfeld[i][j].getPosition());
                        }
                        // System.out.println("move3");
                    } else if (gegn.angriffMoeglich(spielfeld[i][j].getPosition())) {
                        move4.add("" + spielfeld[i][j].getPosition());
                        // System.out.println("move4");
                    }
                }

            }
        }
        if (move.size() > 0) {
            // System.out.println("actual");
            Collections.shuffle(move);
            return move.get(0);
        } else if (move1.size() > 0) {
            // System.out.println("actual1");
            Collections.shuffle(move1);
            return move1.get(0);
        } else if (move2.size() > 0) {
            // System.out.println("actual2");
            Collections.shuffle(move2);
            return move2.get(0);
        } else if (move3.size() > 0) {
            // System.out.println("actual3");
            Collections.shuffle(move3);
            return move3.get(0);
        } else if (move4.size() > 0) {
            // System.out.println("actual4");
            Collections.shuffle(move4);
            return move4.get(0);
        } else {
            // System.out.println("null");
            return "nichts";
        }
    }
}