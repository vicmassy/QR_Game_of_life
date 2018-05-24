package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import sample.JavaFXDisplayDriver;

public class Controller implements Initializable {

    private final int DEFAULT_SIZE = 25;

    @FXML
    private FlowPane base;
    @FXML
    private Button runButton, stopButton, loadButton, saveButton, cleanButton, rootButton;
    @FXML
    private HBox rootBox;
    @FXML
    private Label generation;

    private Board board;

    private JavaFXDisplayDriver display;

    private Timeline loop = null;
    private int cont = 0;
    private int cellSizePx = 20;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoard(DEFAULT_SIZE);
        generation.setText(String.valueOf(cont));
        attachResizeListener();
    }

    @FXML
    private void onRun(Event evt) {
        toggleButtons(false);
        loop = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            board.update();
            display.displayBoard(board);
            ++cont;
            generation.setText(String.valueOf(cont));
        }));
        loop.setCycleCount(48);
        loop.play();
    }

    @FXML
    private void onStop(Event evt) {
        toggleButtons(true);
        cont = 0;
        generation.setText(String.valueOf(cont));
        loop.stop();
    }

    @FXML
    private void onOpen(Event evt) {
        Board newBoard = FileHandler.openFromFile(DEFAULT_SIZE);
        if (newBoard != null) {
            board = newBoard;
            createDisplay();
        }
    }

    @FXML
    private void onSave(Event evt) {
        FileHandler.saveToFile(board);
    }

    @FXML
    private void onClean(Event evt) {
        board = new Board(DEFAULT_SIZE, DEFAULT_SIZE);
        createDisplay();
    }

    private void toggleButtons(boolean enable) {
        runButton.setDisable(!enable);
        loadButton.setDisable(!enable);
        saveButton.setDisable(!enable);
        cleanButton.setDisable(!enable);
        rootButton.setDisable(!enable);
        stopButton.setDisable(enable);

    }

    private void createBoard(int size) {
        board = new Board(size, size);
        createDisplay();
    }

    private void createDisplay() {
        display = new JavaFXDisplayDriver(board.getSize(), cellSizePx, board);
        base.getChildren().clear();
        base.getChildren().add(new Group(display.getPane()));
    }

    private void attachResizeListener() {
        ChangeListener<Number> sizeListener = (observable, oldValue, newValue) -> {
            int newWidth = newValue.intValue();
            cellSizePx = newWidth / 35;
            createDisplay();
        };
        rootBox.widthProperty().addListener(sizeListener);
        /*ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                System.out.println("Height: " + rootBox.getHeight() + " Width: " + rootBox.getWidth());

        rootBox.widthProperty().addListener(stageSizeListener);
        rootBox.heightProperty().addListener(stageSizeListener);*/
    }
}