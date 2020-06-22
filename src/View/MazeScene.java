package View;

import Model.Options;
import ViewModel.MyViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MazeScene implements IView, Initializable {
    private MyViewModel viewModel;
    private int[][] maze;
    private int charCol, charRow;
    public MazeDisplayer mazeDisplayer;
    public Pane pane;
    private MediaPlayer solMusic;
    private MediaPlayer moveMusic;
    private int steps;
    ObjectProperty<Point2D> mouseLocation = new SimpleObjectProperty<>();

    @Override
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        maze = this.viewModel.getMaze();
        charCol = this.viewModel.getCharCol();
        charRow = this.viewModel.getCharRow();
        mazeDisplayer.setMaze(maze, charRow, charCol, this.viewModel.getGoalRow(), this.viewModel.getGoalCol());
        mazeDisplayer.draw();
    }

    @Override
    public void onShowScreen() {
        steps = 0;
        pane.widthProperty().addListener((observable, oldValue, newValue) -> viewModel.setSceneWidth((double)newValue));
        pane.heightProperty().addListener((observable, oldValue, newValue) -> viewModel.setSceneHigh((double)newValue));
        pane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if(event.isControlDown())
                    scroll(event);
            }
        });
        pane.setOnMousePressed(event ->
                mouseLocation.set(new Point2D(event.getScreenX(), event.getScreenY())));

        pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                pane.setCursor(Cursor.HAND);
            }});
        pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (mouseLocation.get() != null) {
                    double x = mouseEvent.getScreenX();
                    double deltaX = x - mouseLocation.get().getX();
                    double y = mouseEvent.getScreenY();
                    double deltaY = y - mouseLocation.get().getY();
                    pane.setTranslateX(pane.getTranslateX() + deltaX);
                    pane.setTranslateY(pane.getTranslateY() + deltaY);
                    mouseLocation.set(new Point2D(x, y));
                }
            }
            });
    }
    private void scroll(ScrollEvent event){
        double factor = 1.05;
        double wheelDirection = event.getDeltaY();
        if(wheelDirection < 0){
            factor = 0.95;
        }
        Scale newScale = new Scale();
        newScale.setX(pane.getScaleX() * factor);
        newScale.setY(pane.getScaleY() * factor);
        newScale.setPivotX(event.getX());
        newScale.setPivotY(event.getY());
        pane.getTransforms().add(newScale);
        event.consume();
        }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyViewModel) {
            if (arg instanceof int[][]) {
                //GenerateMaze
                maze = viewModel.getMaze();
                charCol = viewModel.getCharCol();
                charRow = viewModel.getCharRow();
                mazeDisplayer.setMaze(maze, charRow, charCol, viewModel.getGoalRow(), viewModel.getGoalCol());
                mazeDisplayer.draw();
            } else if (arg instanceof List) {
                //Solution
                mazeDisplayer.setSolution((List) arg);
                mazeDisplayer.draw();
            } else {
                //move
                steps++;
                if(moveMusic == null){
                    String musicFile = "./resources/Songs/move.mp3";     // For example
                    Media sound = new Media(new File(musicFile).toURI().toString());
                    moveMusic = new MediaPlayer(sound);
                }
                if(Options.getOptions().getSoundsMode()){
                    moveMusic.play();
                    moveMusic.seek(Duration.ZERO);
                }
                mazeDisplayer.set_player_position(viewModel.getCharRow(), viewModel.getCharCol());
                mazeDisplayer.draw();
                if (viewModel.getCharRow() == viewModel.getGoalRow() && viewModel.getCharCol() == viewModel.getGoalCol()) {
                    //ReachGoal
                    if (Options.getOptions().getSoundsMode()) {
                        Main.stopMusic();
                        String musicFile = "./resources/Songs/reachGoal.mp3";     // For example
                        Media sound = new Media(new File(musicFile).toURI().toString());
                        solMusic = new MediaPlayer(sound);
                        solMusic.play();
                        solMusic.setOnEndOfMedia(new Runnable() {
                            @Override
                            public void run() {
                                Main.startMusic();
                            }
                        });
                    }
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FinishMessage.fxml"));
                        Parent root = fxmlLoader.load();
                        finishMessage mainView = (finishMessage) fxmlLoader.getController();
                        Scene finishMessageScene = new Scene(root, 400, 400);
                        mainView.setSteps(steps);
                        mainView.setMazeSize(maze.length, maze[0].length);
                        Stage popsStage = new Stage();
                        mainView.setMazeScene(this);
                        popsStage.setResizable(false);
                        popsStage.setScene(finishMessageScene);
                        popsStage.show();
                    }catch (Exception e) {}
                }
            }
        }
    }

    public void redraw() {
        this.mazeDisplayer.draw();
    }

    public void BackToMainScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        IView mainView = fxmlLoader.getController();
        viewModel.deleteObserver(this);
        viewModel.addObserver(mainView);
        mainView.setViewModel(viewModel);
        Main.changeScene(root, mainView);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void SolveMaze(ActionEvent actionEvent) {
        viewModel.solve();
    }

    public void saveMaze(ActionEvent actionEvent) throws IOException {
        Dialog<?> chooseSave = new Dialog<>();
        chooseSave.setResizable(true);

        Window window = chooseSave.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());

        chooseSave.getDialogPane().setStyle("-fx-background-image: url(\"/BackGround/woodbackground.jpg\");"
                + "-fx-background-repeat: stretch;"
                + "-fx-background-size: 100% 100% ;"
                + "-fx-background-position: center center;"
                + "-fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);");
        chooseSave.setTitle("Save Game");

        Button[] buttonTypes = viewModel.getButtons();
        StringProperty whereSave = new SimpleStringProperty("");
        for (int i = 0; i < buttonTypes.length; i++) {
            int finalI = i;
            buttonTypes[i].setOnAction(event -> {
                whereSave.setValue(String.valueOf(finalI));
                window.hide();
                buttonTypes[finalI].setText("Saved " + new Date().toString());
            });
        }
        VBox vBox = new VBox();
        Label text = new Label("Where to save?");
        text.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75),2,0,0,1);"+
                "-fx-text-fill: white;"+
                "-fx-font-weight: bold;"+
                "-fx-font-size: 1.6em;");
        vBox.getChildren().add(text);
        vBox.getChildren().addAll(buttonTypes);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        chooseSave.getDialogPane().setContent(vBox);
        chooseSave.showAndWait();
        if (!whereSave.get().equals(""))
            viewModel.saveMaze(whereSave.get());
        whereSave.setValue("");
    }

    public void closeWindow(ActionEvent actionEvent) {
        viewModel.preClosing();
        System.exit(0);
    }

    public void startAgain(){
        viewModel.generateMaze(viewModel.getMaze().length, viewModel.getMaze()[0].length);
        steps = 0;
    }
}
