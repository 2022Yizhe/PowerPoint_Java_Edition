package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImageContent extends AbstractContent {
    private String src;
    private int width;
    private int height;
    private int x;
    private int y;
}