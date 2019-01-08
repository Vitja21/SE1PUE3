package akteure;

import java.util.ArrayList;
import java.util.Collections;

import spielobjekte.Figur;
import spielobjekte.Reiter;

public class EasyKI extends KI {

    double x_predictor;
    double y_predictor;
    String letztermove;

    public EasyKI(int nummer) {
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
		    next_predictor.add(Math.abs(f.getPosition().getX() - g_f.getPosition().getX())
			    + Math.abs(f.getPosition().getY() - g_f.getPosition().getY()));
		}
	    }
	    if (next_predictor != null)
		for (int i = 0; i < next_predictor.size(); i++) {
		    if (next_predictor.get(i) == Collections.min(next_predictor)) {
			return moeglicherSpielzug(this.gegner.get(i));
		    }
		}
	    next_predictor = new ArrayList<>();
	}

	return "warten";
    }

    public String moeglicherSpielzug(Figur gegn) {
	for (Figur f : this.getHelden()) {
	    if (!f.istBewegt(false) && !f.istTot()) {
		if (f.angriffMoeglich(gegn.getPosition())) {
		    return "nichts";
		} else {
		    String suche = sucheFeld(f, gegn);
		    if (!suche.equals("nichts")) {
			this.letztermove = f.getPosition() + " " + suche;
			return this.letztermove;
		    } else {
			return suche;
		    }
		}
	    }
	}
	return "warten";
    }

    private String sucheFeld(Figur figur, Figur gegn) {
	ArrayList<String> move = new ArrayList<>();
	ArrayList<String> move1 = new ArrayList<>();
	ArrayList<String> move2 = new ArrayList<>();
	ArrayList<String> move3 = new ArrayList<>();
	boolean bool = false;
	if (figur instanceof Reiter) {
	    bool = true;
	}
	for (int i = 0; i < this.getAktuellesSpielbrett().getXLaenge(); i++) {
	    for (int j = 0; j < this.getAktuellesSpielbrett().getYLaenge(); j++) {
		if (figur.bewegungMoeglich(getAktuellesSpielbrett(),
			this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition(), bool, false)
			&& this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty() && !gegn.getPosition()
				.equals(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())) {
		    if (!gegn.angriffMoeglich(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())
			    && this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty()) {
			if (figur.angriffMoeglich(gegn.getPosition())
				&& this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty()) {
			    move.add("" + this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition());
			} else if (!figur.angriffMoeglich(gegn.getPosition())
				&& this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty()) {
			    move2.add("" + this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition());
			}
		    } else {
			if (gegn.angriffMoeglich(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())
				&& figur.angriffMoeglich(gegn.getPosition())
				&& this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty()) {
			    move1.add("" + this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition());
			} else if (gegn
				.angriffMoeglich(this.getAktuellesSpielbrett().copySpielobjekte()[i][j].getPosition())
				&& !figur.angriffMoeglich(gegn.getPosition())
				&& this.getAktuellesSpielbrett().copySpielobjekte()[i][j].isEmpty()) {
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
