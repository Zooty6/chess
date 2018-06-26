package Chess.Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;

import static Chess.Model.Board.isOutOfBoard;

public class Figure {
    private FigureType figureType;
    private FigureColor figureColor;
    private boolean alive = true;
    private Point position;
    private IMoveChecker moveChecker;
    private int moves = 0;
    private Image image;

    public void incMoves() {
        moves++;
    }

    private interface IMoveChecker{
        boolean isValid(Point currentPosition, Point destination, int moves, boolean isHittingEnemy);
    }

    public Figure(FigureType figureType, FigureColor figureColor, Point position) {
        this.figureType = figureType;
        this.figureColor = figureColor;
        if(isOutOfBoard(position)){
            throw new IndexOutOfBoundsException("Position is out of the board.");
        }
        this.position = position;
        //setting how valid moves are calculated based on type
        switch (figureType) {
            case PAWN:
                if(figureColor == FigureColor.WHITE) {
                    moveChecker = (currentPosition, destination, moves, isHittingEnemy) -> {
                        if (isHittingEnemy) {
                            return (destination.y - currentPosition.y == 1 &&
                                    Math.abs(destination.x - currentPosition.x) == 1);
                        } else {
                            if (moves == 0) {
                                return (destination.y - currentPosition.y == 1 || destination.y - currentPosition.y == 2) &&
                                        destination.x == currentPosition.x;
                            } else {
                                return (destination.y - currentPosition.y == 1) &&
                                        destination.x == currentPosition.x;
                            }
                        }
                    };
                }else{
                    moveChecker = (currentPosition, destination, moves, isHittingEnemy) -> {
                        if (isHittingEnemy) {
                            return (destination.y - currentPosition.y == -1 &&
                                    Math.abs(destination.x - currentPosition.x) == 1);
                        } else {
                            if (moves == 0) {
                                return (destination.y - currentPosition.y == -1 || destination.y - currentPosition.y == -2) &&
                                        destination.x == currentPosition.x;
                            } else {
                                return (destination.y - currentPosition.y == -1) &&
                                        destination.x == currentPosition.x;
                            }
                        }
                    };
                }
                break;
            case KNIGHT:
                moveChecker = (currentPosition, destination, moves, isHittingEnemy) ->
                        (Math.abs(currentPosition.getY() - destination.getY()) == 2 &&
                        Math.abs(currentPosition.getX() - destination.getX() == 1)) ||
                        (Math.abs(currentPosition.getY() - destination.getY()) == 1 &&
                        Math.abs(currentPosition.getX() - destination.getX() == 2));
                break;
            case BISHOP:
                moveChecker = (currentPosition, destination, moves, isHittingEnemy) ->
                        ((currentPosition.getY() - currentPosition.getX() == destination.getY() - destination.getX() ||
                        currentPosition.getY() + currentPosition.getX() == destination.getY() + destination.getX()) &&
                        currentPosition.distance(destination) != 0);
                break;
            case ROOK:
                moveChecker = (currentPosition, destination, moves, isHittingEnemy) ->
                                (currentPosition.getX() == destination.getX() ^
                                currentPosition.getY() == destination.getY());
                break;
            case QUEEN:
                moveChecker = (currentPosition, destination, moves1, isHittingEnemy) ->
                                currentPosition.distance(destination) != 0 &&
                                (
                                    //Copy from rook
                                    (currentPosition.getX() == destination.getX() ^
                                    currentPosition.getY() == destination.getY()) ||
                                    //copy from bishop
                                    (currentPosition.getY() - currentPosition.getX() == destination.getY() - destination.getX() ||
                                    currentPosition.getY() + currentPosition.getX() == destination.getY() + destination.getX())
                                );
                break;
            case KING:
                moveChecker = (currentPosition, destination, moves1, isHittingEnemy) ->
                                currentPosition.distance(destination) != 0 &&
                                Math.abs(currentPosition.y - destination.y) <= 1 &&
                                Math.abs(currentPosition.x - destination.x) <= 1;
                break;
        }

        File imageFile = new File("resources/figures/" + (figureColor.toString().substring(0, 1)) + figureType + ".png"); // haaaaaaack
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.out.println(String.format("Couldn't read file: %s", imageFile));
            e.printStackTrace();
        }
    }

    public boolean canMove(Point destination, boolean isHittingEnemy){
        if(isOutOfBoard(destination))
            return false;

        return moveChecker.isValid(position, destination, moves, isHittingEnemy);
    }

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
