package GUIComponents;

import Entities.Entity;
import Entities.Player;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PrimaryView extends VBox {

    public boolean isDone;

    private EntityView entityView;
    private GamePane gamePane;
    private CommandBox commandBox;
    private Player player;
    private double vHeight, cHeight;

    private static final double DEFAULT_WIDTH = 1000;
    private static final double DEFAULT_HEIGHT = 800;
    private static final String BACKGROUND_FILE = "/background.png";
    private static final String FOREGROUND_FILE = "/foreground.png";

    private double width = DEFAULT_WIDTH;
    private double height = DEFAULT_HEIGHT;
    public String currentScene;

    public PrimaryView (String sceneDirectory) throws IOException {
        gamePane = new GamePane(this);
        commandBox = new CommandBox(this);
        updateDimensions();

        currentScene = sceneDirectory;
        sceneDirectory = "scenes/" + sceneDirectory;
        String gameSceneBack = sceneDirectory+BACKGROUND_FILE;//getClass().getClassLoader().getResource(sceneDirectory + BACKGROUND_FILE).getFile();
        //String gameSceneFront = null;//getClass().getClassLoader().getResource(sceneDirectory + FOREGROUND_FILE).getFile();
        gamePane.generateScene(gameSceneBack, null);

        entityView = new EntityView(this, sceneDirectory);
        player = new Player(this);
        super.getChildren().addAll(gamePane, commandBox);
        renderView();

    }

    private void updateDimensions() {
        vHeight = (height*4)/6.0;
        cHeight = height - vHeight;
        gamePane.setDimensions(width, vHeight);
        commandBox.setDimensions(width, cHeight);
    }

    public void nextScene(String sceneDirectory) throws IOException {
        currentScene = sceneDirectory;
        sceneDirectory = "scenes/" + sceneDirectory; //hard coded I know this is all stupid
        String gameSceneBack = sceneDirectory+BACKGROUND_FILE;
        gamePane.clear();
        gamePane.generateScene(gameSceneBack, null);
        entityView = new EntityView(this, sceneDirectory);
        player.render(this);
        player.setPosition(400,150);
        player.setTarget((Entity) null);
        renderView();
    }

    public void renderView() {
        entityView.renderAll();
        player.render(this);
    }

    public void updateAll(double time) {
        player.update(time);
        entityView.updateAll(time);
        commandBox.update();
    }

    public void endGame() {
        isDone = true;
    }

    public boolean isDone() {
        return isDone;
    }

    public EntityView getEntityView() { return entityView; }

    public Player getPlayer() { return player; }

    public CommandBox getCommandBox() {
        return commandBox;
    }

    public GamePane getGamePane() {
        return gamePane;
    }

    public CommandBox.Command getCommand() {
        return commandBox.getCommand();
    }
}
