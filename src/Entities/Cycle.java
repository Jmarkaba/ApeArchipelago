package Entities;

import javafx.scene.image.Image;

public class Cycle {

    private Image[] animationFrames;
    private int currentFrame;

    public Cycle(String[] imagePaths, double width, double height) {
        animationFrames = new Image[imagePaths.length];
        for(int i = 0; i < imagePaths.length; ++i) {
            animationFrames[i] = new Image(imagePaths[i], width, height, false, false);
        }

    }

    public Cycle(Image[] images) {
        animationFrames = images;
    }

    public Image next() {
        Image next = animationFrames[currentFrame];
        currentFrame++;
        if (currentFrame == animationFrames.length)
            currentFrame = 0;
        return next;
    }
}
