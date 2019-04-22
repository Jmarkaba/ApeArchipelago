package GUIComponents;

import Entities.Entity;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class GamePane extends StackPane {

    private PrimaryView parent;

    private Canvas backgroundCanvas = new Canvas();
    private Pane playerPane = new Pane();
    private Canvas foregroundCanvas = new Canvas();
    private Pane foregroundPlayerPane = new Pane();

    public GamePane(PrimaryView main) {
        this.parent = main;
        clear();
    }

    public void setDimensions(double width, double height) {
        super.setWidth(width);
        super.setHeight(height);
        backgroundCanvas.setWidth(width);
        backgroundCanvas.setHeight(height);
        playerPane.setMinWidth(width); playerPane.setPrefWidth(width);
        playerPane.setMinHeight(height); playerPane.setPrefHeight(height);
        foregroundCanvas.setWidth(width);
        foregroundCanvas.setHeight(height);
        foregroundPlayerPane.setMinWidth(width); foregroundPlayerPane.setPrefWidth(width);
        foregroundPlayerPane.setMinHeight(height); foregroundPlayerPane.setPrefHeight(height);
    }

    public void generateScene(String backgroundFile, String foregroundFile) {
        if(backgroundFile != null) {
            Image background = new Image(backgroundFile, super.getWidth(), super.getHeight(), false, false);
            this.backgroundCanvas.getGraphicsContext2D().drawImage(background, 0, 0);
        }
        if(foregroundFile != null) {
            Image foreground = new Image(foregroundFile, super.getWidth(), super.getHeight(), false, false);
            this.foregroundCanvas.getGraphicsContext2D().drawImage(foreground, 0, 0);
        }
    }

    public void clear() {
        super.getChildren().clear();
        backgroundCanvas = new Canvas(backgroundCanvas.getWidth(), backgroundCanvas.getHeight());
        foregroundCanvas = new Canvas(foregroundCanvas.getWidth(), foregroundCanvas.getHeight());
        backgroundCanvas.setMouseTransparent(true);
        foregroundCanvas.setMouseTransparent(true);
        playerPane.getChildren().clear();
        playerPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        playerPane.setOnMouseClicked((event -> {
            parent.getPlayer().setTarget((Entity) null);
            if(parent.getCommand() != CommandBox.Command.WALK)
                parent.getCommandBox().clearCommand();
            else {
                double x = event.getX();
                double y = event.getY();
                Entity e = new Entity() {
                    @Override
                    public void render(PrimaryView main) { }
                };
                e.setPosition(x, y);
                e.setDimensions(3,3);
                parent.getPlayer().setTarget(e);
            }
        }));
        foregroundPlayerPane.getChildren().clear();
        foregroundPlayerPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        foregroundPlayerPane.setMouseTransparent(true);
        setDimensions(super.getWidth(), super.getHeight());
        super.getChildren().addAll(backgroundCanvas, foregroundCanvas, playerPane, foregroundPlayerPane);
    }

    public Canvas getCanvas(boolean isBackground) {
        return isBackground ? backgroundCanvas : foregroundCanvas;
    }

    public Pane getPlayerPane(boolean isBackground) {
        return isBackground ? playerPane : foregroundPlayerPane;
    }
}
