package View;

import Model.IModel;
import Model.MyModel;
import Server.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static public Server server;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 600));
        IModel model= new MyModel();
        MyViewModel viewModel=new MyViewModel(model);
        IView view=fxmlLoader.getController();
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        server = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        Main.server.start();
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
