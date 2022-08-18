package ru.develgame.jsfgame;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("imageTestBean")
@SessionScoped
public class ImageTestBean implements Serializable {
    private int currentFrame = 1;

    private int imageTop = 0;

    public String loadImage() {
        String image = "/images/walk_down" + currentFrame + ".png";
        currentFrame++;
        if (currentFrame > 8)
            currentFrame = 1;
        return image;
    }

    public String getNewTopPosition() {
        imageTop = imageTop + 2;
        return Integer.toString(imageTop);
    }

    public int getImageTop() {
        return imageTop;
    }

    public void setImageTop(int imageTop) {
        this.imageTop = imageTop;
    }
}
