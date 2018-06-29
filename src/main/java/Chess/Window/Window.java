package Chess.Window;

import Chess.Model.Board;

import javax.swing.*;
import java.awt.*;


public class Window extends JFrame {

    private Board board;

    private Playfield playfield;

    public Window() {
        super("Super great chess game");

        board = new Board();
        playfield = new Playfield(this);
    }

    public void initialize() {
        setLayout(new BorderLayout());

        add(playfield, BorderLayout.CENTER);

        setPreferredSize(new Dimension(600, 600));
        // setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void redrawField() {
        playfield.redrawField();
    }

    public Board getBoard() {
        return board;
    }
}
