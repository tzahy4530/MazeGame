package View;

import Model.IModel;
import Model.MazeSaverAndLoader;
import Model.MyModel;
import Model.Options;
import Server.*;
import ViewModel.MyViewModel;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.File;

public class Main extends Application {
    private Server generateServer, solverServer;
    private static Stage window;
    private static MyViewModel viewModel;
    private MediaPlayer startSong;
    private static MediaPlayer backgroundSong;

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (Options.getOptions().getSoundsMode()) {
            String musicFile = "./resources/Songs/startGame.mp3";
            String musicBGFile = "./resources/Songs/backgroundMusic.mp3";
            Media sound = new Media(new File(musicFile).toURI().toString());
            Media soundBG = new Media(new File(musicBGFile).toURI().toString());
            startSong = new MediaPlayer(sound);
            backgroundSong = new MediaPlayer(soundBG);
            startSong.play();
            startSong.setOnEndOfMedia(new Runnable() {
              @Override
              public void run(){
                  backgroundSong.play();
              }
            });
            backgroundSong.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    backgroundSong.seek(Duration.ZERO);
                    backgroundSong.play();
                }
            });
        }
        window = primaryStage;
        loadingScreen();
        PauseTransition delay = new PauseTransition(Duration.seconds(2.5));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoadingScreen.fxml"));
            Parent root = fxmlLoader.load();
            window.setScene(new Scene(root,1200,526));
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
        window.setScene(new Scene(newScene,viewModel.getSceneWidth(),viewModel.getSceneHigh()));
        view.onShowScreen();
        window.show();
    }

    public static void stopMusic(){
        backgroundSong.seek(Duration.seconds(10));
        backgroundSong.pause();
    }

    public static void startMusic(){
        backgroundSong.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
