package testFuerMenschen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import prototypen.Spielbrett;

public class SpielbrettTest {

    public static void main(String[] args) throws IOException {
        Spielbrett a = Spielbrett.getInstance(10, 10, "DEBUG");

        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);

        // Test Bewegungseingabe
        while (true) {
            System.out.print("Bewegungsbefehl eingeben: ");
            String eingabe = br.readLine();
            if (eingabe.toUpperCase().equals("EXIT")) {
                break;
            }
            a.bewegen(eingabe);

        }
    }
}
