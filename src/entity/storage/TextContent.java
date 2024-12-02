package entity.storage;

import lombok.Getter;
import lombok.Setter;
import window.enums.ColorName;

import java.awt.*;

@Setter @Getter
public class TextContent extends AbstractContent {
    private String value;
    private int x;
    private int y;
    private String color;

    public String Value(){ return value; }
    public int X(){ return x; }
    public int Y(){ return y; }
    public String Color(){ return color; }

    @Override
    public void draw(Graphics g) {
        g.setColor(ColorName.getColor(color));
        g.drawString(value, x, y);
    }
}
