package View;

import ViewModel.MyViewModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MyViewController implements IView, Observer, Initializable {
    private MyViewModel viewModel;
    public ComboBox sounds;
    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    public MazeDisplayer mazeDisplayer;
    private int[][] maze;
    private int charCol, charRow;
    public AnchorPane trydosome;


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;


    }

    @Override
    public void onShowScreen() {
        trydosome.widthProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> listnodes = trydosome.getChildren();
            for (Node n : listnodes
            ) {
                viewModel.setSceneWidth((double)newValue);
                n.setLayoutX(trydosome.getWidth() / (double) oldValue * n.getLayoutX());
            }
        });
        trydosome.heightProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> listnodes = trydosome.getChildren();
            for (Node n : listnodes
            ) {
                viewModel.setSceneHigh((double)newValue);
                n.setLayoutY(trydosome.getHeight() / (double) oldValue * n.getLayoutY());

            }
        });
        if (viewModel.getSceneHigh() != 526 || viewModel.getSceneWidth() != 1200) {
            System.out.println(trydosome.getHeight()+ " "+ trydosome.getWidth());
            System.out.println(viewModel.getSceneHigh()+" " +viewModel.getSceneWidth());
            for (Node n : trydosome.getChildren()) {
                n.setLayoutX(viewModel.getSceneWidth() / 1200 * n.getLayoutX());
                n.setLayoutY(viewModel.getSceneHigh() / 526 * n.getLayoutY());
            }
        }

    }


//    public void keyPressed(KeyEvent keyEvent) {
//        viewModel.moveCharacter(keyEvent);
//        keyEvent.consume();
//    }

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
                        generateMaze();

                    } else if (result.get() == buttonMenu) {
                        // ... user chose "Menu"

                    }
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void generateMaze() {
        if (textField_mazeRows.getText().length() < 2 || textField_mazeColumns.getText().length() < 2) {
            Alert alet = new Alert(Alert.AlertType.ERROR);
            alet.setTitle("Values");
            alet.setContentText("You have to fell column and rows");
            alet.setHeaderText("Feel values please");
            alet.show();
            return;
        }
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        viewModel.generateMaze(rows, cols);
    }


//    public void MazeSceneChanger(ActionEvent actionEvent) throws IOException {
//        FXMLLoader fxmllLoader=new FXMLLoader(getClass().getResource("MazeScene.fxml"));
//        Parent mazeScene = fxmllLoader.load();
//        IView mazeView=fxmllLoader.getController();
//        viewModel.addObserver(mazeView);
//        viewModel.deleteObserver(this);
//        mazeView.setViewModel(viewModel);
//        Main.changeScene(new Scene(mazeScene, viewModel.getSceneWidth(), viewModel.getSceneHigh()));
//    }

    public void OptionsSceneChanger(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmllLoader = new FXMLLoader(getClass().getResource("OptionView.fxml"));
        Parent optionsScene = fxmllLoader.load();
        IView optionsView = fxmllLoader.getController();
        viewModel.addObserver(optionsView);
        viewModel.deleteObserver(this);
        optionsView.setViewModel(viewModel);
        Main.changeScene(optionsScene, optionsView);

    }

    public void PlaySceneChanger(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmllLoader = new FXMLLoader(getClass().getResource("PlayView.fxml"));
        Parent playScene = fxmllLoader.load();
        IView playView = fxmllLoader.getController();
        viewModel.addObserver(playView);
        viewModel.deleteObserver(this);
        playView.setViewModel(viewModel);
        Main.changeScene(playScene, playView);

    }


    public void aboutClicked(ActionEvent actionEvent) {
        viewModel.getAboutInformtion().show();
    }

    public void exitProgram(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to exit?");
        ButtonType exit = new ButtonType("I want to Exit"), stay = new ButtonType("I want stay");
        alert.getButtonTypes().setAll(exit, stay);
        Optional<ButtonType> res = alert.showAndWait();
        if (res.get() == exit)
            System.exit(0);
        else
            return;
    }
}
