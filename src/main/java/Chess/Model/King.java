package Chess.Model;

import java.awt.*;

import static Chess.Model.Board.isOutOfBoard;

public class King extends Figure {
    public King(FigureColor figureColor, Point position) {
        super(figureColor, position, FigureType.KING);
    }

    @Override
    public Figure copy() {
        Figure figure = new King(figureColor, new Point(position));
        figure.alive = this.alive;
        figure.moves = this.moves;
        figure.image = this.image;
        return figure;
    }

    @Override
    public boolean canMove(Point destination, boolean isHittingEnemy) {
        if(isOutOfBoard(destination))
            return false;
        
        return position.distance(destination) != 0 &&
                Math.abs(position.y - destination.y) <= 1 &&
                Math.abs(position.x - destination.x) <= 1;
    }
}
