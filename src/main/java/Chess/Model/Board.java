package Chess.Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Board {
    private Set<Figure> figures = new HashSet<>();
    private Turn turn = Turn.WHITES_TURN;
    private State state = State.NORMAL;

    public Board() {
        figures.add(new Figure(Figure.Type.ROOK, Figure.Color.BLACK, new Point(1, 8)));
        figures.add(new Figure(Figure.Type.KNIGHT, Figure.Color.BLACK, new Point(2, 8)));
        figures.add(new Figure(Figure.Type.BISHOP, Figure.Color.BLACK, new Point(3, 8)));
        figures.add(new Figure(Figure.Type.QUEEN, Figure.Color.BLACK, new Point(4, 8)));
        figures.add(new Figure(Figure.Type.KING, Figure.Color.BLACK, new Point(5, 8)));
        figures.add(new Figure(Figure.Type.BISHOP, Figure.Color.BLACK, new Point(6, 8)));
        figures.add(new Figure(Figure.Type.KNIGHT, Figure.Color.BLACK, new Point(7, 8)));
        figures.add(new Figure(Figure.Type.ROOK, Figure.Color.BLACK, new Point(8, 8)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.BLACK, new Point(1, 7)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.BLACK, new Point(2, 7)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.BLACK, new Point(3, 7)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.BLACK, new Point(4, 7)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.BLACK, new Point(5, 7)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.BLACK, new Point(6, 7)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.BLACK, new Point(7, 7)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.BLACK, new Point(8, 7)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.WHITE, new Point(1, 2)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.WHITE, new Point(2, 2)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.WHITE, new Point(3, 2)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.WHITE, new Point(4, 2)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.WHITE, new Point(5, 2)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.WHITE, new Point(6, 2)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.WHITE, new Point(7, 2)));
        figures.add(new Figure(Figure.Type.PAWN, Figure.Color.WHITE, new Point(8, 2)));
        figures.add(new Figure(Figure.Type.ROOK, Figure.Color.WHITE, new Point(1, 1)));
        figures.add(new Figure(Figure.Type.KNIGHT, Figure.Color.WHITE, new Point(2, 1)));
        figures.add(new Figure(Figure.Type.BISHOP, Figure.Color.WHITE, new Point(3, 1)));
        figures.add(new Figure(Figure.Type.QUEEN, Figure.Color.WHITE, new Point(4, 1)));
        figures.add(new Figure(Figure.Type.KING, Figure.Color.WHITE, new Point(5, 1)));
        figures.add(new Figure(Figure.Type.BISHOP, Figure.Color.WHITE, new Point(6, 1)));
        figures.add(new Figure(Figure.Type.KNIGHT, Figure.Color.WHITE, new Point(7, 1)));
        figures.add(new Figure(Figure.Type.ROOK, Figure.Color.WHITE, new Point(8, 1)));
    }

    public Turn getTurn() {
        return turn;
    }

    public State getState() {
        return state;
    }

    public Figure getAliveFigure(Point point){
        Figure figure = null;
        for (Figure fig : figures) {
            if (fig.isAlive()
                    && fig.getPosition().getX() == point.getX()
                    && fig.getPosition().getY() == point.getY()) {
                figure = fig;
                break;
            }
        }
        return figure;
    }

    public Set<Figure> getDeadFigures(Figure.Color color){
        Set<Figure> deadFigures = new HashSet<>();
        figures.forEach(figure -> {
            if(figure.getColor() == color && !figure.isAlive())
                deadFigures.add(figure);
        });
        return deadFigures;
    }

    public boolean move(Point from, Point to){
        if(canMove(from, to)) {
            Figure figureAtDest = getAliveFigure(to);
            if(figureAtDest != null)
                figureAtDest.setAlive(false);
            getAliveFigure(from).setPosition(to);
            turn = turn == Turn.WHITES_TURN ? Turn.BLACKS_TURN : Turn.WHITES_TURN;
            renewState();
            return true;
        }
        return false;
    }

    private boolean canMove(Point from, Point to){
        if(isOutOfBoard(from) || isOutOfBoard(to))
            return false;

        Figure fromFigure = getAliveFigure(from);
        if(fromFigure == null){
            return false;
        }

        ArrayList<Point> pointsOnTheWay = getPointsOnTheWay(from, to);
        for (Point point : pointsOnTheWay) {
            if (getAliveFigure(point) != null) {
                return false;
            }
        }

        Figure toFigure = getAliveFigure(to);
        boolean isHittingEnemy = false;
        if(toFigure != null){
            if (fromFigure.getColor() == toFigure.getColor()) {
                return false;
            }else{
                isHittingEnemy = true;
            }
        }

        if (fromFigure.canMove(to, isHittingEnemy)) {
            Set<Figure> figuresToCalculate = new HashSet<>(figures);
            for (Figure figure : figuresToCalculate) {
                if(figure.getPosition().getX() == to.getX() && figure.getPosition().getY() == to.getY()
                        && figure.isAlive()) {
                    figure.setAlive(false);
                    break;
                }
            }
            for (Figure figure : figuresToCalculate) {
                if(figure.getPosition().getX() == from.getX() && figure.getPosition().getY() == from.getY()
                        && figure.isAlive()){
                    figure.setPosition(to);
                    break;
                }
            }
            return !(calculateState(figuresToCalculate, turn) == State.CHECK);
        }
        return false;
    }

    private void renewState() {
        state = calculateState(figures, turn);
    }

    private State calculateState(Set<Figure> figuresToCalculate, Turn side) {
        //TODO
        return State.NORMAL;
    }

    private ArrayList<Point> getPointsOnTheWay(Point from, Point to) {
        ArrayList<Point> points = new ArrayList<>();

        if(from.getX() == to.getX()){
            if(from.getY() > to.getY()){
                for (int i = (int) from.getY() - 1; i > to.getY(); i--) {
                    points.add(new Point((int) from.getX(), i));
                }
                return points;
            }else{
                for (int i = (int) (from.getY() + 1); i < to.getY(); i++) {
                    points.add(new Point((int) from.getX(), i));
                }
                return points;
            }
        }
        if(from.getY() == to.getY()){
            if(from.getX() > to.getX()){
                for (int i = (int) from.getX() - 1; i > to.getX(); i--) {
                    points.add(new Point(i, (int) from.getY()));
                }
                return points;
            }else{
                for (int i = (int) (from.getX() + 1); i < to.getX(); i++) {
                    points.add(new Point(i, (int) from.getY()));
                }
                return points;
            }
        }
        if(from.getX() - from.getY() == to.getX() - to.getY()){
            if(from.getX() < to.getX()){
                for (int i = (int) (from.getX() + 1); i < to.getX(); i++) {
                    points.add(new Point(i, (int) (from.getY() + i - from.getX())));
                }
                return points;
            }else{
                for (int i = (int) (from.getX() - 1); i > to.getX(); i--) {
                    points.add(new Point(i, (int) (from.getY() - from.getX() + i)));
                }
                return points;
            }
        }
        if(from.getX() + from.getY() == to.getX() + to.getY()){
            if(from.getX() < to.getX()){
                for (int i = (int) (from.getX() + 1); i < to.getX(); i++) {
                    points.add(new Point(i, (int) (from.getY() - i + from.getX())));
                }
                return points;
            }else{//x=5, Y=3 i=4
                for (int i = (int) (from.getX() - 1); i > to.getX(); i--) {
                    points.add(new Point(i, (int) (from.getY() + from.getX() - i)));
                }
                return points;
            }
        }
        return points;
    }

    static boolean isOutOfBoard(Point point){
        return point.getX() > 8 || point.getY() > 8 ||
                point.getX() < 1 || point.getY() < 1 ||
                point.getX() != Math.floor(point.getX()) ||
                point.getY() != Math.floor(point.getY());
    }

    public enum State{
        NORMAL,
        CHECK,
        CHECKMATE,
        DRAW
    }

    public enum Turn{
        WHITES_TURN,
        BLACKS_TURN
    }



}
