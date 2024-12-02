package entity.storage;

import lombok.Getter;
import lombok.Setter;
import window.enums.ColorName;

import java.awt.*;

@Getter @Setter
public class CircleContent extends AbstractContent{
    private int radius;
    private int x;
    private int y;
    private String color;

    public int Radius(){ return radius; }
    public int X(){ return x; }
    public int Y(){ return y; }
    public String Color(){ return color; }

    @Override
    public void draw(Graphics g) {
        g.setColor(ColorName.getColor(color));
        // TODO
    }
}
