package domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Minmax {

    /**
     * MinMaxBaumTiefe
     */
    static int FAVOURED_DEPTH = 8;

    public static int minimax(State node, int depth, boolean max) {

        int value = -1;

        // wenn man an einem blattknoten ist oder das spiel fertig ist
        // base case
        if (depth == FAVOURED_DEPTH || node.gameEnd()) {

            return node.setVal(node.maxEvalFunction());

        }

        // mögliche plays rausfinden und die daraus resultierenden zustände speichern
        LinkedList<State> children = new LinkedList<State>();
        for (int action : node.possiblePlays()) {
            children.add(State.action(node, action));
        }

        // die zustände als kinder speichern
        node.setChildren(children);

        // wenn man an einem max knoten ist
        if (!max) {
            value = Integer.MIN_VALUE;
            for (State state : children) { // false
                value = Math.max(value, minimax(state, depth + 1, !max));
            }

            return node.setVal(value);

            // wenn man einem min knoten ist
        } else { // (* minimizing player *)
            value = Integer.MAX_VALUE;
            for (State state : children) { // true
                value = Math.min(value, minimax(state, depth + 1, !max));
            }

            return node.setVal(value);
        }

    }

    public static void main(String[] args) {
        State root = new State(true);
        root.printState();

        int bestOption = minimax(root, 0, true);

        while (!root.gameEnd()) {

            System.out.println("\n-----------------------\n");

            for (int i = 0; i < root.getChildren().size(); i++) {
                System.out.println("bestoption = " + bestOption);
                if (root.getChildren().get(i).getVal() == bestOption) {
                    System.out.println("Ki spielt Mulde nummer " + (root.getChildren().get(i).getAction() + 1));
                    root = State.action(root, root.getChildren().get(i).getAction());
                    root.printState();
                    break;
                }

            }

            if (root.gameEnd()) {
                break;
            }

            System.out.println("\n-----------------------\n");

            int number = chooseNum();

            System.out.println("Ich spiele Mulde nummer " + (12 - number));
            root = State.action(root, number);

            root.printState();

            waitforInput();

            bestOption = minimax(root, 0, true);

        }

        System.out.println("------------------");
        System.out.println("Spiel ist terminiert");
        root.printEnd();
    }

    private static int chooseNum() {
        Pattern pattern = Pattern.compile("-?\\d+");
        int number = -1;
        System.out.println("Welche stelle willst du spielen aus [1 ,  6 ]? : ");
        while (number < 1 || number > 6) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String input = null;
            try {
                input = bufferedReader.readLine();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (pattern.matcher(input).matches()) {
                number = Integer.parseInt(input);
            }

        }

        return 12 - number;
    }

    private static void waitforInput() {
        System.out.println("\n-----------------------\n");
        System.out.println("Press sth to let AI play");
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }

}
