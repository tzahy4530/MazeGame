package View;

import Model.Options;
import ViewModel.MyViewModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
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
    private MediaPlayer mouseClick;
    public AnchorPane mainPane;


    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onShowScreen() {
        if(mouseClick == null){
            String musicFile = "./resources/Songs/mouseClick.mp3";     // For example
            Media sound = new Media(new File(musicFile).toURI().toString());
            mouseClick = new MediaPlayer(sound);
        }
        mainPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> listnodes = mainPane.getChildren();
            for (Node n : listnodes
            ) {
                viewModel.setSceneWidth((double)newValue);
                n.setLayoutX(mainPane.getWidth() / (double) oldValue * n.getLayoutX());
            }
        });
        mainPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> listnodes = mainPane.getChildren();
            for (Node n : listnodes
            ) {
                viewModel.setSceneHigh((double)newValue);
                n.setLayoutY(mainPane.getHeight() / (double) oldValue * n.getLayoutY());

            }
        });
        if (viewModel.getSceneHigh() != 526 || viewModel.getSceneWidth() != 1200) {
            for (Node n : mainPane.getChildren()) {
                n.setLayoutX(viewModel.getSceneWidth() / 1200 * n.getLayoutX());
                n.setLayoutY(viewModel.getSceneHigh() / 526 * n.getLayoutY());
            }
        }
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
        if(Options.getOptions().getSoundsMode()){
            mouseClick.play();
            mouseClick.seek(Duration.ZERO);
        }
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


    public void OptionsSceneChanger(ActionEvent actionEvent) throws IOException {
        if(Options.getOptions().getSoundsMode()){
            mouseClick.play();
            mouseClick.seek(Duration.ZERO);
        }
        FXMLLoader fxmllLoader = new FXMLLoader(getClass().getResource("OptionView.fxml"));
        Parent optionsScene = fxmllLoader.load();
        IView optionsView = fxmllLoader.getController();
        viewModel.addObserver(optionsView);
        viewModel.deleteObserver(this);
        optionsView.setViewModel(viewModel);
        Main.changeScene(optionsScene, optionsView);

    }

    public void PlaySceneChanger(ActionEvent actionEvent) throws IOException {
        if(Options.getOptions().getSoundsMode()){
            mouseClick.play();
            mouseClick.seek(Duration.ZERO);
        }
        FXMLLoader fxmllLoader = new FXMLLoader(getClass().getResource("PlayView.fxml"));
        Parent playScene = fxmllLoader.load();
        IView playView = fxmllLoader.getController();
        viewModel.addObserver(playView);
        viewModel.deleteObserver(this);
        playView.setViewModel(viewModel);
        Main.changeScene(playScene, playView);

    }


    public void aboutClicked(ActionEvent actionEvent) {

        if(Options.getOptions().getSoundsMode()){
            mouseClick.play();
            mouseClick.seek(Duration.ZERO);
        }
        viewModel.getAboutInformtion().show();
    }

    public void helpSceneChanger(ActionEvent actionEvent) throws IOException {
        if(Options.getOptions().getSoundsMode()){
            mouseClick.play();
            mouseClick.seek(Duration.ZERO);
        }
        FXMLLoader fxmllLoader = new FXMLLoader(getClass().getResource("HelpMessage.fxml"));
        Parent playScene = fxmllLoader.load();
        IView helpView = fxmllLoader.getController();
        viewModel.addObserver(helpView);
        viewModel.deleteObserver(this);
        helpView.setViewModel(viewModel);
        Main.changeScene(playScene, helpView);
    }


    public void exitProgram(ActionEvent actionEvent) {
        if(Options.getOptions().getSoundsMode()){
            mouseClick.play();
            mouseClick.seek(Duration.ZERO);
        }
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
