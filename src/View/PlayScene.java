package View;

import Model.Options;
import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class PlayScene implements IView, Initializable {
    private MyViewModel viewModel;
    private boolean isLoad;
    public Button loadButton;
    public Pane playViewPane;
    private MediaPlayer mouseClick;

    @Override
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public PlayScene() {
    }

    @Override
    public void onShowScreen() {
        if(mouseClick == null){
            String musicFile = "./resources/Songs/mouseClick.mp3";     // For example
            Media sound = new Media(new File(musicFile).toURI().toString());
            mouseClick = new MediaPlayer(sound);
        }

        loadButton.setDisable(!viewModel.hasSavedMaze());
        playViewPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> listnodes = playViewPane.getChildren();
            for (Node n : listnodes
            ) {
                viewModel.setSceneWidth((double) newValue);
                n.setLayoutX(playViewPane.getWidth() / (double) oldValue * n.getLayoutX());
            }
        });
        playViewPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> listnodes = playViewPane.getChildren();
            for (Node n : listnodes
            ) {
                viewModel.setSceneHigh((double) newValue);
                n.setLayoutY(playViewPane.getHeight() / (double) oldValue * n.getLayoutY());

            }
        });
        if (viewModel.getSceneHigh() != 526 || viewModel.getSceneWidth() != 1200) {
            for (Node n : playViewPane.getChildren()) {
                n.setLayoutX(viewModel.getSceneWidth() / 1200 * n.getLayoutX());
                n.setLayoutY(viewModel.getSceneHigh() / 526 * n.getLayoutY());
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Boolean) {
            isLoad = true;
            loadButton.setDisable(true);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isLoad = false;


    }

    public void newGame() throws IOException {
        if(Options.getOptions().getSoundsMode()){
            mouseClick.play();
            mouseClick.seek(Duration.ZERO);
        }

        //moving MazeInformation to ViewModel
        int[] mazeSize = Options.getOptions().getMazeSize();
        if (!isLoad)
            viewModel.generateMaze(mazeSize[0], mazeSize[1]);

        //try

        //moving to MazeScene
        FXMLLoader fxmllLoader = new FXMLLoader(getClass().getResource("MazeScene.fxml"));
        Parent mazeScene = fxmllLoader.load();
        IView mazeView = fxmllLoader.getController();
        viewModel.addObserver(mazeView);
        viewModel.deleteObserver(this);
        mazeView.setViewModel(viewModel);
        Main.changeScene(mazeScene, mazeView);
        ((MazeScene) mazeView).redraw();
        isLoad = false;

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

    public void loadGame(ActionEvent actionEvent) {
        if(Options.getOptions().getSoundsMode()){
            mouseClick.play();
            mouseClick.seek(Duration.ZERO);
        }
        Dialog<?> chooseLoad = new Dialog<>();
        chooseLoad.setResizable(true);

        Window window = chooseLoad.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());

        chooseLoad.getDialogPane().setStyle("-fx-background-image: url(\"/BackGround/woodbackground.jpg\"); "
        + "-fx-background-repeat: stretch;"
        + "-fx-background-size: 100% 100% ;"
        + "-fx-background-position: center center;"
        + "-fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);");

         chooseLoad.setTitle("Loade Game");
        //chooseLoad.setHeaderText("Please choose where to Load to maze");
        Button[] buttonTypes = viewModel.getButtons();
        StringProperty whereLoad = new SimpleStringProperty();

        for (int i = 0; i < buttonTypes.length; i++) {
            int finalI = i;
            buttonTypes[i].setStyle("-fx-font-weight: bold; -fx-background-insets: 0,0 0 5 0,0 0 6 0,0 0 7 0; -fx-text-fill: white;"
            + "-fx-background-radius: 8; " + "-fx-background-color: linear-gradient(from 0% 93% to 0% 100%,#a34313 0%,#903b12 100%),#9d4024,#d86e3a, radial-gradient(center 50% 50%, radius 100%,#d96e3a,#c54e2c); "
                    + "-fx-effect: dropshadow(gaussian, rgba(0.2,0.2,0.2,0.2),1,1,1,1);" + "-fx-font-size: 1.1em;");

            buttonTypes[i].setOnAction(event -> {
                whereLoad.setValue(String.valueOf(finalI));
                window.hide();
                try {
                    viewModel.loadMaze(whereLoad.get());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(null);
                    alert.setHeaderText("File not found.\nplease select another maze");
                    alert.show();
                }
            });
        }
        VBox vBox = new VBox();
        Label text = new Label("Load which file?");
        text.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75),2,0,0,1);"+
        "-fx-font-weight: bold;"+ "-fx-text-fill: white;"+
        "-fx-font-size: 1.6em;");
        vBox.getChildren().add(text);
        vBox.getChildren().addAll(buttonTypes);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        chooseLoad.getDialogPane().setContent(vBox);
        chooseLoad.showAndWait();
        if (isLoad) {
            try {
                this.newGame();
            } catch (Exception e) {
            }
        }
        isLoad=false;

    }


}
