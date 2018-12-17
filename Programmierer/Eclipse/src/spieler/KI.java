package spieler;

import java.util.ArrayList;

import spielobjekte.Spielobjekt;

public abstract class KI extends Spieler {

    public KI(int nummer) {
	super(nummer);
    }

    private ArrayList<Spielobjekt> berechneFeindBewegung() {
	ArrayList<Spielobjekt> bewegung = new ArrayList<>();
	return bewegung;
    }

    private ArrayList<Spielobjekt> berechneFeindAngriff() {
	ArrayList<Spielobjekt> angriff = new ArrayList<>();
	return angriff;
    }

    private void waehleBewegung() {

    }

    private void waehleAngriff() {

    }

}
