package Chess.Model;

import java.awt.*;

import static Chess.Model.Board.isOutOfBoard;

public class Queen extends Figure {
    public Queen(FigureColor figureColor, Point position) {
        super(figureColor, position);
    }

    @Override
    public Figure copy() {
        Figure figure = new Queen(figureColor, new Point(position));
        figure.alive = this.alive;
        figure.moves = this.moves;
        figure.image = this.image;
        return figure;
    }

    @Override
    public boolean canMove(Point destination, boolean isHittingEnemy) {
        if (isOutOfBoard(destination))
            return false;

        return position.distance(destination) != 0 &&
                (
                        //Copy from rook
                        (position.getX() == destination.getX() ^
                                position.getY() == destination.getY()) ||
                                //copy from bishop
                                (position.getY() - position.getX() == destination.getY() - destination.getX() ||
                                        position.getY() + position.getX() == destination.getY() + destination.getX())
                );
    }
}
