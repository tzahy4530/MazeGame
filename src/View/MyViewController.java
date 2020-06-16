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

import java.net.URL;
import java.util.*;

public class MyViewController implements IView, Observer, Initializable {
    private MyViewModel viewModel;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    int[][] maze;
    int charCol, charRow;


    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel){
            if(arg instanceof int[][]){
                //GenerateMaze
                mazeDisplayer = new MazeDisplayer(viewModel.getMaze(),viewModel.getCharRow(),viewModel.getCharCol(),
                        viewModel.getGoalRow(),viewModel.getGoalCol());
                mazeDisplayer.draw();
            }
            else if(arg instanceof List){
                //Solution

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

    public void drawMaze(){}

}
