package entity.storage;

import lombok.Getter;
import lombok.Setter;
import window.enums.ColorName;

import java.awt.*;

@Getter @Setter
public class ImageContent extends AbstractContent {
    private String src;
    private int width;
    private int height;
    private int x;
    private int y;

    public String Src(){ return src; }
    public int Width(){ return width; }
    public int Height(){ return height; }
    public int X(){ return x; }
    public int Y(){ return y; }

    @Override
    public void draw(Graphics g) {
        // TODO
    }
}