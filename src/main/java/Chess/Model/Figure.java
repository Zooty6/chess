package Chess.Model;

import java.awt.*;

import static Chess.Model.Board.isOutOfBoard;

public class Figure {
    private Type type;
    private Color color;
    private boolean alive = true;
    private Point position;
    private IMoveChecker moveChecker;
    private int moves = 0;

    private interface IMoveChecker{
        boolean isValid(Point currentPosition, Point destination, int moves, boolean isHittingEnemy);
    }

    public Figure(Type type, Color color, Point position) {
        this.type = type;
        this.color = color;
        if(isOutOfBoard(position)){
            throw new IndexOutOfBoundsException("Position is out of the board.");
        }
        this.position = position;
        //setting how valid moves are calculated based on type
        switch (type) {
            case PAWN:
                if(color == Color.WHITE) {
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
                        currentPosition.getX() - destination.getX() == 1) ||
                        (Math.abs(currentPosition.getY() - destination.getY()) == 1 &&
                        currentPosition.getX() - destination.getX() == 2);
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


    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Type getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public boolean isAlive() {
        return alive;
    }

    public Point getPosition() {
        return position;
    }
    public enum Type {
        PAWN,
        KNIGHT,
        BISHOP,
        ROOK,
        QUEEN,
        KING

    }
    public enum Color {
        WHITE,
        BLACK

    }
}
