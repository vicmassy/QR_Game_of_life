package sample;

import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class JavaFXDisplayDriver implements DisplayDriver{

    private int sz;
    private TilePane tilePane = new TilePane(3,3);

    public JavaFXDisplayDriver(int boardSize, int cellSizePx, Board board) {
        sz = boardSize;
        tilePane.setPrefRows(boardSize);
        tilePane.setPrefColumns(boardSize);

        Cell[][] g = board.getGrid();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Color c = g[i][j].getState() ? Color.BLACK : Color.WHITE;
                Rectangle r = new Rectangle(cellSizePx, cellSizePx, c);
                tilePane.getChildren().add(r);

                attachListeners(r, g[i][j]);
            }
        }
    }

    public void displayBoard(Board board) {
        Cell[][] g = board.getGrid();
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[0].length; j++) {
                Rectangle r = (Rectangle) tilePane.getChildren().get(boardToPaneCoords(i, j));
                r.setFill(g[i][j].getState() ? Color.BLACK : Color.WHITE);
            }
        }
    }

    public TilePane getPane() {
        return tilePane;
    }

    private int boardToPaneCoords(int i, int j) {
        return i * sz + j;
    }

    private void attachListeners(Rectangle r, Cell c) {
        r.setOnMouseClicked(e -> {
            r.setFill(c.getState() ? Color.WHITE : Color.BLACK);
            c.setNewState(!c.getState());
            c.updateState();
        });
    }

}
