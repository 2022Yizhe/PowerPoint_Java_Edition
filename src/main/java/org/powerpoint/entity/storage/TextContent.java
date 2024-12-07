package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

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
}
