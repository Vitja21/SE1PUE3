package testFuerMenschen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import prototypen.Spielbrett;

public class SpielbrettTest {

    public static void main(final String[] args) throws IOException {
        final Spielbrett a = Spielbrett.getInstance(10, 10, "DEBUG");

        final InputStreamReader in = new InputStreamReader(System.in);
        final BufferedReader br = new BufferedReader(in);

        // Test Bewegungseingabe
        while (true) {
            System.out.print("Befehl eingeben: ");
            a.setFehlermeldung("");
            final String eingabe = br.readLine();
            if (eingabe.toUpperCase().equals("EXIT")) {
                break;
            }
            a.bewegungBefehleInterpretieren(eingabe);

        }
    }
}