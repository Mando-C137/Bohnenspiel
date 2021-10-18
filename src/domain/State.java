package src.domain;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class State {

    private LinkedList<State> children;

    private int action;

    private int val;

    private int myPoints;

    private boolean myTurn;

    private int oppPoints;

    private int[] board = { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 }; // position 1-12

    /**
     * Konstruktor standardmäßig
     */
    public State() {

        this.myPoints = 0;
        this.oppPoints = 0;

        this.children = new LinkedList<State>();
        this.val = 0;
        this.action = -1;
    }

    /**
     * copy constructor
     * 
     * @param s state instance to copy
     */
    public State(State s) {
        this.board = Arrays.copyOf(s.board, 12);
        this.myPoints = s.myPoints;
        this.oppPoints = s.oppPoints;
        this.children = new LinkedList<State>();
    }

    /**
     * Bewertungsfunktion für minmax, wenn man an einem blattknoten, das durch Tiefe
     * erreicht wurde. es wird über punktevergleich ausgewertet
     * 
     * @return
     */
    public int evaluate(boolean enemy) {

        // if (this.myPoints > 36) {
        // return Integer.MAX_VALUE;
        // } else if (this.oppPoints > 36) {
        // return Integer.MIN_VALUE;
        // }

        // das oben klappt schlechter als das unten

        // TODO checken ob der Gegner uns jetzt aushungern lassen kann oder der Gegner
        // ausgehungert ist.

        int best;
        if (!enemy) {
            if (this.myPoints > 36) {
                best = this.myPoints - this.oppPoints + 72;
            } else {
                best = this.myPoints - this.oppPoints;
            }
        } else {
            if (this.oppPoints > 36) {
                best = this.oppPoints - this.myPoints + 72;
            } else {
                best = this.oppPoints - this.myPoints;
            }
        }

        if (ausgehungert(enemy)) {
            int hunger = aushungern(enemy);
            best = (hunger > best) ? hunger : best;
        }

        return best;

    }

    private boolean ausgehungert(boolean enemy) {
        if (enemy) {
            for (int i = 0; i < 6; i++) {
                if (this.board[i] != 0) {
                    return false;
                }
            }
        }

        else {
            for (int i = 6; i < 12; i++) {
                if (this.board[i] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private int aushungern(boolean enemy) {

        int sum = 0;
        if (enemy) {
            for (int i = 0; i < 6; i++) {
                sum += this.board[i];
            }

            return (this.oppPoints + sum > this.myPoints) ? 72 + this.oppPoints + sum - this.myPoints : -72;

        } else {
            for (int i = 6; i < 12; i++) {
                if (this.board[i] != 0) {
                    sum += this.board[i];
                }
            }
            return (this.myPoints + sum > this.oppPoints) ? 72 + this.myPoints + sum - this.oppPoints : -72;
        }

    }

    /**
     * gibt eine Liste aus integern zurück 0 , wobei jede zahl für die i te stelle
     * zählt angefangen bei 0 mit dem zählen.
     * 
     * @return liste
     */
    public List<Integer> possiblePlays() {

        List<Integer> res = new LinkedList<Integer>();

        for (int i = 0; i < 6; i++) {
            if (this.board[myTurn ? i : 6 + i] != 0) {
                res.add(myTurn ? i : 6 + i);
            }
        }

        return res;

    }

    @Override
    public int hashCode() {
        int res = Arrays.hashCode(this.board);
        return res;
    }

    /**
     * equals methode so überschrieben, dass zwei zustände über punkte und spielfeld
     * verglichen werden.
     */
    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (!(o instanceof State)) {
            return false;
        }

        State param = (State) o;

        if (this.myTurn != param.myTurn) {
            return false;
        }

        if (this.oppPoints != param.oppPoints || this.myPoints != param.myPoints) {
            return false;
        }

        for (int i = 0; i < this.board.length; i++) {

            if (this.board[i] != param.board[i]) {
                return false;
            }

        }

        return true;

    }

    /**
     * kopiert von Dozent Christian, elegant
     * 
     * @param copy
     * @param field
     * @return
     */
    public static State action(State copy, int field) {

        State s = new State(copy);

        s.action = field;
        int startField = field;

        int value = s.board[field];
        s.board[field] = 0;
        while (value > 0) {
            field = (++field) % 12;
            s.board[field]++;
            value--;
        }

        if (s.board[field] == 2 || s.board[field] == 4 || s.board[field] == 6) {
            do {
                if (startField < 6) {
                    s.myPoints += s.board[field];
                } else {
                    s.oppPoints += s.board[field];
                }
                s.board[field] = 0;
                field = (field == 0) ? field = 11 : --field;
            } while (s.board[field] == 2 || s.board[field] == 4 || s.board[field] == 6);
        }

        s.myTurn = !copy.myTurn;
        return s;
    }

    /**
     * checkt ob ein game zu ende ist
     * 
     * @return
     */
    public boolean gameEnd() {

        return this.possiblePlays().size() == 0;

    }

    /**
     * gibt zustand aus mit Punktzahl und
     */
    public void printState() {

        System.out.println("Punktzahl KI ( max )    : " + this.myPoints);
        System.out.println("Punktzahl Gegner( min ) : " + this.oppPoints);

        System.out.println("Spielfeld sieht so aus ");

        for (int i = 0; i < 6; i++) {
            System.out.print(" " + this.board[11 - i] + " ");
        }

        System.out.println();

        for (int i = 0; i < 6; i++) {
            System.out.print(" " + this.board[i] + " ");
        }

        System.out.println();

    }

    /**
     * setter für den value und gibt ihn zurück
     * 
     * @param val
     * @return
     */
    public int setVal(int val) {
        this.val = val;
        return val;
    }

    /**
     * Getter für die KinderKnoten
     * 
     * @return
     */
    public List<State> getChildren() {
        return this.children;
    }

    /**
     * Getter für den Value
     */
    public int getVal() {
        return this.val;
    }

    /**
     * Getter für die Action ,die dazu hingeführt hat
     * 
     * @return
     */
    public int getAction() {
        return this.action;
    }

    public boolean maximizingPlayer() {
        return this.myTurn;
    }

    public void printEnd() {

        System.out
                .println("KI hat" + this.myPoints + " Punkte und Gegner hat " + this.oppPoints + " Punkte.\nChapeau\n");
    }

    public int firstAction(boolean max) {
        for (int i : possiblePlays()) {
            return i;
        }

        return 1;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

}
