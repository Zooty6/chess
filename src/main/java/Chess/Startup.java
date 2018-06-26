package Chess;

import Chess.Model.Board;
import Chess.Window.Window;

public class Startup {
    public static void main(String[] args) {
        Board board = new Board();

        Window window = new Window();
        window.initialize();

        window.redrawField();

        while (true) {
            try {
                Thread.sleep(999999);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
