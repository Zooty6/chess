package Chess.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;

import static Chess.Model.Board.isOutOfBoard;

public abstract class Figure {
    protected FigureType figureType;
    protected FigureColor figureColor;
    protected boolean alive = true;
    protected Point position;
    protected int moves = 0;
    protected Image image;

    public void incMoves() {
        moves++;
    }

    public Figure(FigureColor figureColor, Point position, FigureType figureType) {
        this.figureType = figureType;
        this.figureColor = figureColor;
        if(isOutOfBoard(position)){
            throw new IndexOutOfBoundsException("Position is out of the board.");
        }
        this.position = position;

        File imageFile = new File("resources/figures/" + (figureColor.toString().substring(0, 1)) + figureType + ".png"); // haaaaaaack
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.out.println(String.format("Couldn't read file: %s", imageFile));
            e.printStackTrace();
        }
    }

    public abstract Figure copy();
    /*{
        Figure figure = new Figure(figureType, figureColor, new Point(position));
        figure.alive = this.alive;
        figure.moves = this.moves;
        figure.image = this.image;
        return figure;
    }*/

    public abstract boolean canMove(Point destination, boolean isHittingEnemy);
    /*{
        if(isOutOfBoard(destination))
            return false;

        return moveChecker.isValid(position, destination, moves, isHittingEnemy);
    }*/

    public boolean move(Point destination, boolean isHittingEnemy){
        if(canMove(destination, isHittingEnemy)){
            moves++;
            position.setLocation(destination);
            return true;
        }
        return false;
    }

    public Image getImage() {
        return image;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public FigureType getFigureType() {
        return figureType;
    }

    public FigureColor getFigureColor() {
        return figureColor;
    }

    public boolean isAlive() {
        return alive;
    }

    public Point getPosition() {
        return position;
    }

    public enum FigureType {
        PAWN,
        KNIGHT,
        BISHOP,
        ROOK,
        QUEEN,
        KING
    }
    public enum FigureColor {
        WHITE,
        BLACK
    }
}
