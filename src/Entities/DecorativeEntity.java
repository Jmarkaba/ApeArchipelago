package Entities;

import GUIComponents.PrimaryView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class DecorativeEntity extends Entity {

    private Image entityImage;

    public DecorativeEntity(Image entityImage, String entityName) {
        this.entityName = entityName;
        setImage(entityImage);
    }

    public DecorativeEntity(Image entityImage, String entityName, double positionX, double positionY) {
        this.entityName = entityName;
        setImage(entityImage);
        setPosition(positionX, positionY);
    }

    public void setImage(Image entityImage) {
        this.entityImage = entityImage;
        width = entityImage.getWidth();
        height = entityImage.getHeight();
    }

    public void render(PrimaryView main) {
        Canvas can = main.getGamePane().getCanvas(isBackground);
        GraphicsContext gc = can.getGraphicsContext2D();
        gc.drawImage(entityImage, positionX, positionY);
    }

    @Override
    public void update(double time) {
        super.update(time);
        if(hasHorizonalMovement() && velocityX != 0 && velocityX >= velocityY) {
            currentCycle = velocityX > 0 ? walkRight : walkLeft;
            this.entityImage = currentCycle.next();
        }
        else if(hasVerticalMovement() && velocityY != 0) {
            currentCycle = velocityY > 0 ? walkToward : walkAway;
            this.entityImage = currentCycle.next();
        } else {
            currentCycle = null;
        }
    }

    public Image getEntityImage() { return entityImage; }
}
