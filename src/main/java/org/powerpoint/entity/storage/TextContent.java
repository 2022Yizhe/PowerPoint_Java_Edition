package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class TextContent extends AbstractContent {
    private String value;
    private int x;
    private int y;
    private String color;
    private String font;
    private int size;
}
