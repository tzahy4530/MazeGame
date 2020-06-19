package View;

import Model.Options;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Properties;

public class CharacterButton extends Pane {
    private Image image;
    private String name;

    public CharacterButton(Image image, String name) {
        this.name=name;
        this.image = image;
        this.getChildren().add(new ImageView(this.image));
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStyle("-fx-background-color:#dae7f3;");
                System.out.println("MouseOn");
            }
        });

        this.setOnMouseClicked(event -> Options.getOptions().setCharacter(name));
        this.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                setStyle("-fx-background-color:transparent;");
            }
        });
    }
}
