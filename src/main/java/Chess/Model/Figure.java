package Chess.Model;

import java.awt.*;

public class Figure {
    private Type type;
    private Color color;
    private boolean alive = true;
    private Point position;

    public Figure(Type type, Color color, Point position) {
        this.type = type;
        this.color = color;
        if(position.getX() > 8 || position.getY() > 8 ||
           position.getX() < 1 || position.getY() < 1 ){
            throw new IndexOutOfBoundsException("Position is out of the board.");
        }
        this.position = position;
    }

    public boolean move(Point destination){
        //TODO
        return false;
    }

    public enum Type {
        pawn,
        knight,
        bishop,
        rook,
        queen,
        king
    }

    public enum Color {
        white,
        black
    }
}
