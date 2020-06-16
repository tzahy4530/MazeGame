package View;

import ViewModel.MyViewModel;
import algorithms.search.AState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.*;

public class MyViewController implements IView, Observer, Initializable {
    private MyViewModel viewModel;
    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    int[][] maze;
    int charCol, charRow;

    public void keyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel){
            if(arg instanceof int[][]){
                //GenerateMaze
                maze = viewModel.getMaze();
                charCol = viewModel.getCharCol();
                charRow = viewModel.getCharRow();
                mazeDisplayer.setMaze(maze,charRow,charCol,viewModel.getGoalRow(),viewModel.getGoalCol());
                mazeDisplayer.draw();
            }
            else if(arg instanceof List){
                //Solution
                mazeDisplayer.setSolution((List) arg);
                mazeDisplayer.draw();
            }
            else{
                //move
                mazeDisplayer.set_player_position(viewModel.getCharRow(),viewModel.getCharCol());
                mazeDisplayer.draw();
                if(viewModel.getCharRow()==viewModel.getGoalRow() && viewModel.getCharCol()==viewModel.getGoalCol()){
                    //ReachGoal
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void donotWorkAlert(ActionEvent actionEvent) {
        Alert newalert = new Alert(Alert.AlertType.INFORMATION);
        newalert.setContentText("we need to start....");
        newalert.setHeaderText("START!!");
        newalert.show();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void startWorking(ActionEvent actionEvent) {
        Alert newalert = new Alert(Alert.AlertType.INFORMATION);
        newalert.setContentText("Lets go Lets GO!!!");
        newalert.setHeaderText("START!!");
        ButtonType newbutton = new ButtonType("OK Lets GO ! ");
        newalert.setTitle("GO-GO-GO-GO-GO");
        newalert.show();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void generateMaze()
    {
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        viewModel.generateMaze(rows,cols);

    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
}
