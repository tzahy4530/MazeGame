package View;

import Model.ClientStrategySolveMaze;
import Model.IModel;
import Model.MyModel;
import Server.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    Server generateServer, solverServer ;
    static Stage window;
    @Override
    public void start(Stage primaryStage) throws Exception{
        window=primaryStage;
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 186, 404));
        IModel model= new MyModel();
        MyViewModel viewModel=new MyViewModel(model);
        IView view=fxmlLoader.getController();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        generateServer.start();
        solverServer = new Server(5401,10000, new ServerStrategySolveSearchProblem());
        solverServer.start();
        primaryStage.show();
        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    public static Stage getWindow() {
        return window;
    }

    private <T extends Event> void closeWindowEvent(T t) {
        solverServer.stop();
        generateServer.stop();
    }

    public static void changeScene(Scene scene) {
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
