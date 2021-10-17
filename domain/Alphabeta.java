package domain;

public class Alphabeta {

    /**
     * MinMaxBaumTiefe
     */
    private static int FAVOURED_DEPTH = 9;

    public static boolean enemy;

    public static int alphabeta(State node, int depth, int alpha, int beta, boolean max) {
	
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
        if (max) {// (* maximizing player *)
            value = Integer.MIN_VALUE;
            for (State state : node.getChildren()) { // false
                value = Math.max(value, alphabeta(state, depth + 1, alpha, beta, false));
                if(value >= beta) {
                	break;
                }
                alpha = Math.max(value, alpha);
            }

            return node.setVal(value);

            // wenn man einem min knoten ist
        } else { // (* minimizing player *)
            value = Integer.MAX_VALUE;
            for (State state : node.getChildren()) { // true
                value = Math.min(value, alphabeta(state, depth + 1, alpha, beta, true));
                if(value <= alpha) {
                	break;
                }
                beta = Math.min(value, beta);
            }

            return node.setVal(value);
        }

    }

}