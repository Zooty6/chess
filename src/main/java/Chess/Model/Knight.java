package Chess.Model;

import java.awt.*;

import static Chess.Model.Board.isOutOfBoard;

public class Knight extends Figure {
    public Knight(FigureColor figureColor, Point position) {
        super(figureColor, position, FigureType.KNIGHT);
    }

    @Override
    public Figure copy() {
        Figure figure = new Knight(figureColor, new Point(position));
        figure.alive = this.alive;
        figure.moves = this.moves;
        figure.image = this.image;
        return figure;
    }

    @Override
    public boolean canMove(Point destination, boolean isHittingEnemy) {
        if(isOutOfBoard(destination))
            return false;
        
        return (Math.abs(position.getY() - destination.getY()) == 2 &&
                Math.abs(position.getX() - destination.getX()) == 1) ||
                (Math.abs(position.getY() - destination.getY()) == 1 &&
                        Math.abs(position.getX() - destination.getX()) == 2);
    }
}
