package View;

import Model.Options;
import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class PlayScene implements IView, Initializable {
    private MyViewModel viewModel;
    private boolean isLoad;
    public Button loadButton;

    @Override
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onShowScreen() {
        loadButton.setDisable(!viewModel.hasSavedMaze());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Boolean) {
            System.out.println("update");
            isLoad = true;
            loadButton.setDisable(true);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isLoad = false;


    }

    public void newGame() throws IOException {

        //moving MazeInformation to ViewModel
        int[] mazeSize = Options.getOptions().getMazeSize();
        System.out.println("Load = " + isLoad);
        if (!isLoad)
            viewModel.generateMaze(mazeSize[0], mazeSize[1]);

        //moving to MazeScene
        FXMLLoader fxmllLoader = new FXMLLoader(getClass().getResource("MazeScene.fxml"));
        Parent mazeScene = fxmllLoader.load();
        IView mazeView = fxmllLoader.getController();
        viewModel.addObserver(mazeView);
        viewModel.deleteObserver(this);
        mazeView.setViewModel(viewModel);
        Main.changeScene(new Scene(mazeScene, viewModel.getSceneWidth(), viewModel.getSceneHigh()));
        ((MazeScene) mazeView).redraw();
        isLoad = false;
    }

    public void BackToMainScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        IView mainView = fxmlLoader.getController();
        viewModel.deleteObserver(this);
        viewModel.addObserver(mainView);
        mainView.setViewModel(viewModel);
        Main.changeScene(new Scene(root,  viewModel.getSceneWidth(), viewModel.getSceneHigh()));
    }

    public void loadGame(ActionEvent actionEvent) {
        Dialog<?> chooseLoad = new Dialog<>();
        chooseLoad.setResizable(true);

        Window window = chooseLoad.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());

        chooseLoad.setTitle("Choose Load option");
        chooseLoad.setHeaderText("Please choose where to Load to maze");

        Button[] buttonTypes = viewModel.getButtons();
        StringProperty whereLoad = new SimpleStringProperty();
        for (int i = 0; i < buttonTypes.length; i++) {
            int finalI = i;
            buttonTypes[i].setOnAction(event -> {
                whereLoad.setValue(String.valueOf(finalI));
                window.hide();
                System.out.println(finalI);
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
        vBox.getChildren().addAll(buttonTypes);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        chooseLoad.getDialogPane().setContent(vBox);
        chooseLoad.showAndWait();

    }


}
