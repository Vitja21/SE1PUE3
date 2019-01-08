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

    public HardKI(int nummer) {
	super(nummer);
	// TODO Auto-generated constructor stub
    }

    public void setX(double x) {
	x_predictor = x;
    }

    public double getX() {
	return x_predictor;
    }

    public void setY(double y) {
	y_predictor = y;
    }

    public double getY() {
	return y_predictor;
    }

    public String berechneSpielzug() {
	for (final Figur f : this.getHelden()) {
	    if (!f.istBewegt(false) && !f.istTot()) {
		if (angriffsPositionenKalkulieren(f)) {
		    String a = this.sucheAnderesFeld(f);
		    a_gegner = new ArrayList<>();
		    positionen = new ArrayList<>();
		    if (!a.equals("nichts")) {
			return f.getPosition() + " " + a;
		    }
		} else {
		    for (Figur g : this.gegner) {
			if (f.angriffMoeglich(g.getPosition())) {
			    String ausgabe = this.moeglicherSpielzug(f, g);
			    if (!ausgabe.equals("nichts")) {
				return ausgabe;
			    }
			}
		    }

		    String ausgabe = this.moeglicherSpielzug(f, andereFigur(f));
		    if (!ausgabe.equals("nichts")) {
			return ausgabe;
		    }
		}
	    }
	}

	return "warten";
    }

    public Figur andereFigur(Figur f) {
	ArrayList<Double> next_predictor = new ArrayList<>();
	if (!f.istBewegt(false) && !f.istTot()) {
	    for (final Figur g_f : this.gegner) {
		setX(Math.abs(f.getPosition().getX() - g_f.getPosition().getX()));
		setY(Math.abs(f.getPosition().getY() - g_f.getPosition().getY()));
		next_predictor.add(getX() + getY());
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

    private String moeglicherSpielzug(Figur f, Figur gegn) {
	if (!f.angriffMoeglich(gegn.getPosition())) {
	    String suche = sucheFeld(f, gegn);
	    if (!suche.equals("nichts")) {
		this.letztermove = f.getPosition() + " " + suche;
		return this.letztermove;
	    }
	}
	return "nichts";
    }

    public boolean angriffsPositionenKalkulieren(Figur f) {
	for (Figur gegn : this.gegner) {
	    if (potenziellePosition(gegn, f)) {
		a_gegner.add(gegn);
	    }
	}
	if (!positionen.isEmpty() && !a_gegner.isEmpty()) {
	    for (int i = 0; i < positionen.size(); i++) {
		for (Point point : positionen.get(i)) {
		    for (int j = i + 1; j < positionen.size(); j++) {
			if (positionen.get(j).contains(point)) {
			    return true;
			}
		    }

		}
	    }
	}
	return false;
    }

    private String sucheAnderesFeld(Figur f) {
	ArrayList<Point> potenziellangreifbar = new ArrayList<>();
	Spielobjekt[][] spielfeld = this.getAktuellesSpielbrett().copySpielobjekte();
	for (int i = 0; i < spielfeld.length; i++) {
	    for (int j = 0; j < spielfeld[i].length; j++) {
		boolean wir_angreifbar = false;
		boolean gegner_angreifbar = false;
		boolean bool = false;
		if (f instanceof Reiter) {
		    bool = true;
		}
		if (f.bewegungMoeglich(getAktuellesSpielbrett(), spielfeld[i][j].getPosition(), bool, false)
			&& Spiel.getSpielbrett().getFeld(spielfeld[i][j].getPosition()).isEmpty()) {
		    for (int k = 0; k < positionen.size(); k++) {
			if (test(positionen.get(k), a_gegner.get(k), spielfeld[i][j].getPosition(), f)) {
			    wir_angreifbar = true;
			}
		    }
		    if (!wir_angreifbar) {
			return "" + spielfeld[i][j].getPosition();
		    } else {
			for (int k = 0; k < positionen.size(); k++) {
			    if (test(spielfeld[i][j].getPosition(), f, positionen.get(k), a_gegner.get(k))) {
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
	    for (Figur g : a_gegner) {
		if (f.angriffMoeglich(g.getPosition())) {
		    return sucheFeld(f, g);
		}
	    }
	    Collections.shuffle(a_gegner);
	    return sucheFeld(f, a_gegner.get(0));
	}

    }

    private boolean test(ArrayList<Point> arrayList, Figur gegner, Point unser, Figur f) {
	for (Point p : arrayList) {
	    if (hypothese(gegner, f, p, unser)) {
		return true;
	    }
	}
	return false;
    }

    private boolean test(Point unser, Figur f, ArrayList<Point> arrayList, Figur gegner) {
	for (Point p : arrayList) {
	    if (hypothese(gegner, f, p, unser)) {
		return true;
	    }
	}
	return false;
    }

    private boolean potenziellePosition(Figur gegn, Figur f) {
	ArrayList<Point> position = new ArrayList<>();
	Spielobjekt[][] spielfeld = this.getAktuellesSpielbrett().copySpielobjekte();
	for (int i = 0; i < spielfeld.length; i++) {
	    for (int j = 0; j < spielfeld[i].length; j++) {
		boolean bool = false;
		if (gegn instanceof Reiter) {
		    bool = true;
		}
		if (gegn.bewegungMoeglich(getAktuellesSpielbrett(), spielfeld[i][j].getPosition(), bool, false)
			&& Spiel.getSpielbrett().getFeld(spielfeld[i][j].getPosition()).isEmpty()) {
		    if (hypothese(gegn, f, spielfeld[i][j].getPosition())) {
			position.add(spielfeld[i][j].getPosition());
		    }
		}
	    }
	}
	if (!position.isEmpty())
	    this.positionen.add(position);
	return !position.isEmpty();
    }

    private boolean hypothese(Figur figur1, Figur figur2, Point position) {
	Point p = figur1.getPosition();
	figur1.setPosition(position);
	if (figur1.angriffMoeglich(figur2.getPosition())) {
	    figur1.setPosition(p);
	    return true;
	} else {
	    figur1.setPosition(p);
	    return false;
	}
    }

    private boolean hypothese(Figur figur1, Figur figur2, Point position1, Point position2) {
	Point p1 = figur1.getPosition();
	Point p2 = figur2.getPosition();
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

    private String sucheFeld(Figur figur, Figur gegn) {
	ArrayList<String> move = new ArrayList<>();
	ArrayList<String> move1 = new ArrayList<>();
	ArrayList<String> move2 = new ArrayList<>();
	ArrayList<String> move3 = new ArrayList<>();
	ArrayList<String> move4 = new ArrayList<>();
	boolean bool = false;
	// System.out.println(figur.getName());
	double entfernung = ((Math.abs(figur.getPosition().getX() - gegn.getPosition().getX()))
		+ Math.abs(figur.getPosition().getY() - gegn.getPosition().getY()));
	// System.out.println(entfernung);
	Spielobjekt[][] spielfeld = this.getAktuellesSpielbrett().copySpielobjekte();
	if (figur instanceof Reiter) {
	    bool = true;
	}
	for (int i = 0; i < spielfeld.length; i++) {
	    for (int j = 0; j < spielfeld[i].length; j++) {
		if (figur.bewegungMoeglich(getAktuellesSpielbrett(), spielfeld[i][j].getPosition(), bool, false)
			&& spielfeld[i][j].isEmpty() && !gegn.getPosition()
				.equals(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())) {
		    if (hypothese(figur, gegn, spielfeld[i][j].getPosition())
			    && !gegn.angriffMoeglich(spielfeld[i][j].getPosition())) {
			move.add("" + spielfeld[i][j].getPosition());

			// System.out.println("move");
		    } else if (hypothese(figur, gegn, spielfeld[i][j].getPosition())
			    && gegn.angriffMoeglich(spielfeld[i][j].getPosition())) {
			move1.add("" + spielfeld[i][j].getPosition());

			// System.out.println("move1");
		    } else if (!gegn.angriffMoeglich(spielfeld[i][j].getPosition())) {
			double moeglicheentfernung = (Math
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