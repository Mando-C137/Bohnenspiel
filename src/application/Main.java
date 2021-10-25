package src.application;

import src.communication.Game2;

public class Main {

    public static void main(String[] args) {
        Game2 game = new Game2();

        /**
         * Eine der beiden Methoden muss auskommentiert sein!
         */

        /**
         * Hier kann das Spiel erstellt werden, d.h. unsere KI erstellt das Spiel und
         * Ihre KI sollte dann beitreten.
         */
        // game.createGame();

        /**
         * Hier kann einem Spiel beigetreten werden, d.h. Ihre KI muss das Spiel
         * erstellen und Sie müssen in dem StringParameter die Spiel ID eingeben.
         */
        game.joinGame("3322");
    }

}
