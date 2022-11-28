import java.util.Scanner;

public class Sokoban
{
    public static boolean play = true; // Boolean erstellt der angibt, ob das Spiel aktuell lÃ¤uft
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in); // Scanner fÃ¼r die Eingabe vom Benutzer erstellen
        char [][] room = new char[8][4];

        // Anfangsspielbrett definieren
        for (int j=0;j<room[0].length;++j) {
            for (int i=0;i<room.length;++i) {
                room[i][j] = '.';
            }
        }
        room[0][0] = 'P'; // Spieler in die Ecke Links oben setzen
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
                default -> System.out.println("UngÃ¼ltige Eingabe\n"); // Abfragen ob der Nutzer irgendwas anderes als 1-5 eingegeben hat
            }

            System.out.println(); // Abstand zwischen den Tabellen generieren
        }
        input.close(); // Input wieder schlieÃŸen
        System.out.println("Spiel beendet!");
    }

    // Aktuelles Spielfeld in der Konsole ausgeben
    public static void printRoom(char[][] room) {
        for (int j=0; j<room[0].length; ++j) {
            for (int i=0; i<room.length; ++i) {
                System.out.print(room[i][j]);
            }
            System.out.println();
        }
    }

    public static void move(char[][] room, int x, int y) {
        for (int j=0;j<room[0].length;++j) {
            for (int i=0;i<room.length;++i) {
                if (room[i][j] == 'P') { // Schauen, wo der Spieler sich gerade befindet

                    // Den ArrayIndexOutOfBoundsException Fehler abfangen â†’ Tritt auf, wenn Nutzer aus dem Spielfeld ausbrechen wÃ¼rde
                    if (i+x < 0 || i+x > 7 || j+y < 0 || j+y > 3) {
                        System.out.println("Warnung: Du kannst nicht aus dem Spielfeld laufen!");
                    }
                    else {
                        room[i+x][j+y] = 'P'; // Den gefundenen Spieler dann verschieben
                        room[i][j] = '.';
                    }

                    return;
                }
            }
        }
    }

    // Move Befehle "Ã¼bersetzen"
    public static void up(char[][] room) {
        move(room, 0, -1);
    }
    public static void down(char[][] room) {
        move(room, 0, 1);
    }
    public static void left(char[][] room) {
        move(room, -1, 0);
    }
    public static void right(char[][] room) {
        move(room, 1, 0);
    }
}