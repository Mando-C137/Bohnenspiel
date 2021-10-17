package communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import domain.Minmax;
import domain.State;

public class API {
    // static String server = "http://127.0.0.1:5000";
    static String server = "http://bohnenspiel.informatik.uni-mannheim.de";
    static String name = "mirko";

    static State state;
    static boolean MyCreation;

    public static void main(String[] args) throws Exception {
        // System.out.println(load(server));
        // createGame();
        joinGame("216");
    }

    static void createGame() throws Exception {
        String url = server + "/api/creategame/" + name;
        String gameID = load(url);
        System.out.println("Spiel erstellt. ID: " + gameID);

        url = server + "/api/check/" + gameID + "/" + name;
        while (true) {
            Thread.sleep(3000);
            String state = load(url);
            System.out.print("." + " (" + state + ")");
            if (state.equals("0") || state.equals("-1")) {
                break;
            } else if (state.equals("-2")) {
                System.out.println("time out");
                return;
            }
        }
        play(gameID);
    }

    static void joinGame(String gameID) throws Exception {
        String url = server + "/api/joingame/" + gameID + "/" + name;
        String state = load(url);
        System.out.println("Join-Game-State: " + state);
        if (state.equals("1")) {
            MyCreation = false;
            play(gameID);
        } else if (state.equals("0")) {
            System.out.println("error (join game)");
        }
    }

    static void play(String gameID) throws Exception {
        String checkURL = server + "/api/check/" + gameID + "/" + name;
        String statesMsgURL = server + "/api/statemsg/" + gameID;
        String stateIdURL = server + "/api/state/" + gameID;
        state = new State();
        int start, end;
        if (!MyCreation) {
            start = 7;
            end = 12;
        } else {
            start = 1;
            end = 6;
        }

        while (true) {
            System.out.println();
            Thread.sleep(1000);
            int moveState = Integer.parseInt(load(checkURL));
            int stateID = Integer.parseInt(load(stateIdURL));
            if (stateID != 2 && ((start <= moveState && moveState <= end) || moveState == -1)) {
                if (moveState != -1) {
                    int selectedField = moveState - 1;
                    state = State.action(state, selectedField);
                    System.out.println("Gegner waehlte: " + moveState);
                    state.printState();
                }
                // calculate fieldID
                int selectField = 0;
                int bestOption = Minmax.minimax(state, 0, true);
                for (int i = 0; i < state.getChildren().size(); i++) {
                    if (state.getChildren().get(i).getVal() == bestOption) {
                        selectField = state.getChildren().get(i).getAction();
                        System.out.println("KI spielt Mulde nummer " + (state.getChildren().get(i).getAction() + 1));
                        state = State.action(state, state.getChildren().get(i).getAction());
                        break;
                    }
                }

                state.printState();

                move(gameID, selectField + 1);
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

    static void move(String gameID, int fieldID) throws Exception {
        String url = server + "/api/move/" + gameID + "/" + name + "/" + fieldID;
        System.out.println(load(url));
    }

    static String load(String url) throws Exception {
        URI uri = new URI(url.replace(" ", ""));
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(uri.toURL().openConnection().getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        bufferedReader.close();
        return (sb.toString());
    }
}