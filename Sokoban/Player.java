package Sokoban;

public class Player {
    private int[] pos = new int[2];
    public Player(Level room) {
        this.pos[0] = room.findPlayerPos()[0];
        this.pos[1] = room.findPlayerPos()[1];
    }

    public int[] getPlayerPos() {
        return this.pos;
    }

    public void move(Level room, int moveX, int moveY) throws Exception {
        // Variablen für die Positionen erstellen (damit es übersichtlicher bleibt)
        int targetPosX = this.pos[0] + moveX;
        int targetPosY = this.pos[1] + moveY;
        int currentPosX = this.pos[0];
        int currentPosY = this.pos[1];
        char charAtPosition = room.getCharAtPosition(targetPosX, targetPosY);
        char charAtCurrent = room.getCharAtPosition(currentPosX, currentPosY);

        // TargetPos = Wand
        if (charAtPosition == '#') {
            throw new Exception("Warnung: Du läufst gegen eine Wand");
        }

        // TargetPos = Zielfeld
        else if (charAtPosition == '.') {   // Spieler landet auf Zielfeld
            room.setCharAtPosition(targetPosX, targetPosY, '+');

            // Altes Feld ersetzen
            room.setCharAtPosition(currentPosX, currentPosY, ' ');

            // Spielerposition aktualisieren
            this.pos[0] = targetPosX;
            this.pos[1] = targetPosY;
        }

        // TargetPos = Leeres Feld
        else if (charAtPosition == ' ') {   // Spieler landet auf leerem Feld
            room.setCharAtPosition(targetPosX, targetPosY,'@');

            // Altes Feld ersetzen
            if(charAtCurrent == '+') // Wenn Spieler von einem Zielfeld kam, das auch wieder so darstellen
                room.setCharAtPosition(currentPosX, currentPosY, '.');
            else
                room.setCharAtPosition(currentPosX, currentPosY, ' ');

            // Spielerposition aktualisieren
            this.pos[0] = targetPosX;
            this.pos[1] = targetPosY;
        }

        // TargetPos = Kiste
        else if (charAtPosition == '$') {
            if (moveChest(room, targetPosX + moveX, targetPosY + moveY)) {
                room.setCharAtPosition(targetPosX, targetPosY, '@');

                // Altes Feld ersetzen
                if(charAtCurrent == '+')
                    room.setCharAtPosition(currentPosX, currentPosY, '.');
                else
                    room.setCharAtPosition(currentPosX, currentPosY, ' ');

                // Spielerposition aktualisieren
                this.pos[0] = targetPosX;
                this.pos[1] = targetPosY;
            }
            else {
                throw new Exception("Warnung: Die Kiste kann sich dort nicht hinbewegen"); // Hinweis senden, wenn sich die Kisten nicht bewegen kann
            }
        }

        // TargetPos = Kiste auf einem Zielfeld
        else if (charAtPosition == '*') {
            if (moveChest(room, targetPosX + moveX, targetPosY + moveY)) {
                room.setCharAtPosition(targetPosX, targetPosY, '+');

                // Altes Feld ersetzen
                room.setCharAtPosition(currentPosX, currentPosY, ' ');

                // Spielerposition aktualisieren
                this.pos[0] = targetPosX;
                this.pos[1] = targetPosY;
            }
            else {
                throw new Exception("Warnung: Die Kiste kann sich dort nicht hinbewegen"); // Hinweis senden, wenn sich die Kisten nicht bewegen kann
            }
        }
    }

    private boolean moveChest(Level room, int targetPosX, int targetPosY) { // Helper-Methode für move() um Kisten zu bewegen
        boolean possible = true;
        char CharAtPosition = room.getCharAtPosition(targetPosX, targetPosY);

        /* ------------------------------------
             Mögliche Fälle abgleichen
        ------------------------------------ */
        // Stößt gegen die Wand (oder gegen eine andere Kiste bzw Zielfeld)
        if (CharAtPosition == '#' || CharAtPosition == '*' || CharAtPosition == '$')
            possible = false; // Zurückmelden, dass die Bewegung nicht möglich ist

        if (CharAtPosition == ' ') // Kiste kommt auf ein leeres Feld
            room.setCharAtPosition(targetPosX, targetPosY, '$');

        if (CharAtPosition == '.') // Kiste kommt auf ein Zielfeld
            room.setCharAtPosition(targetPosX, targetPosY, '*');

        return possible;
    }
}