package Chess.Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Board {
    private Set<Figure> figures = new HashSet<>();
    private Figure.FigureColor turn = Figure.FigureColor.WHITE;
    private State state = State.NORMAL;

    public Board() {
        figures.add(new Rook(Figure.FigureColor.BLACK, new Point(1, 8)));
        figures.add(new Knight(Figure.FigureColor.BLACK, new Point(2, 8)));
        figures.add(new Bishop(Figure.FigureColor.BLACK, new Point(3, 8)));
        figures.add(new Queen(Figure.FigureColor.BLACK, new Point(4, 8)));
        figures.add(new King(Figure.FigureColor.BLACK, new Point(5, 8)));
        figures.add(new Bishop(Figure.FigureColor.BLACK, new Point(6, 8)));
        figures.add(new Knight(Figure.FigureColor.BLACK, new Point(7, 8)));
        figures.add(new Rook(Figure.FigureColor.BLACK, new Point(8, 8)));
        figures.add(new Pawn(Figure.FigureColor.BLACK, new Point(1, 7)));
        figures.add(new Pawn(Figure.FigureColor.BLACK, new Point(2, 7)));
        figures.add(new Pawn(Figure.FigureColor.BLACK, new Point(3, 7)));
        figures.add(new Pawn(Figure.FigureColor.BLACK, new Point(4, 7)));
        figures.add(new Pawn(Figure.FigureColor.BLACK, new Point(5, 7)));
        figures.add(new Pawn(Figure.FigureColor.BLACK, new Point(6, 7)));
        figures.add(new Pawn(Figure.FigureColor.BLACK, new Point(7, 7)));
        figures.add(new Pawn(Figure.FigureColor.BLACK, new Point(8, 7)));
        figures.add(new Pawn(Figure.FigureColor.WHITE, new Point(1, 2)));
        figures.add(new Pawn(Figure.FigureColor.WHITE, new Point(2, 2)));
        figures.add(new Pawn(Figure.FigureColor.WHITE, new Point(3, 2)));
        figures.add(new Pawn(Figure.FigureColor.WHITE, new Point(4, 2)));
        figures.add(new Pawn(Figure.FigureColor.WHITE, new Point(5, 2)));
        figures.add(new Pawn(Figure.FigureColor.WHITE, new Point(6, 2)));
        figures.add(new Pawn(Figure.FigureColor.WHITE, new Point(7, 2)));
        figures.add(new Pawn(Figure.FigureColor.WHITE, new Point(8, 2)));
        figures.add(new Rook(Figure.FigureColor.WHITE, new Point(1, 1)));
        figures.add(new Knight(Figure.FigureColor.WHITE, new Point(2, 1)));
        figures.add(new Bishop(Figure.FigureColor.WHITE, new Point(3, 1)));
        figures.add(new Queen(Figure.FigureColor.WHITE, new Point(4, 1)));
        figures.add(new King(Figure.FigureColor.WHITE, new Point(5, 1)));
        figures.add(new Bishop(Figure.FigureColor.WHITE, new Point(6, 1)));
        figures.add(new Knight(Figure.FigureColor.WHITE, new Point(7, 1)));
        figures.add(new Rook(Figure.FigureColor.WHITE, new Point(8, 1)));
    }

    public Figure.FigureColor getTurn() {
        return turn;
    }

    public State getState() {
        return state;
    }

    public Figure getAliveFigure(Point point){
        return getAliveFigure(point, figures);
    }

    public Figure getAliveFigure(Point point, Set<Figure> figuresPool){
        Figure figure = null;
        for (Figure fig : figuresPool) {
            if (fig.isAlive()
                    && fig.getPosition().getX() == point.getX()
                    && fig.getPosition().getY() == point.getY()) {
                figure = fig;
                break;
            }
        }
        return figure;
    }

    public Set<Figure> getDeadFigures(Figure.FigureColor figureColor){
        Set<Figure> deadFigures = new HashSet<>();
        figures.forEach(figure -> {
            if(figure.getFigureColor() == figureColor && !figure.isAlive())
                deadFigures.add(figure);
        });
        return deadFigures;
    }

    public boolean move(Point from, Point to){
        if(canMove(from, to)) {
            Figure figureAtDest = getAliveFigure(to);
            if(figureAtDest != null)
                figureAtDest.setAlive(false);
            Figure fromFigure = getAliveFigure(from);
            fromFigure.setPosition(to);
            fromFigure.incMoves();
            turn = turn == Figure.FigureColor.WHITE ? Figure.FigureColor.BLACK : Figure.FigureColor.WHITE;
            renewState();
            return true;
        }
        return false;
    }

    private boolean canMove(Point from, Point to){
        return canMove(from, to, false, figures);
    }

    private boolean canMove(Point from, Point to, boolean chessCheck, Set<Figure> figuresToCalculateWith){
        if(isOutOfBoard(from) || isOutOfBoard(to))
            return false;

        Figure fromFigure = getAliveFigure(from);
        if(fromFigure == null){
            return false;
        }

        if(fromFigure.getFigureColor() != turn && !chessCheck)
            return false;

        ArrayList<Point> pointsOnTheWay = getPointsOnTheWay(from, to);
        for (Point point : pointsOnTheWay) {
            if (getAliveFigure(point, figuresToCalculateWith) != null) {
                return false;
            }
        }

        Figure toFigure = getAliveFigure(to);
        boolean isHittingEnemy = false;
        if(toFigure != null){
            if (fromFigure.getFigureColor() == toFigure.getFigureColor()) {
                return false;
            }else{
                isHittingEnemy = true;
            }
        }

        boolean allowedMove = fromFigure.canMove(to, isHittingEnemy);

        if (allowedMove && !chessCheck) {
            Set<Figure> figuresToCalculate = new HashSet<>();
            for (Figure figure : this.figures) {
                figuresToCalculate.add(figure.copy());
            }

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

        return allowedMove;
    }

    private void renewState() {
        //state = calculateState(figures, turn);
    }

    private State calculateState(Set<Figure> figuresToCalculate, Figure.FigureColor side) {
        Figure king = null;
        for (Figure figure : figuresToCalculate) {
            if(figure.getFigureType() == Figure.FigureType.KING && figure.getFigureColor() == side){
                king = figure;
                break;
            }
        }
        if(king == null)
            throw new NullPointerException();
        boolean canHitKing = false;
        for (Figure figure : figuresToCalculate) {
            if(figure.getFigureColor() != side && figure.isAlive() &&
                canMove(figure.getPosition(), king.getPosition(), true, figuresToCalculate)){
                canHitKing = true;
                break;
            }
        }
        if(canHitKing)
            return State.CHECK;

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
}
