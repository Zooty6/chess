package Chess.Window;

import Chess.Model.Figure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Playfield extends JPanel implements ActionListener {
    private Window window;

    private JButton[][] playfield;

    private static final Color blackColor = Color.decode("#282828");
    private static final Color whiteColor = Color.decode("#ebdbb2");
    private static final Color highlightedColor = Color.decode("#fe8019");

    private Figure selected;

    public Playfield(Window window) {
        this.window = window;

        playfield = new JButton[8][8];

        setLayout(new GridLayout(playfield.length, playfield[0].length));

        for (int y = 0; y < playfield.length; y++) {
            for (int x = 0; x < playfield[y].length; x++) {
                playfield[y][x] = new JButton();
                playfield[y][x].addActionListener(this);

                Point p = ConvertToBoardCoordinate(x, y);
                //playfield[y][x].setText(p.x + "/" + p.y);

                restoreBackgroundColor(x, y);

                add(playfield[y][x]);
            }
        }
    }

    public void redrawField() {
        for (int y = 0; y < playfield.length; y++) {
            for (int x = 0; x < playfield[y].length; x++) {
                Figure figure = window.getBoard().getAliveFigure(ConvertToBoardCoordinate(x, y));
                if (figure == null) {
                    playfield[y][x].setIcon(null);
                } else {
                    playfield[y][x].setIcon(new StretchIcon(figure.getImage()));
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int y = 0; y < playfield.length; y++) {
            for (int x = 0; x < playfield[y].length; x++) {
                if (e.getSource() == playfield[y][x]) {
                    Point clickedCoordinates = ConvertToBoardCoordinate(x, y);
                    Figure clicked = window.getBoard().getAliveFigure(clickedCoordinates);

                    //System.out.println(clickedCoordinates);

                    if ((clicked == null || clicked.getFigureColor() != window.getBoard().getTurn()) && selected != null) { // we wanna move there
                        Point tmpPoint = new Point(selected.getPosition());
                        boolean didMove = window.getBoard().move(selected.getPosition(), clickedCoordinates);

                        if(didMove) {
                            Point p = ConvertToFieldCoordinate(tmpPoint);
                            restoreBackgroundColor(p.x, p.y);
                            selected = null;
                        }
                    } else {
                        if (clicked != null && clicked.getFigureColor() == window.getBoard().getTurn()) {
                            playfield[y][x].setBackground(highlightedColor);
                            if (selected != null) {
                                Point point = ConvertToFieldCoordinate(selected.getPosition());
                                restoreBackgroundColor(point.x, point.y);
                            }
                            selected = clicked;
                        }
                    }
                    redrawField();
                }
            }
        }
    }

    private void restoreBackgroundColor(int x, int y) {
        if ((x + y) % 2 == 0)
            playfield[y][x].setBackground(whiteColor);
        else
            playfield[y][x].setBackground(blackColor);
    }

    private Point ConvertToBoardCoordinate(int x, int y) {
        return new Point(x + 1, playfield.length - y);
    }

    private Point ConvertToFieldCoordinate(Point position) {
        return new Point(position.x - 1, playfield.length - position.y);
    }
}
