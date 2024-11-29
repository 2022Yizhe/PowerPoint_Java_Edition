package entity.storage;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class OvalContent extends AbstractContent{
    private int radiusX;
    private int radiusY;
    private int x;
    private int y;
    private String color;
}
