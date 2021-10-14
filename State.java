import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class State {

    public int minMaxVal;

    private int myPoints;

    private int oppPoints;

    private boolean myTurn;

    private int[] board = { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6 }; // position 1-12

    public State() {
        this.myPoints = 0;
        this.oppPoints = 0;
        this.myTurn = true; // ??;
    }

    public int maxEvalFunction() {

        if (this.myPoints > 36) {
            return Integer.MAX_VALUE;
        } else

            return this.myPoints - this.oppPoints;
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
            if (this.board[this.myTurn ? i : 6 + i] != 0) {
                res.add(this.myTurn ? i : 6 + i);
            }
        }

        return res;

    }

    @Override
    public int hashCode() {
        int res = Arrays.hashCode(this.board);
        return res;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (!(o instanceof State)) {
            return false;
        }

        State param = (State) o;

        for (int i = 0; i < this.board.length; i++) {

            if (this.board[i] != param.board[i]) {
                return false;
            }

        }

        return true;

    }

    public static State action(int field, State copy) {

        State s = new State(copy);

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

        return s;
    }

    public State(State s) {
        this.board = Arrays.copyOf(s.board, 12);
        this.myPoints = s.myPoints;
        this.oppPoints = s.oppPoints;
        this.myTurn = s.myTurn;
    }

    public boolean maximizingPlayer() {
        return this.myTurn;
    }

    public void printState() {

        System.out.println("Punktzahl Maximierer : " + this.myPoints);
        System.out.println("Punktzahl Minimierer : " + this.oppPoints);

        System.out.println("Spielfeld sieht so aus ");

        for (int i = 0; i < 12; i++) {
            System.out.print(" " + this.board[i] + " ");
            if (i == 5) {
                System.out.println();
            }
        }

        System.out.println();
    }

}
