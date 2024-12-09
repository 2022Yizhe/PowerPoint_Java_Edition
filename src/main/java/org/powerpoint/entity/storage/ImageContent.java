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

    /**
     * 初始化图片参数为默认值
     * 坐标参数通过鼠标点击事件传递
     */
    public void initDefault(int x, int y) {
        super.setContentType("image");
        this.src = "null";
        this.width = 400;
        this.height = 400;
        this.x = x;
        this.y = y;
    }
}