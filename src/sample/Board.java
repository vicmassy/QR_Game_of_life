package sample;

import org.moeaframework.core.variable.BinaryVariable;

public class Board {

    private Cell[][] grid;
    private int height;
    private int width;

    public Board(Cell[][] grid) {
        this.grid = grid;
        height = width = grid.length;
    }

    public Board(int height, int width) {
        this.height = height;
        this.width = width;
        grid = new Cell[height][width];

        for (int h=0; h<grid.length; h++){
            for (int w=0; w<grid[h].length; w++){
                grid[h][w] = new Cell();
            }
        }
    }

    public Board(BinaryVariable chromosome, int height, int width) {
        this.height = height;
        this.width = width;
        grid = new Cell[height][width];
        for(int i = 0; i < Problem.DEFAULT_SIZE; ++i) {
            for(int j = 0; j < Problem.DEFAULT_SIZE; ++j) {
                grid[i][j] = new Cell(chromosome.get(i*Problem.DEFAULT_SIZE+j));
            }
        }
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public int getSize() {
        return width;
    }

    public int checkNeighbours(int row, int col) {
        int count = 0;
        if (row > 0 && col > 0) {
            if (isAlive(row-1,col-1)) ++count;
        }
        if (row > 0) {
            if(isAlive(row-1, col)) ++count;
        }
        if (row > 0 && col < width-1) {
            if(isAlive(row-1, col+1)) ++count;
        }
        if (col > 0) {
            if (isAlive(row,col-1)) ++count;
        }
        if (col < width-1) {
            if (isAlive(row, col+1)) ++count;
        }
        if (row < height-1 && col > 0) {
            if (isAlive(row+1, col-1)) ++count;
        }
        if (row < height-1) {
            if (isAlive(row+1, col)) ++count;
        }
        if (row < height-1 && col < width-1) {
            if (isAlive(row+1, col+1)) ++count;
        }
        return count;
    }

    public boolean isAlive(int row, int col) {
        return grid[row][col].getState();
    }

    public void prepare() {
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int neighbours = checkNeighbours(i,j);
                if (neighbours < 2 || neighbours > 3) grid[i][j].setNewState(false);
                else if (neighbours == 3) grid[i][j].setNewState(true);
                else grid[i][j].setNewState(grid[i][j].getState());
            }
        }
    }

    public void update() {
        prepare();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                grid[i][j].updateState();
            }
        }
    }

}
