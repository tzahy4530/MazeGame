package View;

import Model.Options;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

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

    @Override
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onShowScreen() {

    }

    @Override
    public void update(Observable o, Object arg) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void applyOptions(ActionEvent actionEvent) {
        if (textField_mazeRows.getText().length()<2 ||textField_mazeColumns.getText().length()<2) {
            Alert alet = new Alert(Alert.AlertType.ERROR);
            alet.setTitle("Values");
            alet.setContentText("You have to fell column and rows");
            alet.setHeaderText("Feel values please");
            alet.show();
            return;
        }
        Options options = Options.getOptions();
        options.setMazeSize(Integer.valueOf(textField_mazeRows.getText()),Integer.valueOf(textField_mazeColumns.getText()));
        boolean soundsMode = true;
        switch (sounds.getValue().toString()){
            case "ON":
                soundsMode = true;
                break;
            case "OFF":
                soundsMode = false;
                break;
        }
        options.setSoundsMode(soundsMode);
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
}
