package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements IView, Observer, Initializable {
    private MyViewModel viewModel;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    int[][] maze;
    int charCol, charRow;

    @Override
    public void update(Observable o, Object arg) {

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
}
