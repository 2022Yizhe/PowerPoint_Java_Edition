package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class OvalContent extends AbstractContent{
    private int radiusX;
    private int radiusY;
    private int x;
    private int y;
    private String color;

    public int RadiusX() { return radiusX; }
    public int RadiusY() { return radiusY; }
    public int X() { return x; }
    public int Y() { return y; }
    public String Color() { return color; }
}
