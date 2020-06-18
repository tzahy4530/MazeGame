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
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class PlayScene implements IView, Initializable {
    private MyViewModel viewModel;
    private boolean isLoad;

    @Override
    public void setViewModel(MyViewModel viewModel) { this.viewModel = viewModel;}

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Boolean){
            System.out.println("update");
            isLoad=true;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isLoad=false;
    }

    public void newGame() throws IOException {

        //moving MazeInformation to ViewModel
        int[] mazeSize = Options.getOptions().getMazeSize();
        System.out.println("Load = "+isLoad);
        if (!isLoad)
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
        isLoad=false;
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

    public void loadGame(ActionEvent actionEvent) throws Exception {
//        FileChooser fileChooser=new FileChooser();
//        fileChooser.setTitle("Select saved game");
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze Files","*.maze"));
//        File selectedMaze=fileChooser.showOpenDialog(Main.getWindow());
//        if(selectedMaze==null) return;
//        viewModel.loadMazeFromFile(selectedMaze);
        Dialog<?> chooseLoad = new Dialog<>();
        chooseLoad.setResizable(true);

        Window window = chooseLoad.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());

        chooseLoad.setTitle("Choose Load option");
        chooseLoad.setHeaderText("Please choose where to Load to maze");

        Button[] buttonTypes = new Button[10];
        StringProperty whereLoad = new SimpleStringProperty();
        for (int i = 0; i < buttonTypes.length; i++) {
            buttonTypes[i] = new Button("save " + (i + 1));
            int finalI = i;
            buttonTypes[i].setOnAction(event -> {
                whereLoad.setValue(String.valueOf(finalI));
                window.hide();
                System.out.println(finalI);
            });
        }
        VBox vBox = new VBox();
        vBox.getChildren().addAll(buttonTypes);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        chooseLoad.getDialogPane().setContent(vBox);
        chooseLoad.showAndWait();
        viewModel.loadMaze(whereLoad.get());
    }

//    public void saveGame(ActionEvent actionEvent) throws IOException {
//        FileChooser fileChooser=new FileChooser();
//        fileChooser.setTitle("Select saved game");
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze Files","*.maze"));
//        File selectedMaze=fileChooser.showSaveDialog(Main.getWindow());
//        viewModel.saveMaze(selectedMaze);
//    }
}
