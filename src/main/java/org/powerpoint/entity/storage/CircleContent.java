package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CircleContent extends AbstractContent{
    private int radius;
    private int x;
    private int y;
    private String color;
    private int thickness;
}
