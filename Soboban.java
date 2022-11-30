import java.util.Scanner;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Sokoban
{
    public static boolean play = true; // Boolean erstellt der angibt, ob das Spiel aktuell läuft

    public static char[][] ReadTXT(Path fp) throws Exception {
    // Im ersten Durchlauf, die Größe des Arrays bestimmen

        // BufferedReader & die benötigten Variablen erzeugen
        BufferedReader checkLines = Files.newBufferedReader(fp);
        String line = null;
        int y = 0;
        int x = 0;

        // Ausmessen groß der Array sein muss
        while ((line = checkLines.readLine()) != null) {
            y += 1;
            if (line.length() > x)
                x = line.length();
        }

        char[][] feld = new char[x][y]; // Array erstellen

    // Im zweiten Durchlauf Daten einfügen und fehlende Ecken etc. auffüllen

        // BufferedReader & die benötigten Variablen erzeugen
        line = null;
        BufferedReader readLines = Files.newBufferedReader(fp);

        // Daten einfügen
        y = 0;
        int anzSpieler = 0;
        int anzKisten = 0;
        int anzZielPos = 0;

        while ((line = readLines.readLine()) != null) {
            int i = 0;
            while (i < x) {
                char current = line.charAt(i);
                // Grammatik unseres Spieles abfragen & die wichtigen Werte Zählen
                switch (String.valueOf(current)) {
                    case "#", " ", "*": // Eigentlich müsste * anzKisten und anzZielPos je um 1 erhöhen...
                        // da uns aber nur die Differenz der beiden Werte interessiert ist das hier irrelevant
                        break;
                    case "@":
                        anzSpieler += 1;
                        break;
                    case "+":
                        anzSpieler += 1;
                        anzZielPos += 1;
                        break;
                    case "$":
                        anzKisten +=1;
                        break;
                    case ".":
                        anzZielPos +=1;
                        break;
                    default:
                        // Abfragen, ob ungültige Zeichen im Spielfeld vorkommen
                        System.out.println("Warnung! '" + current + "' ist kein gültiges Zeichen! Bitte korrigieren Sie ihr Spielfeld");
                        play = false;
                        break;
                }
                feld[i][y] = current; // Falls Grammatik sich nicht beschwert & das Programm weiter läuft, wird die aktuelle Stelle im Array befüllt
                i+=1;
            }
            y += 1;
        }

    // Spielregeln fürs Spielfeld überprüfen
        // 1. Es darf max. 1 Spieler / eine Startposition geben
        if (anzSpieler != 1) {
            System.out.println("Warnung: Es muss immer nur exakt einen Spieler auf dem Spielfeld vorhanden sein!");
            play = false;
        }
        // 2. Es darf max. 1 Spieler geben
        if (anzKisten != anzZielPos) {
            System.out.println("Warnung: Es müssen exakt so viele Kisten wie Zielpositionen existieren!");
            play = false;
        }

        return feld; // Wenn alles passt, das Feld zurückgeben
    }

    public static void main(String[] args) throws Exception {
        // Args auslesen
        Path fp = Paths.get("sokoban.txt"); // Pfad bestimmen
        if (args.length != 0)
        {
            fp = Paths.get(args[0]); // Falls Argumente gegeben wurden diese verwenden
        }

        Scanner input = new Scanner(System.in); // Scanner für die Eingabe vom Benutzer erstellen

        // Anfangsspielbrett definieren
        char [][] room = ReadTXT(fp);

        while (play) {
            printRoom(room); // Den aktuellen Raum ausgeben
            System.out.println("(1)up (2)down (3)left (4)right (5)exit");
            int choice = input.nextInt(); // Nutzerinput abfragen
            switch (choice) {
                case 1 -> up(room);
                case 2 -> down(room);
                case 3 -> left(room);
                case 4 -> right(room);
                case 5 -> play = false;
                default -> System.out.println("Ungültige Eingabe\n"); // Abfragen ob der Nutzer irgendwas anderes als 1-5 eingegeben hat
            }

            System.out.println(); // Abstand zwischen den Tabellen generieren
        }
        input.close(); // Input wieder schließen
        System.out.println("Spiel beendet!");
    }

    // Aktuelles Spielfeld in der Konsole ausgeben -> Überprüft auch nebenbei, ob das Spiel gewonnen ist
    public static void printRoom(char[][] room) {
        int chestsLeft = 0;
        for (int j=0; j<room[0].length; ++j) {
            for (int i=0; i<room.length; ++i) {
                System.out.print(room[i][j]);
                if (room[i][j] == '$') {
                    chestsLeft += 1;
                }
            }
            System.out.println();

            // Überprüfen, ob das Spiel gewonnen wurde
            if (chestsLeft == 0)
            {
                play = false;
                System.out.println("Herzlichen Glückwunsch! Du hast das Spiel gewonnen!");
            }
        }
    }

    public static void movePlayer(char[][] room, int moveX, int moveY) {
        char lastPos = ' '; //für Feld, das im letzten Zug ersetzt wurde, damit keine wichtigen Zeichen verschwinden (funktioniert noch nicht)
        for (int j=0;j<room[0].length;++j) {
            for (int i=0;i<room.length;++i) {
                if (room[i][j] == '@' || room[i][j] == '+') { // Schauen, wo der Spieler sich gerade befindet
                    if (room[i+moveX][j+moveY] == '#') {
                        System.out.println("Warnung: Du läufst gegen eine Wand");
                    }
                    else if (room[i+moveX][j+moveY] == '.') {   // Spieler landet auf Zielfeld
                        room[i+moveX][j+moveY] = '+';
                        room[i][j] = lastPos;
                        lastPos = '.';
                    }
                    else if (room[i+moveX][j+moveY] == ' ') {   // Spieler landet auf leerem Feld
                        room[i + moveX][j + moveY] = 'P';
                        room[i][j] = lastPos;
                        lastPos = ' ';
                    }
                    else if (room[i+moveX][j+moveY] == '$') {
                        if (moveChest(room, i + 2 * moveX, j + 2 * moveY)) {
                            room[i + moveX][j + moveY] = 'P';
                            room[i][j] = lastPos;
                            lastPos = '$';
                        }
                    }
                    else if (room[i+moveX][j+moveY] == '*') {
                        if (moveChest(room, i + 2 * moveX, j + 2 * moveY)) {
                            room[i + moveX][j + moveY] = 'P';
                            room[i][j] = lastPos;
                            lastPos = '*';
                        }
                    }
                    else {
                        System.out.println("Warnung: Die Kiste kann sich dort nicht hinbewegen");   //aus moveChest kopiert
                    }
                    return;
                }
            }
        }
    }

    public static boolean moveChest(char[][] room, int targetposX, int targetPosY) {
        boolean possible = true;
        // Mögliche Fälle abgleichen
            // Stößt gegen die Wand (oder gegen eine andere Kiste bzw Zielfeld)
        if (room[targetposX][targetPosY] == '#' || room[targetposX][targetPosY] == '*' || room[targetposX][targetPosY] == '$')
        {
            possible = false; // Zurückmelden, dass die Bewegung nicht möglich ist
        }
        if (room[targetposX][targetPosY] == ' ') // Gerät auf ein leeres Feld
        {
            room[targetposX][targetPosY] = '$';
        }
        if (room[targetposX][targetPosY] == '.') // Gerät auf ein Zielfeld
        {
            room[targetposX][targetPosY] = '*';
        }
        // "Move Spieler" muss dann darauf reagieren, wenn er gegen eine Kiste läuft
        // ⇾ Wenn diese sich nicht bewegen kann, kann der Spieler diese bewegung folglich auch nicht ausführen
        return possible;
    }

    // Move Befehle "übersetzen"
    public static void up(char[][] room) {
        movePlayer(room, 0, -1);
    }
    public static void down(char[][] room) {
        movePlayer(room, 0, 1);
    }
    public static void left(char[][] room) {
        movePlayer(room, -1, 0);
    }
    public static void right(char[][] room) {
        movePlayer(room, 1, 0);
    }
}
