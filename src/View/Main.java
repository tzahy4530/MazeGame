package View;

import Model.IModel;
import Model.MazeSaverAndLoader;
import Model.MyModel;
import Server.*;
import ViewModel.MyViewModel;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Main extends Application {
    private Server generateServer, solverServer;
    private static Stage window;
    private static MyViewModel viewModel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        loadingScreen();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished( event -> startApp()
        );
        delay.play();
    }

    public static Stage getWindow() {
        return window;
    }

    private void startApp(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
            Parent root = fxmlLoader.load();
            window.setTitle("Maze Game");
            IModel model = new MyModel(new MazeSaverAndLoader());
            viewModel = new MyViewModel(model);
            viewModel.setSceneHigh(526);
            viewModel.setSceneWidth(1200);
            window.setScene(new Scene(root, viewModel.getSceneWidth(), viewModel.getSceneHigh()));
            IView view = fxmlLoader.getController();
            view.setViewModel(viewModel);
            viewModel.addObserver(view);
            generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
            generateServer.start();
            solverServer = new Server(5401, 10000, new ServerStrategySolveSearchProblem());
            solverServer.start();
            view.onShowScreen();
            window.show();
            window.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        } catch (Exception e){}
    }

    private void loadingScreen(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loadingScreen.fxml"));
            Parent root = fxmlLoader.load();
            window.setScene(new Scene(root,1200,506));
            window.show();
        }
        catch (Exception e){}
    }

    private <T extends Event> void closeWindowEvent(T t) {
        solverServer.stop();
        generateServer.stop();
        viewModel.preClosing();

    }

    public static void changeScene(Parent newScene, IView view) {
        // try to center the Scene
//        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
//        window.setX((screenBounds.getWidth()) / 2);
//        window.setY((screenBounds.getHeight()) / 2);
        window.setScene(new Scene(newScene,viewModel.getSceneWidth(),viewModel.getSceneHigh()));
        view.onShowScreen();
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
