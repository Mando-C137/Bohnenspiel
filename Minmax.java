import java.util.LinkedList;
import java.util.List;

public class Minmax {

    static int FAVOURED_DEPTH = 6;

    private static int minimax(State node, int depth, boolean maximizingPlayer) {

        int value;

        List<State> children = new LinkedList<State>();
        for (int action : node.possiblePlays()) {
            children.add(State.action(action, node));
        }

        for (State b : children) {
            b.printState();
        }

        if (depth == FAVOURED_DEPTH || children.isEmpty())
            return node.maxEvalFunction();

        if (node.maximizingPlayer()) {
            value = Integer.MIN_VALUE;
            for (State state : children) {
                value = Math.max(value, minimax(state, depth + 1, false));
            }

            return value;
        } else // (* minimizing player *)
            value = Integer.MAX_VALUE;
        for (State state : children) {
            value = Math.min(value, minimax(state, depth + 1, true));
        }

        return value;

    }

    public static void main(String[] args) {
        State root = new State();
        root.printState();

        System.out.println(minimax(root, 0, true));

    }

}
