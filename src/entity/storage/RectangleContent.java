package entity.storage;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class RectangleContent extends AbstractContent{
    private int width;
    private int height;
    private int x;
    private int y;
    private String color;

    public int Width(){ return width; }
    public int Height(){ return height; }
    public int X(){ return x; }
    public int Y(){ return y; }
    public String Color(){ return color; }
}
