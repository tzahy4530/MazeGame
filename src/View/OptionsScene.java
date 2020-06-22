package View;

import Model.Options;
import ViewModel.MyViewModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class OptionsScene implements IView, Initializable {
    @FXML
    public ChoiceBox sounds;
    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    private MyViewModel viewModel;
    public Pane optionPane;
    private MediaPlayer mouseClick;

    public OptionsScene() {

    }

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
        optionPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> listnodes = optionPane.getChildren();
            for (Node n : listnodes
            ) {
                viewModel.setSceneWidth((double)newValue);
                n.setLayoutX(optionPane.getWidth() / (double) oldValue * n.getLayoutX());
            }
        });
        optionPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Node> listnodes = optionPane.getChildren();
            for (Node n : listnodes
            ) {
                viewModel.setSceneHigh((double)newValue);
                n.setLayoutY(optionPane.getHeight() / (double) oldValue * n.getLayoutY());

            }
        });
        if (viewModel.getSceneHigh() != 526 || viewModel.getSceneWidth() != 1200) {
            for (Node n : optionPane.getChildren()) {
                n.setLayoutX(viewModel.getSceneWidth() / 1200 * n.getLayoutX());
                n.setLayoutY(viewModel.getSceneHigh() / 526 * n.getLayoutY());
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void applyOptions(ActionEvent actionEvent) {
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
        Options options = Options.getOptions();
        options.setMazeSize(Integer.valueOf(textField_mazeRows.getText()), Integer.valueOf(textField_mazeColumns.getText()));
        boolean soundsMode = true;
        switch (sounds.getValue().toString()) {
            case "ON":
                soundsMode = true;
                Main.startMusic();
                break;
            case "OFF":
                soundsMode = false;
                Main.stopMusic();
                break;
        }
        options.setSoundsMode(soundsMode);
    }

    public void choosePlayer() {
        if(Options.getOptions().getSoundsMode()){
            mouseClick.play();
            mouseClick.seek(Duration.ZERO);
        }
        Image kenny = null, cartman = null, kyle = null, stan = null;
        try {
            kenny = new Image(new FileInputStream("./resources/Pictures/kenny.png"));
            cartman = new Image(new FileInputStream("./resources/Pictures/cartman.png"));
            kyle = new Image(new FileInputStream("./resources/Pictures/kyle.png"));
            stan = new Image(new FileInputStream("./resources/Pictures/stan.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Dialog<?> choosePlayer = new Dialog<>();
        Window window = choosePlayer.getDialogPane().getScene().getWindow();
        StackPane stackPane = new StackPane(new ImageView(
                new Image(getClass().getResourceAsStream("/BackGround/woodbackground.jpg"))));
        choosePlayer.getDialogPane().setStyle("-fx-background-image: url(\"/BackGround/woodbackground.jpg\");");
        //stackPane.setPrefSize(24, 24);
        //choosePlayer.setGraphic(stackPane);

        window.setOnCloseRequest(event -> choosePlayer.hide());


        GridPane pane = new GridPane();
        CharacterButton cartmanButton = new CharacterButton(cartman, "cartman"), kyleButton = new CharacterButton(kyle, "kyle"), stanButton = new CharacterButton(stan, "stan"), kennyButton = new CharacterButton(kenny, "kenny");

        pane.add(cartmanButton, 0, 0);
        pane.add(stanButton, 1, 0);
        pane.add(kennyButton, 2, 0);
        pane.add(kyleButton, 3, 0);
        pane.setOnMouseClicked(event -> window.hide());
        pane.setAlignment(Pos.CENTER);
        pane.setVgap(20);
        choosePlayer.setTitle("Please choose character: ");
        choosePlayer.getDialogPane().setContent(pane);
        choosePlayer.show();
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
}
