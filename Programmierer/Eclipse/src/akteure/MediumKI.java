package akteure;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import main.Spiel;
import spielobjekte.Figur;
import spielobjekte.Reiter;
import spielobjekte.Spielobjekt;

public class MediumKI extends KI {

    double x_predictor;
    double y_predictor;
    String letztermove;

    public MediumKI(int nummer) {
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
	ArrayList<Double> next_predictor = new ArrayList<>();
	for (final Figur f : this.getHelden()) {
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
			String ausgabe = moeglicherSpielzug(f, this.gegner.get(i));
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

    public String moeglicherSpielzug(Figur f, Figur gegn) {
	if (!f.angriffMoeglich(gegn.getPosition())) {
	    String suche = sucheFeld(f, gegn);
	    if (!suche.equals("nichts")) {
		this.letztermove = f.getPosition() + " " + suche;
		return this.letztermove;
	    }
	}
	return "nichts";
    }

    private boolean hypothese(Figur f, Figur gegn, Point position) {
	Point p = f.getPosition();
	f.setPosition(position);
	if (f.angriffMoeglich(gegn.getPosition())) {
	    f.setPosition(p);
	    return true;
	} else {
	    f.setPosition(p);
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
			&& Spiel.getSpielbrett().getFeld(spielfeld[i][j].getPosition()).isEmpty() && !gegn.getPosition()
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
// private String suchenFeld(Figur figur, Figur gegner) {
// ArrayList<Integer> moves = new ArrayList<>();
// Spielobjekt[][] spielobjekte =
// this.getAktuellesSpielbrett().copySpielobjekte();
// boolean bool = false;
// if(figur instanceof Reiter) {
// bool = true;
// }
// for (int i = 0; i < spielobjekte.length; i++) {
// for (int j = 0; j < spielobjekte[i].length; j++) {
// if
// (figur.bewegungMoeglich(this.getAktuellesSpielbrett(),spielobjekte[i][j].getPosition(),bool,
// false)
// && this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty()) {
// if() {
//
// }
//
//
//
//
//
//
// if
// (!gegn.angriffMoeglich(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())
// && this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty()) {
// if (figur.angriffMoeglich(gegn.getPosition())
// && this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty()) {
// move.add("" +
// this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition());
// } else if (!figur.angriffMoeglich(gegn.getPosition())
// && this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty()) {
// move2.add("" +
// this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition());
// }
// } else {
// if
// (gegn.angriffMoeglich(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())
// && figur.angriffMoeglich(gegn.getPosition())
// && this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty()) {
// move1.add("" +
// this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition());
// } else if (gegn
// .angriffMoeglich(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())
// && !figur.angriffMoeglich(gegn.getPosition())
// && this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty()) {
// move3.add("" +
// this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition());
// }
// }
// }
// }
// }
// if (move.size() > 1) {
// Collections.shuffle(move);
// return move.get(0);
// } else if (move1.size() > 1) {
// Collections.shuffle(move1);
// return move1.get(0);
// } else if (move2.size() > 1) {
// Collections.shuffle(move2);
// return move2.get(0);
// } else {
// Collections.shuffle(move3);
// return move3.get(0);
// }
// }