package src.domain;

public class Minmax {

    /**
     * MinMaxBaumTiefe
     */
    private static int FAVOURED_DEPTH = 4;

    public static boolean enemy;

    public static int minimax(State node, int depth, boolean max) {

        int value = -1;

        // wenn man an einem blattknoten ist oder das spiel fertig ist
        // base case
        if (depth == FAVOURED_DEPTH || node.gameEnd()) {

            return node.setVal(node.evaluate(enemy));

        }

        // mögliche plays rausfinden und die daraus resultierenden zustände speichern

        for (int action : node.possiblePlays()) {
            node.getChildren().add((State.action(node, action)));
        }

        // wenn man an einem max knoten ist
        if (max) {
            value = Integer.MIN_VALUE;
            for (State state : node.getChildren()) { // false
                value = Math.max(value, minimax(state, depth + 1, false));
            }

            return node.setVal(value);

            // wenn man einem min knoten ist
        } else { // (* minimizing player *)
            value = Integer.MAX_VALUE;
            for (State state : node.getChildren()) { // true
                value = Math.min(value, minimax(state, depth + 1, true));
            }

            return node.setVal(value);
        }

    }

}
