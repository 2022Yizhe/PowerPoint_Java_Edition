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
}
