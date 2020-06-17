package View;

import Model.Options;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class PlayScene implements IView, Initializable {
    private MyViewModel viewModel;

    @Override
    public void setViewModel(MyViewModel viewModel) { this.viewModel = viewModel;}

    @Override
    public void update(Observable o, Object arg) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void newGame() throws IOException {

        //moving MazeInformation to ViewModel
        int[] mazeSize = Options.getOptions().getMazeSize();
        viewModel.generateMaze(mazeSize[0], mazeSize[1]);

        //moving to MazeScene
        FXMLLoader fxmllLoader=new FXMLLoader(getClass().getResource("MazeScene.fxml"));
        Parent mazeScene = fxmllLoader.load();
        IView mazeView=fxmllLoader.getController();
        viewModel.addObserver(mazeView);
        viewModel.deleteObserver(this);
        mazeView.setViewModel(viewModel);
        Main.changeScene(new Scene(mazeScene, 1280,600));
        ((MazeScene)mazeView).redraw();
    }

    public void BackToMainScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        IView mainView=fxmlLoader.getController();
        viewModel.deleteObserver(this);
        viewModel.addObserver(mainView);
        mainView.setViewModel(viewModel);
        Main.changeScene(new Scene(root));
    }

    public void loadGame(ActionEvent actionEvent) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Select saved game");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze Files","*.maze"));
        File selectedMaze=fileChooser.showOpenDialog(Main.getWindow());
    }
}
