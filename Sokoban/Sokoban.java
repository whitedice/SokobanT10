package Sokoban;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Sokoban
{
    public static void main(String[] args) throws Exception {
        // Args auslesen
        Path fp = Paths.get("sokoban.txt"); // Standard-Pfad bestimmen
        if (args.length != 0)
        {
            fp = Paths.get(args[0]); // Falls Argumente gegeben wurden diese verwenden
        }

        // Spieler und Level erstellen
        Level level = new Level();
        try {
            level.loadLevel(fp); // Level laden
        } catch (Exception e) {
            System.out.println(e.getMessage()); // Fehlermeldung ausgeben
            System.exit(0); // Programm beenden
        }

        if (!level.isValidLevel()) {
            System.out.println("Die Textdatei enthält keine gültige Map!");
            System.exit(0); // Programm beenden
        }
        Player player = new Player(level);

        Runtime game = new Runtime(level, player);
        game.run();
    }
}