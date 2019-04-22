package Entities;

import GUIComponents.PrimaryView;
import javafx.geometry.Rectangle2D;

public abstract class Entity {

    protected String entityName;
    protected double width, height;
    protected double positionX, positionY;
    protected double velocityX, velocityY;
    public boolean isBackground = true;

    protected Cycle currentCycle;
    protected Cycle walkRight, walkLeft, walkToward, walkAway;

    public Entity() {}

    public void setDimensions(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void setPosition(double x, double y) {
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }

    public void changeVelocity(double dx, double dy) {
        velocityX += dx;
        velocityY += dy;
    }

    public void update(double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public Rectangle2D getBoundary() {
        Rectangle2D boundingRect = new Rectangle2D(positionX, positionY, width, height);
        return boundingRect;
    }

    public boolean intersects(Entity other) {
        return this.getBoundary().intersects(other.getBoundary());
    }

    public void addCycle(String type, String[] imagePaths) {
        Cycle cycle = new Cycle(imagePaths, this.width, this.height);
        switch (type) {
            case "left":
                walkLeft = cycle;
                break;
            case "right":
                walkRight = cycle;
                break;
            case "toward":
                walkToward = cycle;
                break;
            case "away":
                walkAway = cycle;
                break;
            default:
                currentCycle = cycle;
        }
    }

    public boolean hasHorizonalMovement() {
        return walkRight != null && walkLeft != null;
    }

    public boolean hasVerticalMovement() {
        return walkToward != null && walkAway != null;
    }

    public void setIsBackground(boolean b) {
        isBackground = b;
    }

    public void setEntityName(String name) {
        this.entityName = name;
    }

    public String getName() { return entityName; }

    public abstract void render(PrimaryView main);

    public String toString() {
        return "EntityName: " + entityName +
                "\nWidth: " + width +
                "\nHeight: " + height +
                "\nPosition: [" + positionX + "," + positionY + "]" +
                "\nVelocity: [" + velocityX + "," + velocityY + "]";
    }
}
