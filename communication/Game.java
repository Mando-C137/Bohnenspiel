package communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import domain.Minmax;
import domain.State;

public class Game {
    static String server = "http://bohnenspiel.informatik.uni-mannheim.de";
    static String name = "mirko";

    String gameID;

    State state;

    boolean creator;

    private Game() {

    }

    void createGame() {

        this.creator = true;

        String anfrage = server + "/api/creategame/" + name;

        gameID = load(anfrage);

        anfrage = server + "/api/check/" + gameID + "/" + name;

        while (true) {

            String state = load(anfrage);

            if (state.equals("0") || state.equals("-1")) {
                break;
            } else if (state.equals("-2")) {
                System.out.println("time out");
                return;
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        startGame();

    }

    private void startGame() {

        this.state = new State(true);

        play();

    }

    private void play() {

        String checkURL = server + "/api/check/" + gameID + "/" + name;
        String statesMsgURL = server + "/api/statemsg/" + gameID;
        String stateIdURL = server + "/api/state/" + gameID;

        while (true) {
            System.out.println();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            int moveState = Integer.parseInt(load(checkURL));
            int stateID = Integer.parseInt(load(stateIdURL));
            if (stateID != 2 && (inRange(moveState) || moveState == -1)) {
                if (moveState != -1) {
                    int selectedField = moveState - 1;
                    state = State.action(state, selectedField);
                    System.out.println("Gegner waehlte: " + moveState);
                    state.printState();
                }
                // calculate fieldID
                int selectField = 0;
                int bestOption = Minmax.minimax(state, 0);
                for (int i = 0; i < state.getChildren().size(); i++) {
                    if (state.getChildren().get(i).getVal() == bestOption) {
                        selectField = state.getChildren().get(i).getAction();
                        System.out.println("KI spielt Mulde nummer " + (state.getChildren().get(i).getAction() + 1));
                        state = State.action(state, state.getChildren().get(i).getAction());
                        break;
                    }
                }

                state.printState();

                sendMove(selectField + 1);
            } else if (moveState == -2 || stateID == 2) {
                System.out.println("GAME Finished");
                checkURL = server + "/api/statemsg/" + gameID;
                System.out.println(load(checkURL));
                return;
            } else {
                System.out.println("- " + moveState + "\t\t" + load(statesMsgURL));
            }

        }

    }

    private void sendMove(int action) {
        String anfrage = server + "/api/move/" + gameID + "/" + name + "/" + action;
        load(anfrage);

    }

    static String load(String url) {

        StringBuilder sb = null;
        try {
            URI uri = new URI(url.replace(" ", ""));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(uri.toURL().openConnection().getInputStream()));
            sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return (sb.toString());
    }

    boolean inRange(int answer) {
        if (!creator) {
            return answer >= 1 && answer <= 6;
        } else {
            return answer >= 7 && answer <= 12;
        }
    }

    public static void main(String[] args) {

        Game game = new Game();

        game.createGame();

    }
}
