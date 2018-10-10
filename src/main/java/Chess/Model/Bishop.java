package Chess.Model;

import java.awt.*;

import static Chess.Model.Board.isOutOfBoard;

public class Bishop extends Figure {
    public Bishop(FigureColor figureColor, Point position) {
        super(figureColor, position, FigureType.BISHOP);
    }

    @Override
    public Figure copy() {
        Figure figure = new Bishop(figureColor, new Point(position));
        figure.alive = this.alive;
        figure.moves = this.moves;
        figure.image = this.image;
        return figure;
    }

    @Override
    public boolean canMove(Point destination, boolean isHittingEnemy) {
        if(isOutOfBoard(destination))
            return false;

        return ((position.getY() - position.getX() == destination.getY() - destination.getX() ||
                position.getY() + position.getX() == destination.getY() + destination.getX()) &&
                position.distance(destination) != 0);
    }
}
