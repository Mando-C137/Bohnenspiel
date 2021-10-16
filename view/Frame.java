package view;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import communication.Game;
import domain.State;

public class Frame extends JFrame {

    private JPanel boardPanel;

    private JPanel optionPanel;

    private Container c;

    private JTextField id;

    private JButton createButton;

    private JButton jButton;

    Game game;

    ArrayList<JLabel> board;

    public Frame() {
        super("Game");
        c = getContentPane();
        c.setLayout(new BorderLayout());
        setResizable(false);

        createTitle();

        createBoardPanel();
        createOptionPanel();
        createScorePanel();

        addListeners();

        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        game = new Game();
        game.setGui(this);

    }

    private void addListeners() {

        createButton.addActionListener(e -> {

            createButton.setEnabled(false);
            jButton.setEnabled(false);
            game.createGame();

        });

        jButton.addActionListener(e -> {

            if (!id.getText().isEmpty() && id.getText().matches("\\d+")) {
                createButton.setEnabled(false);
                jButton.setEnabled(false);
                id.setText("");
                id.setEditable(false);
                game.joinGame(id.getText());
            }

        });

    }

    private void createTitle() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("BOHNENSPIEL");
        label.setFont(new Font("Calibri", Font.BOLD, 20));
        panel.add(label);
        c.add(panel, BorderLayout.PAGE_START);
    }

    public static void main(String args[]) {
        Frame frame = new Frame();
        frame.setVisible(true);

    }

    void createBoardPanel() {
        this.boardPanel = new JPanel(new GridLayout(2, 6, 0, 0));

        this.board = new ArrayList<JLabel>();

        for (int i = 0; i < 12; i++) {
            JLabel add = new JLabel(String.valueOf(i));
            add.setFont(new Font("Calibri", Font.BOLD, 20));
            add.setAlignmentX(0.5f);
            add.setBackground(Color.LIGHT_GRAY);
            add.setMaximumSize(new Dimension(10, 10));
            boardPanel.add(add);
            board.add(add);
        }

        this.c.add(boardPanel, BorderLayout.CENTER);
    }

    void createScorePanel() {
        JPanel score = new JPanel();
        score.setLayout(new FlowLayout());

        JLabel myScore = new JLabel("0");
        JLabel p = new JLabel("   :   ");
        JLabel oppScore = new JLabel("0");

        JLabel[] arr = { myScore, p, oppScore };

        for (JLabel l : arr) {
            l.setFont(new Font("Calibri", Font.BOLD, 20));
            score.add(l);
        }

        c.add(score, BorderLayout.SOUTH);

    }

    void createOptionPanel() {
        this.optionPanel = new JPanel();
        this.optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("CHOOSE OPTION");
        label.setAlignmentX(0.5f);

        id = new JTextField();

        id.setBackground(Color.LIGHT_GRAY);
        id.setFont(new Font("Calibri", Font.BOLD, 20));

        createButton = new JButton("create Game");
        createButton.setAlignmentX(0.5f);
        jButton = new JButton("join Game");
        jButton.setAlignmentX(0.5f);

        this.optionPanel.add(label);
        this.optionPanel.add(createButton);
        this.optionPanel.add(id);
        this.optionPanel.add(jButton);

        c.add(optionPanel, BorderLayout.LINE_START);
    }

    public void renew() {

        for (int i = 0; i < 12; i++) {

        }

    }
}