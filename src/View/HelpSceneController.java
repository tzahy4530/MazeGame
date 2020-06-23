package View;

import Model.Options;
import ViewModel.MyViewModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class HelpSceneController implements IView, Initializable {
    private MyViewModel viewModel;
    public Pane helpMainPane;
    private MediaPlayer mouseClick;

    @Override
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
        helpMainPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> listnodes = helpMainPane.getChildren();
            for (Node n : listnodes
            ) {
                viewModel.setSceneWidth((double)newValue);
                n.setLayoutX(helpMainPane.getWidth() / (double) oldValue * n.getLayoutX());
            }
        });
        helpMainPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> listnodes = helpMainPane.getChildren();
            for (Node n : listnodes
            ) {
                viewModel.setSceneHigh((double)newValue);
                n.setLayoutY(helpMainPane.getHeight() / (double) oldValue * n.getLayoutY());

            }
        });
        if (viewModel.getSceneHigh() != 526 || viewModel.getSceneWidth() != 1200) {
            for (Node n : helpMainPane.getChildren()) {
                n.setLayoutX(viewModel.getSceneWidth() / 1200 * n.getLayoutX());
                n.setLayoutY(viewModel.getSceneHigh() / 526 * n.getLayoutY());
            }
        }
    }

    public void BackToMainScene(ActionEvent actionEvent) throws IOException {
        if(Options.getOptions().getSoundsMode()){
            mouseClick.play();
            mouseClick.seek(Duration.ZERO);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        IView mainView = fxmlLoader.getController();
        viewModel.deleteObserver(this);
        viewModel.addObserver(mainView);
        mainView.setViewModel(viewModel);
        Main.changeScene(root, mainView);
    }

    @Override
    public void update(Observable o, Object arg) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
