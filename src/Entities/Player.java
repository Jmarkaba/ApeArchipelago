package Entities;

import GUIComponents.PrimaryView;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;

public class Player extends InteractableEntity {

    private static final String PLAYER_DIRECTORY = "player/";
    private static final String PLAYER_NAME = "Guybrush";

    private static final double SPECIAL_BOUNDING_WIDTH = 5;
    private static final double SPECIAL_BOUNDING_HEIGHT = 5;

    private Label playerDialogue = new Label("");
    private double labelTimer;
    private boolean timing;

    private double feetPositionY, feetPositionX;

    private Entity target;

    public Player(PrimaryView main) throws IOException {
        super(main, (InteractableEntity) main.getEntityView().generateEntity(PLAYER_NAME, PLAYER_DIRECTORY));
        playerDialogue.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 24));
        playerDialogue.setTextFill(Color.BLUEVIOLET);
        this.main.getGamePane().getPlayerPane(true).getChildren().add(this.playerDialogue);
    }

    @Override
    public void update(double time) {
        super.update(time);
        feetPositionY = positionY + 80;
        feetPositionX = positionX + 30;
        if(this.target != null) {
            updateWalking();
        }
        playerDialogue.setLayoutX(positionX);
        playerDialogue.setLayoutY(positionY-30);
        if(timing) {
            labelTimer += time;
            if(labelTimer >= 20) {
                setLabelVisible(false);
                setLabel("");
                labelTimer = 0;
                timing = false;
            }
        }
    }

    public void startTimer() {
        setLabelVisible(true);
        labelTimer = 0;
        timing = true;
    }

    public void setTarget(String name) { this.target = main.getEntityView().getEntity(name); }
    public void setTarget(Entity target) {
        this.target = target;
    }

    public void updateWalking() {
        double dist;
        if(!(this.intersects(target))) {
            dist = target.positionX - this.feetPositionX;
            velocityX = (Math.abs(dist) > 40) ? dist/20 : (dist < 0) ? -4 : 4;
            dist = target.positionY - this.feetPositionY;
            velocityY = (Math.abs(dist) > 40) ? dist/20 : (dist < 0) ? -4 : 4;
        } else {
            this.setVelocity(0,0);
        }
    }

    public void setLabel(String text) {
        playerDialogue.setText(text);
    }

    public void setLabelVisible(boolean b) {
        if(b) {
            playerDialogue.setVisible(true);
        } else {
            playerDialogue.setVisible(false);
        }
    }

    @Override
    public Rectangle2D getBoundary() {
        Rectangle2D boundingRect = new Rectangle2D(feetPositionX, feetPositionY, SPECIAL_BOUNDING_WIDTH, SPECIAL_BOUNDING_HEIGHT);
        return boundingRect;
    }
}
