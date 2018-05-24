package sample;

public class Cell {

    private boolean state;
    private boolean newState;

    public Cell() {
        this.state = false;
    }

    public Cell(boolean state) {
        this.state = state;
    }

    public void setNewState(boolean state) {
        newState = state;
    }

    public void updateState() {
        state = newState;
    }

    public boolean getState() {
        return state;
    }

}
