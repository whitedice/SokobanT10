package Sokoban;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Level {

    private static final char[] VALID_INPUTS = {'#', ' ', '$', '.', '@', '+', '*'};
    private char[][] room;

    public void loadLevel(Path fp) throws Exception {
        int y = 0;
        int x = 0;
        String line = null;
        try {
            /* -----------------------------------------------------------------------
                    Im ersten Durchlauf, die Größe des Arrays bestimmen
           ----------------------------------------------------------------------- */

            // BufferedReader & die benötigten Variablen erzeugen
            BufferedReader checkLines = Files.newBufferedReader(fp);
            // Ausmessen groß der Array sein muss
            while ((line = checkLines.readLine()) != null) {
                y += 1;
                if (line.length() > x)
                    x = line.length();
                // verhindert, dass eine Zeile die länger ist, als die anderen, nicht mehr ausgelesen werden kann... eigentlich implementiert
                // gewesen, weil ich ursprünglich dachte, wir sollten uns selbst drum kümmern, dass z.B. die Ecken aufgefüllt werden
            }
        } catch (Exception e) {
            throw new Exception("Warnung! Das Spielfeld konnte nicht geladen werden. Bitte überprüfen Sie, ob im ausgewähltem Pfad ein Spielfeld existiert.");
        }

        char[][] feld = new char[x][y]; // Array erstellen
        /* -----------------------------------------------------------------------
                 Im zweiten Durchlauf Daten einfügen
           ----------------------------------------------------------------------- */
        // BufferedReader & die benötigten Variablen erzeugen
        line = null;
        BufferedReader readLines = Files.newBufferedReader(fp);

        // Daten einfügen
        y = 0;
        while ((line = readLines.readLine()) != null) {
            int i = 0;
            while (i < x) {
                char current = '#';
                try {
                    current = line.charAt(i);
                } catch (Exception ignored) {
                }
                if ((new String(VALID_INPUTS).contains(String.valueOf(current)))) // Checken, ob das Zeichen nach der Grammatik unseres Spieles ist
                    feld[i][y] = current; // Falls Grammatik sich nicht beschwert & das Programm weiter läuft, wird die aktuelle Stelle im Array befüllt
                else
                    throw new Exception("Warnung! '" + current + "' ist kein gültiges Zeichen! Bitte korrigieren Sie ihr Spielfeld");
                i += 1;
            }
            y += 1;
        }
        room = feld; // Wenn alles passt, das Feld speichern
    }

    public String toString() {
        String output = "";
        for (int y = 0; y < room[0].length; y++) {
            for (int x = 0; x < room.length; x++) {
                output += room[x][y];
            }
            output += "\n";
        }
        return output;
    }

    public boolean isCompleted() {
        int chestsLeft = 0;
        for (int j = 0; j < room[0].length; ++j) {
            for (int i = 0; i < room.length; ++i) {
                if (room[i][j] == '$') {
                    chestsLeft += 1;
                }
            }
        }
        // Überprüfen, ob das Spiel gewonnen wurde
        return chestsLeft == 0;
    }

    public boolean isValidLevel() {
        int anzSpieler = 0;
        int anzKisten = 0;
        int anzZielPos = 0;

        // jedes Feld durchgehen
        for (int j = 0; j < room[0].length; ++j) {
            for (int i = 0; i < room.length; ++i) {
                switch (String.valueOf(room[i][j])) {
                    case "@":
                        anzSpieler += 1;
                        break;
                    case "+":
                        anzSpieler += 1;
                        anzZielPos += 1;
                        break;
                    case "$":
                        anzKisten += 1;
                        break;
                    case ".":
                        anzZielPos += 1;
                        break;
                    // Eigentlich müsste * anzKisten und anzZielPos je um 1 erhöhen... da uns aber nur die Differenz der beiden Werte interessiert ist das hier irrelevant
                }
            }
        }
        /* ------------------------------------------------
             Spielregeln fürs Spielfeld überprüfen
       ------------------------------------------------ */
        // 1. Es darf max. 1 Spieler / eine Startposition geben | 2. Es darf max. 1 Spieler geben
        return anzSpieler == 1 && anzKisten == anzZielPos;
    }

    public char getCharAtPosition(int x, int y) {
        return room[x][y];
    }

    public void setCharAtPosition(int x, int y, char c) {
        room[x][y] = c;
    }

    public int[] findPlayerPos() {
        // X und Y Position vom Spieler definieren
        int plPosX = 0;
        int plPosY = 0;

        // Spieler finden:
        for (int j=0;j<room[0].length;++j) {
            for (int i=0;i<room.length;++i) {
                if (room[i][j] == '@' || room[i][j] == '+') { // Schauen, wo der Spieler sich gerade befindet
                    plPosX = i;
                    plPosY = j;
                }
            }
        }
        return new int[]{plPosX, plPosY};
    }
}
