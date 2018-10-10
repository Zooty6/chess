package Chess.Model;

import java.awt.*;

import static Chess.Model.Board.isOutOfBoard;

public class Pawn extends Figure {
    public Pawn(FigureColor figureColor, Point position) {
        super(figureColor, position);
        this.figureType = FigureType.PAWN;
    }

    @Override
    public Figure copy() {
        Figure figure = new Pawn(figureColor, new Point(position));
        figure.alive = this.alive;
        figure.moves = this.moves;
        figure.image = this.image;
        return figure;
    }

    @Override
    public boolean canMove(Point destination, boolean isHittingEnemy) {
        if(isOutOfBoard(destination))
            return false;

        if (figureColor == FigureColor.WHITE) {
            if (isHittingEnemy) {
                return (destination.y - position.y == 1 &&
                        Math.abs(destination.x - position.x) == 1);
            } else {
                if (moves == 0) {
                    return (destination.y - position.y == 1 || destination.y - position.y == 2) &&
                            destination.x == position.x;
                } else {
                    return (destination.y - position.y == 1) &&
                            destination.x == position.x;
                }
            }
        } else {
            if (isHittingEnemy) {
                return (destination.y - position.y == -1 &&
                        Math.abs(destination.x - position.x) == 1);
            } else {
                if (moves == 0) {
                    return (destination.y - position.y == -1 || destination.y - position.y == -2) &&
                            destination.x == position.x;
                } else {
                    return (destination.y - position.y == -1) &&
                            destination.x == position.x;
                }
            }
        }
    }
}
