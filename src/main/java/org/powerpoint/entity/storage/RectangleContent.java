package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class RectangleContent extends AbstractContent{
    private int width;
    private int height;
    private int x;
    private int y;
    private String color;
    private int thickness;
}
