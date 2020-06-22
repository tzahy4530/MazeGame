package View;

import ViewModel.MyViewModel;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class finishMessage {
    public Label steps;
    public Label mazeSize;
    public Pane congPane;
    private MazeScene mazeScene;

    public void setMazeScene(MazeScene mazeScene){
        this.mazeScene = mazeScene;
    }

    public void setSteps(int stepsCount){
        steps.setText(((Integer)stepsCount).toString());
    }

    public void setMazeSize(int row, int col){
        mazeSize.setText(((Integer)row).toString()+","+((Integer)col).toString());
    }

    public void playAgain() throws IOException{
        mazeScene.startAgain();
        exit();
    }

    public void back() throws IOException {
        mazeScene.BackToMainScene(new javafx.event.ActionEvent());
        exit();
    }

    public Pane getPane(){return congPane;}

    public void exit(){
        congPane.getScene().getWindow().hide();
    }
}
