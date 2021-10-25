package src.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import src.domain.*;

public class Game2 {
    static String server = "http://bohnenspiel.informatik.uni-mannheim.de";
    static String name = "bitte beitreten";

    private String gameID;

    private State state;

    private boolean creator;

    public Game2() {

    }

    public void createGame() {

        this.creator = true;
        Alphabeta.enemy = false;

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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.state = new State();
        play();

    }

    public void joinGame(String gameID) {
        Alphabeta.enemy = true;
        this.gameID = gameID;
        this.state = new State();

        String anfrage = server + "/api/joingame/" + gameID + "/" + name;
        String res = load(anfrage);
        System.out.println("Join-Game-State: " + res);
        if (res.equals("1")) {
            creator = false;
            play();
        } else if (res.equals("0")) {
            System.out.println("error (join game)");
        }

    }

    private void play() {

        String checkURL = server + "/api/check/" + gameID + "/" + name;
        String statesMsgURL = server + "/api/statemsg/" + gameID;
        String stateIdURL = server + "/api/state/" + gameID;

        while (true) {
            System.out.println();

            try {
                Thread.sleep(100);
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
                int selectField = -1;

                state.setMyTurn(!Alphabeta.enemy);

                int bestOption = Alphabeta.alphabeta(state, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

                for (int i = 0; i < state.getChildren().size(); i++) {

                    if (state.getChildren().get(i).getVal() == bestOption) {
                        selectField = state.getChildren().get(i).getAction();

                        state = State.action(state, state.getChildren().get(i).getAction());
                        state.printState();
                        break;
                    }
                }

                if (selectField == -1) {
                    System.out.println("kein verbessernder play gefunden");
                    selectField = state.firstAction(!this.creator);
                    state = State.action(state, selectField);
                    state.printState();
                }

                System.out.println("ausgewählter Play : " + (selectField + 1));
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

    public State getState() {
        return this.state;
    }

    // wichtig nach action muss noch gegugt werden ob fertig ist
}
