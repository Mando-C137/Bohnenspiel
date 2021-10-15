import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Minmax {

    static int FAVOURED_DEPTH = 6;

    private static int minimax(State node, int depth, boolean maximizingPlayer) {

        int value = -1;

        LinkedList<State> children = new LinkedList<State>();
        for (int action : node.possiblePlays()) {
            children.add(State.action(node, action));
        }

        node.setChildren(children);

        // for (State b : children) {
        // b.printState();
        // }

        if (depth == FAVOURED_DEPTH || children.isEmpty()) {
            if (children.isEmpty())
                System.out.println("emptylist");
            return node.setVal(node.maxEvalFunction());
        }

        if (node.maximizingPlayer()) {
            value = Integer.MIN_VALUE;
            for (State state : children) {
                value = Math.max(value, minimax(state, depth + 1, false));
            }

            return node.setVal(value);
        } else { // (* minimizing player *)
            value = Integer.MAX_VALUE;
            for (State state : children) {
                value = Math.min(value, minimax(state, depth + 1, true));
            }

            return node.setVal(value);
        }

    }

    public static void main(String[] args) {
        State root = new State();
        root.printState();

        int bestOption = minimax(root, 0, true);

        while (!root.gameEnd()) {

            System.out.println("\n-----------------------\n");

            for (int i = 0; i < root.getChildren().size(); i++) {
                if (root.getChildren().get(i).getVal() == bestOption) {
                    System.out.println("Ki spielt Mulde nummer " + root.getChildren().get(i).getAction());
                    root = State.action(root, root.getChildren().get(i).getAction());
                    root.printState();
                    break;
                }

            }

            System.out.println("\n-----------------------\n");

            int number = extracted();

            System.out.println("Ich spiele Mulde nummer " + number);
            root = State.action(root, number);

            root.printState();
            bestOption = minimax(root, 0, true);

        }
    }

    private static int extracted() {
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

}
