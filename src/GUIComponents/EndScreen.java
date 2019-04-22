package GUIComponents;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class EndScreen extends Pane {

    public static final Background BLACK_BACKGROUND =
            new Background(new BackgroundFill(Color.BLACK, null, null));

    public EndScreen() {
        this.setBackground(BLACK_BACKGROUND);
        Label l = new Label("YOU WIN!!!!");
        l.setTextFill(Color.WHITE);
        l.setFont(Font.font("Comic Sans", FontWeight.EXTRA_BOLD, 40));
        l.setLayoutX(this.getWidth()/2);
        l.setLayoutY(this.getHeight()/2);
        this.getChildren().add(l);
    }
}
