package Entities;

import GUIComponents.PrimaryView;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class InteractableEntity extends Entity {

    private CustomImageView imView;
    protected PrimaryView main;

    public InteractableEntity(PrimaryView pv, InteractableEntity other) {
        this.main = pv;
        this.entityName = other.entityName;
        this.width = other.width; this.height = other.height;
        this.imView = other.imView;
        this.positionX = other.positionX; this.positionY = other.positionY;
        this.velocityX = other.velocityX; this.velocityY = other.velocityY;
        this.currentCycle = other.currentCycle;
        this.walkRight = other.walkRight; this.walkLeft = other.walkLeft;
        this.walkAway = other.walkAway; this.walkToward = other.walkToward;
    }

    public InteractableEntity(PrimaryView pv, Image i, String name) {
        this.main = pv;
        imView = new CustomImageView(i, this.main);
        this.entityName = name;
    }

    public InteractableEntity(PrimaryView pv, Image i, String name, double positionX, double positionY) {
        this.main = pv;
        imView = new CustomImageView(i, this.main);
        setPosition(positionX, positionY);
        this.entityName = name;
    }

    @Override
    public void update(double time) {
        super.update(time);
        if(hasHorizonalMovement() && velocityX != 0 && Math.abs(velocityX) >= Math.abs(velocityY)) {
            currentCycle = velocityX >= 0 ? walkRight : walkLeft;
            setImageView(currentCycle.next());
        }
        else if(hasVerticalMovement() && velocityY != 0) {
            currentCycle = velocityY > 0 ? walkToward : walkAway;
            setImageView(currentCycle.next());
        }
        if(entityName.equals("monkey")) {
            Entity banana = main.getEntityView().getEntity("banana");
            if (banana != null) {
                double dist;
                if(!(this.intersects(banana))) {
                    dist = banana.positionX - this.positionX;
                    velocityX = (Math.abs(dist) > 40) ? dist/20 : (dist < 0) ? -4 : 4;
                    dist = banana.positionY - this.positionY;
                    velocityY = (Math.abs(dist) > 40) ? dist/20 : (dist < 0) ? -4 : 4;
                } else {
                    this.setVelocity(0,0);
                    main.endGame();
                }
            }
        }
    }

    public void render(PrimaryView main) {
        if(!main.getGamePane().getPlayerPane(isBackground).getChildren().contains(this.imView))
            main.getGamePane().getPlayerPane(isBackground).getChildren().add(this.imView);
        imView.setX(positionX);
        imView.setY(positionY);
    }

    public void setImageView(Image i) {
        imView.setImage(i);
    }

    public CustomImageView getImageView() {
        return imView;
    }

    protected class CustomImageView extends ImageView{

        private PrimaryView main;

        public CustomImageView(Image i, PrimaryView main) {
            super(i);
            super.setPickOnBounds(true);
            this.main = main;

            this.addEventHandler(MouseEvent.MOUSE_ENTERED, event ->  {
                getScene().setCursor(Cursor.CROSSHAIR);
                main.getCommandBox().setCommandLabelText(entityName);
                event.consume();
            });
            this.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                getScene().setCursor(Cursor.DEFAULT);
                main.getCommandBox().setCommandLabelText("");
                event.consume();
            });
            this.addEventHandler(MouseEvent.MOUSE_CLICKED, new InteractableHandler(this.main));
        }
    }

    protected class InteractableHandler implements EventHandler<MouseEvent> {

        private PrimaryView main;

        public InteractableHandler(PrimaryView main) {
            super();
            this.main = main;
        }

        public void handle(MouseEvent event) {
            switch (this.main.getCommand()) {
                case OPEN:
                    this.main.getPlayer().setLabel("I can't do that");
                    this.main.getPlayer().startTimer();
                    break;
                case LOOK:
                    this.main.getPlayer().setLabel("It's a " + entityName);
                    this.main.getPlayer().startTimer();
                    break;
                case WALK:
                    this.main.getPlayer().setTarget(entityName);
                    try {
                        if (entityName.equals("beach")) {
                            this.main.nextScene("beach");
                        } else if (entityName.equals("jungle")) {
                            this.main.nextScene("jungle");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case TALK:
                    this.main.getPlayer().setLabel("I can't do that");
                    this.main.getPlayer().startTimer();
                    break;
                case PUSH:
                    this.main.getPlayer().setLabel("I can't do that");
                    this.main.getPlayer().startTimer();
                    break;
                case CLOSE:
                    this.main.getPlayer().setLabel("I can't do that");
                    this.main.getPlayer().startTimer();
                    break;
                case GRAB:
                    this.main.getPlayer().setTarget(entityName);
                    if (entityName.equals("banana") && !this.main.getCommandBox().hasItem("banana")) {
                        this.main.getCommandBox().addItem(entityName, imView.getImage(), main.getEntityView().getEntity(entityName));
                        this.main.getEntityView().removeEntity(entityName);
                    } else if (entityName.equals("monkey")) {
                        this.main.getPlayer().setLabel("How can I get the monkey to come down?");
                        this.main.getPlayer().startTimer();
                    } else {
                        this.main.getPlayer().setLabel("I can't do that");
                        this.main.getPlayer().startTimer();
                    }
                    break;
                case USE:
                    this.main.getPlayer().setTarget(entityName);
                    if (entityName.equals("banana") && this.main.currentScene.equals("beach")) {
                        this.main.getCommandBox().addItem(entityName, imView.getImage(), main.getEntityView().getEntity(entityName));
                        this.main.getEntityView().removeEntity(entityName);
                    } else {
                        this.main.getPlayer().setLabel("I can't do that");
                        this.main.getPlayer().startTimer();
                    }
                    break;
                case DROP:
                    this.main.getPlayer().setLabel("I can't do that");
                    this.main.getPlayer().startTimer();
                    break;
            }
            event.consume();
        }
    }
}
