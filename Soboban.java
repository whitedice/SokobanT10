import java.util.Scanner;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Sokoban
{
    public static boolean play = true; // Boolean erstellt der angibt, ob das Spiel aktuell läuft

    public static void main(String[] args) throws Exception {
        // Args auslesen
        Path fp = Paths.get("sokoban.txt"); // Standard-Pfad bestimmen
        if (args.length != 0)
        {
            fp = Paths.get(args[0]); // Falls Argumente gegeben wurden diese verwenden
        }

        Scanner input = new Scanner(System.in); // Scanner für die Eingabe vom Benutzer erstellen

        // Anfangsspielbrett definieren
        char [][] room = ReadTXT(fp);

        while (play) {
            printRoom(room); // Den aktuellen Raum ausgeben & Überpfüfen, ob das spiel schon gewonnen wurde

            if (play) // Deswegen erneuter check, ob das Spiel nicht schon beendet wurde
            {
                System.out.println("(1)up (2)down (3)left (4)right (5)aufgeben");
                int choice = input.nextInt(); // Nutzerinput abfragen
                switch (choice) {
                    case 1 -> movePlayer(room, 0, -1);
                    case 2 -> movePlayer(room, 0, 1);
                    case 3 -> movePlayer(room, -1, 0);
                    case 4 -> movePlayer(room, 1, 0);
                    case 5 -> play = false;
                    default -> System.out.println("Ungültige Eingabe\n"); // Abfragen ob der Nutzer irgendwas anderes als 1-5 eingegeben hat
                }
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
        }

        // Überprüfen, ob das Spiel gewonnen wurde
        if (chestsLeft == 0)
        {
            play = false;
            System.out.println("Herzlichen Glückwunsch! Du hast das Spiel gewonnen!");
        }
    }

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
                // verhindert, dass eine Zeile die länger ist, als die anderen, nicht mehr ausgelesen werden kann... eigentlich implementiert
                // gewesen, weil ich ursprünglich dachte, wir sollten uns selbst drum kümmern, dass z.B. die Ecken aufgefüllt werden
        }

        char[][] feld = new char[x][y]; // Array erstellen

        // Im zweiten Durchlauf Daten einfügen

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


    public static void movePlayer(char[][] room, int moveX, int moveY) {
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

        // TargetPos = Wand
        if (room[plPosX+moveX][plPosY+moveY] == '#') {
            System.out.println("Warnung: Du läufst gegen eine Wand");
        }

        // TargetPos = Zielfeld
        else if (room[plPosX+moveX][plPosY+moveY] == '.') {   // Spieler landet auf Zielfeld
            room[plPosX+moveX][plPosY+moveY] = '+';
            room[plPosX][plPosY] = ' ';
        }

        // TargetPos = Leeres Feld
        else if (room[plPosX+moveX][plPosY+moveY] == ' ') {   // Spieler landet auf leerem Feld
            room[plPosX + moveX][plPosY + moveY] = '@';
            if(room[plPosX][plPosY] == '+') // Wenn Spieler von einem Zielfeld kam, das auch wieder so darstellen
                room[plPosX][plPosY]= '.';
            else
                room[plPosX][plPosY]= ' ';
        }

        // TargetPos = Kiste
        else if (room[plPosX+moveX][plPosY+moveY] == '$') {
            if (moveChest(room, plPosX + 2 * moveX, plPosY + 2 * moveY)) {
                room[plPosX + moveX][plPosY + moveY] = '@';
                if(room[plPosX][plPosY] == '+')
                    room[plPosX][plPosY]= '.';
                else
                    room[plPosX][plPosY]= ' ';
            }
            else {
                System.out.println("Warnung: Die Kiste kann sich dort nicht hinbewegen");   //aus moveChest kopiert
            }
        }

        // TargetPos = Kiste auf einem Zielfeld
        else if (room[plPosX+moveX][plPosY+moveY] == '*') {
            if (moveChest(room, plPosX + 2 * moveX, plPosY + 2 * moveY)) {
                room[plPosX + moveX][plPosY + moveY] = '@';
                room[plPosX][plPosY] = ' ';
            }
            else {
                System.out.println("Warnung: Die Kiste kann sich dort nicht hinbewegen");   //aus moveChest kopiert
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
        return possible;
    }
}
