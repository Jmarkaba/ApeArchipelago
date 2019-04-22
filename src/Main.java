import GUIComponents.EndScreen;
import GUIComponents.PrimaryView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        final String START_SCENE = "beach";

        PrimaryView pv = new PrimaryView(START_SCENE);
        Scene scene = new Scene(pv);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        KeyFrame update_kf = new KeyFrame(Duration.millis(160),(event) -> {
            pv.updateAll(1);
            pv.renderView();
            if(pv.isDone()) {
                endGame(primaryStage);
            }
        });
        Timeline timeline = new Timeline(update_kf);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void endGame(Stage stage) {
        stage.setScene(new Scene(new EndScreen(), 1000, 800));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
