package Sokoban;

import java.util.Scanner;

public class Runtime {
    public boolean AUFGEGEBEN = false; // Boolean erstellt der angibt, ob das Spiel aktuell läuft
    private Level level;
    private Player player;

    public Runtime(Level room, Player player) {
        this.level = room;
        this.player = player;
    }

    public void run() {
        Scanner input = new Scanner(System.in); // Scanner für die Eingabe vom Benutzer erstellen
        while (!this.level.isCompleted() && !AUFGEGEBEN) {
            System.out.println(this.level.toString());

            // Eingabe vom Benutzer lesen
            System.out.println("(1)up (2)down (3)left (4)right (5)aufgeben");
            int choice = input.nextInt(); // Nutzerinput abfragen
            try {
                System.out.print("\n\n"); // Abstand zwischen den Tabellen generieren
                switch (choice) {
                    case 1 -> this.player.move(this.level, 0, -1);
                    case 2 -> this.player.move(this.level, 0, +1);
                    case 3 -> this.player.move(this.level, -1, 0);
                    case 4 -> this.player.move(this.level, +1, 0);
                    case 5 -> AUFGEGEBEN = true;
                    default -> System.out.println("Warunung: Ungültige Eingabe"); // Abfragen ob der Nutzer irgendwas anderes als 1-5 eingegeben hat
                }
            } catch (Exception e) {
                System.out.print("\n\n"); // Abstand zwischen den Tabellen generieren
                System.out.println(e.getMessage());
            }
        }

        input.close(); // Input wieder schließen
        if (AUFGEGEBEN) {
            System.out.println("Game over! Sie haben aufgegeben!");
        } else {
            System.out.println("Herzlichen Glückwunsch! Sie haben gewonnen!");
        }

        System.out.println("Spiel beendet...");
    }
}
