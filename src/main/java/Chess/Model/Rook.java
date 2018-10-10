package Chess.Model;

import java.awt.*;

import static Chess.Model.Board.isOutOfBoard;

public class Rook extends Figure{
    public Rook(FigureColor figureColor, Point position) {
        super(figureColor, position, FigureType.ROOK);
    }

    @Override
    public Figure copy() {
        Figure figure = new Rook(figureColor, new Point(position));
        figure.alive = this.alive;
        figure.moves = this.moves;
        figure.image = this.image;
        return figure;
    }

    @Override
    public boolean canMove(Point destination, boolean isHittingEnemy) {
        if(isOutOfBoard(destination))
            return false;
        
        return (position.getX() == destination.getX() ^
                position.getY() == destination.getY());
    }
}
