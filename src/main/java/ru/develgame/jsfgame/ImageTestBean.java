package ru.develgame.javaeejsf;

import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("imageTestBean")
@SessionScoped
public class ImageTestBean implements Serializable {
    private int currentFrame = 1;

    public String loadImage() {
        String image = "/images/walk_down" + currentFrame + ".png";
        currentFrame++;
        if (currentFrame > 8)
            currentFrame = 1;
        return image;
    }
}
