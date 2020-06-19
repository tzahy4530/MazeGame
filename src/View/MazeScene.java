package View;

import Model.Options;
import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Window;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MazeScene implements IView, Initializable {
    private MyViewModel viewModel;
    private int[][] maze;
    private int charCol, charRow;
    public MazeDisplayer mazeDisplayer;

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
                mazeDisplayer.set_player_position(viewModel.getCharRow(), viewModel.getCharCol());
                mazeDisplayer.draw();
                if (viewModel.getCharRow() == viewModel.getGoalRow() && viewModel.getCharCol() == viewModel.getGoalCol()) {
                    //ReachGoal
                    if (Options.getOptions().getSoundsMode()) {
                        String musicFile = "sound.mp3";     // For example
                        Media sound = new Media(new File(musicFile).toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(sound);
                        mediaPlayer.play();
                    }
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Congratulations!");
                    alert.setHeaderText("You have reached the goal.");
                    alert.setContentText("Choose your option.");

                    ButtonType buttonPlayAgain = new ButtonType("Play again");
                    ButtonType buttonMenu = new ButtonType("Menu");
                    alert.getButtonTypes().setAll(buttonPlayAgain, buttonMenu);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonPlayAgain) {
                        // ... user chose "PlayAgain"
                        viewModel.generateMaze(viewModel.getMaze().length, viewModel.getMaze()[0].length);

                    } else if (result.get() == buttonMenu) {
                        // ... user chose "Menu"
                        try {
                            BackToMainScene(new ActionEvent());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
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
        Main.changeScene(new Scene(root));
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

        chooseSave.setTitle("Choose save option");
        chooseSave.setHeaderText("Please choose where to save to maze");

        Button[] buttonTypes = viewModel.getButtons();
        StringProperty whereSave = new SimpleStringProperty();
        for (int i = 0; i < buttonTypes.length; i++) {
            int finalI = i;
            buttonTypes[i].setOnAction(event -> {
                whereSave.setValue(String.valueOf(finalI));
                window.hide();
                System.out.println(finalI);
                buttonTypes[finalI].setText("Saved "+new Date().toString());
            });
        }
        VBox vBox = new VBox();
        vBox.getChildren().addAll(buttonTypes);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        chooseSave.getDialogPane().setContent(vBox);
        chooseSave.showAndWait();
        viewModel.saveMaze(whereSave.get());


    }

    public void closeWindow(ActionEvent actionEvent) {
        viewModel.preClosing();
        System.exit(0);
    }
}
