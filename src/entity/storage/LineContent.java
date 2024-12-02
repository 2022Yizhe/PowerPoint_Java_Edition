package entity.storage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LineContent extends AbstractContent{
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private String color;


    public int StartX(){ return startX; }
    public int StartY(){ return startY; }
    public int EndX(){ return endX; }
    public int EndY(){ return endY; }
    public String Color(){ return color; }
}
